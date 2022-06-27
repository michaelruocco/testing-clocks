package uk.co.mruoc.test.clock;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class OverridableClock extends Clock {

    private static final Clock DEFAULT_CLOCK = Clock.systemUTC();
    private static final Duration DEFAULT_OFFSET = Duration.ZERO;

    private final Clock clock;
    private Duration offset;
    private List<Instant> overrides;

    public OverridableClock() {
        this(Collections.emptyList());
    }

    public OverridableClock(Instant... overrides) {
        this(toList(overrides));
    }

    public OverridableClock(Collection<Instant> overrides) {
        this(DEFAULT_CLOCK, DEFAULT_OFFSET, overrides);
    }

    public OverridableClock(Clock clock, Duration offset, Collection<Instant> overrides) {
        this.clock = clock;
        this.offset = offset;
        this.overrides = toList(overrides);
    }

    public void setOffset(Duration offset) {
        log.debug("set offset {}", offset);
        this.offset = offset;
    }

    public Duration getOffset() {
        return offset;
    }

    public void clearOffset() {
        setOffset(Duration.ZERO);
    }

    public void setOverrides(Instant... overrides) {
        setOverrides(new ArrayList<>(Arrays.asList(overrides)));
    }

    public void setOverrides(Collection<Instant> overrides) {
        log.debug("set overrides {}", overrides);
        this.overrides = toList(overrides);
    }

    public Collection<Instant> getOverrides() {
        return overrides;
    }

    public void clearOverrides() {
        setOverrides(Collections.emptyList());
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new OverridableClock(clock.withZone(zone), offset, overrides);
    }

    @Override
    public Instant instant() {
        Instant instant = getOverride().orElseGet(this::getCurrentTime);
        return instant.plus(offset);
    }

    private Optional<Instant> getOverride() {
        if (CollectionUtils.isEmpty(overrides)) {
            return Optional.empty();
        }
        Instant override = overrides.remove(0);
        log.debug("returning override time {}", override);
        return Optional.ofNullable(override);
    }

    private Instant getCurrentTime() {
        Instant instant = clock.instant();
        log.debug("returning current time {}", instant);
        return instant;
    }

    private static List<Instant> toList(Instant... values) {
        return toList(Arrays.asList(values));
    }

    private static List<Instant> toList(Collection<Instant> values) {
        return new ArrayList<>(values);
    }
}
