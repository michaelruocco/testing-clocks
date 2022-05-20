package uk.co.mruoc.test.clock;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.mruoc.test.clock.InstantAssert.assertThatInstant;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class PreConfiguredClockTest {

    @Test
    void shouldHaveUtcZoneByDefault() {
        Clock clock = new PreConfiguredClock();

        ZoneId zoneId = clock.getZone();

        assertThat(zoneId).isEqualTo(ZoneId.of("Z"));
    }

    @Test
    void shouldReturnClockWithUpdatedZone() {
        Clock clock = new PreConfiguredClock();

        Clock updated = clock.withZone(ZoneId.of("GMT"));

        assertThat(updated.getZone()).isEqualTo(ZoneId.of("GMT"));
    }

    @Test
    void shouldReturnCurrentTimeIfNoTimesConfigured() {
        Clock clock = new PreConfiguredClock();

        Instant instant = clock.instant();

        assertThatInstant(instant).isCloseToCurrentTime();
    }

    @Test
    void shouldReturnConfiguredTimesInOrderAndLoopBackToFirstValueWhenAllValuesUsed() {
        Instant expected1 = Instant.parse("2021-01-05T20:14:00.000Z");
        Instant expected2 = Instant.parse("2022-02-06T21:15:11.111Z");
        Clock clock = new PreConfiguredClock(expected1, expected2);

        assertThat(clock.instant()).isEqualTo(expected1);
        assertThat(clock.instant()).isEqualTo(expected2);
        assertThat(clock.instant()).isEqualTo(expected1);
        assertThat(clock.instant()).isEqualTo(expected2);
    }
}
