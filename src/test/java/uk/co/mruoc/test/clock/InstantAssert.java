package uk.co.mruoc.test.clock;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.data.TemporalOffset;
import org.assertj.core.data.TemporalUnitOffset;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class InstantAssert extends AbstractAssert<InstantAssert, Instant> {

    private static final TemporalUnitOffset DEFAULT_VARIANCE = within(250, MILLIS);

    private InstantAssert(Instant instant) {
        super(instant, InstantAssert.class);
    }

    public static InstantAssert assertThatInstant(Instant actual) {
        return new InstantAssert(actual);
    }

    public InstantAssert isCloseToCurrentTime() {
        return isCloseToCurrentTime(DEFAULT_VARIANCE);
    }

    public InstantAssert isCloseToCurrentTime(TemporalOffset<Temporal> allowedVariance) {
        isNotNull();
        Instant expected = Instant.now();
        assertThat(actual).isCloseTo(expected, allowedVariance);
        return this;
    }

    public InstantAssert isCloseToCurrentTimeWithOffset(Duration offset) {
        return isCloseToCurrentTimeWithOffset(offset, DEFAULT_VARIANCE);
    }

    public InstantAssert isCloseToCurrentTimeWithOffset(Duration offset, TemporalOffset<Temporal> allowedVariance) {
        isNotNull();
        Instant expected = Instant.now().plus(offset);
        assertThat(actual).isCloseTo(expected, allowedVariance);
        return this;
    }

}
