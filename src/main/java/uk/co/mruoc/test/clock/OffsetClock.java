package uk.co.mruoc.test.clock;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OffsetClock extends Clock {

    private static final Clock DEFAULT_CLOCK = Clock.systemUTC();
    private static final Duration DEFAULT_OFFSET = Duration.ZERO;

    private final Clock clock;
    private Duration offset;

    public OffsetClock() {
        this(DEFAULT_CLOCK);
    }

    public OffsetClock(Clock clock) {
        this(clock, DEFAULT_OFFSET);
    }

    public OffsetClock(Duration offset) {
        this(DEFAULT_CLOCK, offset);
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new OffsetClock(clock.withZone(zone), offset);
    }

    @Override
    public Instant instant() {
        Instant instant = clock.instant().plus(offset);
        log.debug("returning instant {} using offset {}", instant, offset);
        return instant;
    }

    public Duration getOffset() {
        return offset;
    }

    public void setOffset(Duration offset) {
        this.offset = offset;
        log.debug("set offset {}", offset);
    }

    public void reset() {
        setOffset(DEFAULT_OFFSET);
    }
}
