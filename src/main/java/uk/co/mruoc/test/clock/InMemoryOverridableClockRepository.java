package uk.co.mruoc.test.clock;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

@Slf4j
public class InMemoryOverridableClockRepository implements OverridableClockRepository {

    private static final Duration DEFAULT_OFFSET = Duration.ZERO;

    private Duration offset;
    private List<Instant> overrides;

    public InMemoryOverridableClockRepository() {
        this(Collections.emptyList());
    }

    public InMemoryOverridableClockRepository(Instant... overrides) {
        this(toList(overrides));
    }

    public InMemoryOverridableClockRepository(Collection<Instant> overrides) {
        this(DEFAULT_OFFSET, overrides);
    }

    public InMemoryOverridableClockRepository(Duration offset, Collection<Instant> overrides) {
        this.offset = offset;
        this.overrides = toList(overrides);
    }

    public void setOffset(Duration offset) {
        log.debug("set offset {}", offset);
        this.offset = offset;
    }

    @Override
    public Duration getOffset() {
        return offset;
    }

    @Override
    public void clearOffset() {
        setOffset(Duration.ZERO);
    }

    @Override
    public void setOverrides(Instant... overrides) {
        setOverrides(new ArrayList<>(Arrays.asList(overrides)));
    }

    @Override
    public void setOverrides(Collection<Instant> overrides) {
        log.debug("set overrides {}", overrides);
        this.overrides = toList(overrides);
    }

    @Override
    public Collection<Instant> getOverrides() {
        return overrides;
    }

    @Override
    public void clearOverrides() {
        setOverrides(Collections.emptyList());
    }

    @Override
    public Optional<Instant> removeNextOverride() {
        if (CollectionUtils.isEmpty(overrides)) {
            return Optional.empty();
        }
        Instant override = overrides.remove(0);
        log.debug("returning override time {}", override);
        return Optional.ofNullable(override);
    }

    private static List<Instant> toList(Instant... values) {
        return toList(Arrays.asList(values));
    }

    private static List<Instant> toList(Collection<Instant> values) {
        return new ArrayList<>(values);
    }
}
