package uk.co.mruoc.test.clock;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class OverridableClock extends Clock {

    private static final Clock DEFAULT_CLOCK = Clock.systemUTC();

    private final Clock clock;
    private Instant override;

    public OverridableClock() {
        this(DEFAULT_CLOCK);
    }

    public OverridableClock(Instant override) {
        this(DEFAULT_CLOCK, override);
    }

    public OverridableClock(Clock clock) {
        this(clock, null);
    }

    public OverridableClock(Clock clock, Instant override) {
        this.clock = clock;
        this.setOverride(override);
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new OverridableClock(clock.withZone(zone), override);
    }

    @Override
    public Instant instant() {
        return getOverride().orElseGet(this::getCurrentTime);
    }

    private Instant getCurrentTime() {
        Instant instant = clock.instant();
        log.debug("returning current time {}", instant);
        return instant;
    }

    public void plus(Duration duration) {
        setOverride(override.plus(duration));
    }

    public void setOverride(Instant override) {
        this.override = override;
        log.debug("set override {}", override);
    }

    public void clearOverride() {
        setOverride(null);
    }

    private Optional<Instant> getOverride() {
        if (override != null) {
            log.debug("return overridden time {}", override);
        }
        return Optional.ofNullable(override);
    }

}
