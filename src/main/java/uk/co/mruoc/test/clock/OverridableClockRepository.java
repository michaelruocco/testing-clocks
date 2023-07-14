package uk.co.mruoc.test.clock;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface OverridableClockRepository {

    void setOffset(Duration offset);

    Optional<Duration> getOffset();

    void clearOffset();

    void setOverrides(Instant... overrides);

    void setOverrides(Collection<Instant> overrides);

    Collection<Instant> getOverrides();

    void clearOverrides();

    Optional<Instant> removeNextOverride();
}
