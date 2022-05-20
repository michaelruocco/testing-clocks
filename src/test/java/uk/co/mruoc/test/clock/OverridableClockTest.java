package uk.co.mruoc.test.clock;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.mruoc.test.clock.InstantAssert.assertThatInstant;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
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
    void shouldAddDurationToOverride() {
        Instant override = Instant.parse("2021-01-07T19:10:00.000Z");
        OverridableClock clock = new OverridableClock(override);
        Duration duration = Duration.ofMinutes(5);

        clock.plus(duration);

        assertThat(clock.instant()).isEqualTo(override.plus(duration));
    }

    @Test
    void shouldBeAbleToUpdateAndClearOverride() {
        OverridableClock clock = new OverridableClock();

        assertOverriddenTimeReturned(clock, Instant.parse("2021-02-05T22:22:22.222Z"));
        assertOverriddenTimeReturned(clock, Instant.parse("2021-03-06T11:11:11.111Z"));

        clock.clearOverride();

        assertThatInstant(clock.instant()).isCloseToCurrentTime();
    }

    private void assertOverriddenTimeReturned(OverridableClock clock, Instant override) {
        clock.setOverride(override);
        assertThat(clock.instant()).isEqualTo(override);
    }
}
