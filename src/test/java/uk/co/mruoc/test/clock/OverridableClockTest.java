package uk.co.mruoc.test.clock;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.mruoc.test.clock.InstantAssert.assertThatInstant;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collection;
import org.junit.jupiter.api.Test;

class OverridableClockTest {

    @Test
    void shouldHaveUtcZoneByDefault() {
        Clock clock = new OverridableClock();

        ZoneId zoneId = clock.getZone();

        assertThat(zoneId).isEqualTo(ZoneId.of("Z"));
    }

    @Test
    void shouldReturnClockWithUpdatedZone() {
        Clock clock = new OverridableClock();

        Clock updated = clock.withZone(ZoneId.of("GMT"));

        assertThat(updated.getZone()).isEqualTo(ZoneId.of("GMT"));
    }

    @Test
    void shouldReturnCurrentTimeNoOverrideConfigured() {
        Clock clock = new OverridableClock();

        Instant instant = clock.instant();

        assertThatInstant(instant).isCloseToCurrentTime();
    }

    @Test
    void shouldReturnOverriddenCurrentTimeIfConfigured() {
        Instant override = Instant.parse("2021-01-04T23:24:07.385Z");
        Clock clock = new OverridableClock(override);

        Instant instant = clock.instant();

        assertThat(instant).isEqualTo(override);
    }

    @Test
    void shouldReturnMultipleOverriddenCurrentTimesIfConfigured() {
        Instant override1 = Instant.parse("2021-01-04T23:24:07.385Z");
        Instant override2 = Instant.parse("2021-02-05T22:20:13.474Z");
        Clock clock = new OverridableClock(override1, override2);

        Instant instant1 = clock.instant();
        Instant instant2 = clock.instant();
        Instant instant3 = clock.instant();

        assertThat(instant1).isEqualTo(override1);
        assertThat(instant2).isEqualTo(override2);
        assertThatInstant(instant3).isCloseToCurrentTime();
    }

    @Test
    void shouldReturnOverridesIfConfigured() {
        Instant override1 = Instant.parse("2021-01-04T23:24:07.385Z");
        Instant override2 = Instant.parse("2021-02-05T22:20:13.474Z");
        OverridableClock clock = new OverridableClock(override1, override2);

        Collection<Instant> overrides = clock.getOverrides();

        assertThat(overrides).containsExactly(override1, override2);
    }

    @Test
    void shouldAddOffsetToOverrideIfSet() {
        Instant override = Instant.parse("2021-01-07T19:10:00.000Z");
        OverridableClock clock = new OverridableClock(override);
        Duration offset = Duration.ofMinutes(5);

        clock.setOffset(offset);

        assertThat(clock.instant()).isEqualTo(override.plus(offset));
    }

    @Test
    void shouldBeAbleToUpdateAndClearOverride() {
        OverridableClock clock = new OverridableClock();
        assertOverriddenTimeReturned(clock, Instant.parse("2021-02-05T22:22:22.222Z"));
        assertOverriddenTimeReturned(clock, Instant.parse("2021-03-06T11:11:11.111Z"));

        clock.clearOverrides();

        assertThatInstant(clock.instant()).isCloseToCurrentTime();
    }

    @Test
    void shouldSetOffsetIfOverrideIsNotSet() {
        OverridableClock clock = new OverridableClock();
        Duration offset = Duration.ofMinutes(3);

        clock.setOffset(offset);

        assertThatInstant(clock.instant()).isCloseToCurrentTimeWithOffset(offset);
    }

    @Test
    void shouldReturnOffsetIfSet() {
        OverridableClock clock = new OverridableClock();
        Duration expectedOffset = Duration.ofMinutes(3);
        clock.setOffset(expectedOffset);

        Duration offset = clock.getOffset();

        assertThat(offset).isEqualTo(expectedOffset);
    }

    @Test
    void shouldClearOffset() {
        OverridableClock clock = new OverridableClock();
        clock.setOffset(Duration.ofMinutes(3));

        clock.clearOffset();

        assertThatInstant(clock.instant()).isCloseToCurrentTime();
    }

    private void assertOverriddenTimeReturned(OverridableClock clock, Instant override) {
        clock.setOverrides(override);
        assertThat(clock.instant()).isEqualTo(override);
    }
}
