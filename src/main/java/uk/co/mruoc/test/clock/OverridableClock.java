package uk.co.mruoc.test.clock;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class OverridableClock extends Clock {

    private static final Clock DEFAULT_CLOCK = Clock.systemUTC();

    private final Clock clock;
    private final OverridableClockRepository repository;

    public OverridableClock() {
        this(DEFAULT_CLOCK, new InMemoryOverridableClockRepository());
    }

    public OverridableClock(Instant... overrides) {
        this(new InMemoryOverridableClockRepository(overrides));
    }

    public OverridableClock(OverridableClockRepository repository) {
        this(DEFAULT_CLOCK, repository);
    }

    public void setOffset(Duration offset) {
        repository.setOffset(offset);
    }

    public Duration getOffset() {
        return repository.getOffset().orElse(Duration.ZERO);
    }

    public void clearOffset() {
        repository.clearOffset();
    }

    public void setOverrides(Instant... overrides) {
        repository.setOverrides(overrides);
    }

    public void setOverrides(Collection<Instant> overrides) {
        repository.setOverrides(overrides);
    }

    public Collection<Instant> getOverrides() {
        return repository.getOverrides();
    }

    public void clearOverrides() {
        repository.clearOverrides();
    }

    @Override
    public ZoneId getZone() {
        return clock.getZone();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new OverridableClock(clock.withZone(zone), repository);
    }

    @Override
    public Instant instant() {
        Instant instant = repository.removeNextOverride().orElseGet(this::getCurrentTime);
        return instant.plus(getOffset());
    }

    private Instant getCurrentTime() {
        Instant instant = clock.instant();
        log.debug("returning current time {}", instant);
        return instant;
    }
}
