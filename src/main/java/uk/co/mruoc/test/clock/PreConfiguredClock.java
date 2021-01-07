package uk.co.mruoc.test.clock;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class PreConfiguredClock extends Clock {

    private static final Clock DEFAULT_CLOCK = Clock.systemUTC();

    private final Clock clock;
    private final List<Instant> values;
    private final IntUnaryOperator incrementOrReset;
    private final AtomicInteger index = new AtomicInteger();

    public PreConfiguredClock(Instant... values) {
        this(Arrays.asList(values));
    }

    public PreConfiguredClock(Collection<Instant> values) {
        this(DEFAULT_CLOCK, new ArrayList<>(values));
    }

    public PreConfiguredClock(Clock clock, List<Instant> values) {
        this.clock = clock;
        this.values = values;
        this.incrementOrReset = new IncrementOrReset(values.size());
        log.debug("configured with values {}", values);
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new PreConfiguredClock(clock.withZone(zone), values);
    }

    @Override
    public Instant instant() {
        Instant instant = calculateInstant();
        log.debug("returning instant {}", instant);
        return instant;
    }

    private Instant calculateInstant() {
        if (values.isEmpty()) {
            log.info("returning current time");
            return clock.instant();
        }
        return values.get(index.getAndUpdate(incrementOrReset));
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class IncrementOrReset implements IntUnaryOperator {

        private final int size;

        @Override
        public int applyAsInt(int operand) {
            log.debug("using index for configured value {}", operand);
            int incremented = operand + 1;
            if (incremented >= size) {
                return 0;
            }
            return incremented;
        }
    }

}
