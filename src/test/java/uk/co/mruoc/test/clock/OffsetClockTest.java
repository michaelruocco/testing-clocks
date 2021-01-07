package uk.co.mruoc.test.clock;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.mruoc.test.clock.InstantAssert.assertThatInstant;


class OffsetClockTest {

    @Test
    void shouldHaveUtcZoneByDefault() {
        Clock clock = new OffsetClock();

        ZoneId zoneId = clock.getZone();

        assertThat(zoneId).isEqualTo(ZoneId.of("Z"));
    }

    @Test
    void shouldReturnClockWithUpdatedZone() {
        Clock clock = new OffsetClock();

        Clock updated = clock.withZone(ZoneId.of("GMT"));

        assertThat(updated.getZone()).isEqualTo(ZoneId.of("GMT"));
    }

    @Test
    void shouldReturnCurrentTimeNoOffsetConfigured() {
        Clock clock = new OffsetClock();

        Instant instant = clock.instant();

        assertThatInstant(instant).isCloseToCurrentTime();
    }

    @Test
    void shouldReturnCurrentTimeWithOffsetAppliedIfOffsetConfigured() {
        Duration offset = Duration.ofMinutes(5);
        Clock clock = new OffsetClock(offset);

        Instant instant = clock.instant();

        assertThatInstant(instant).isCloseToCurrentTimeWithOffset(offset);
    }

    @Test
    void shouldReturnOffset() {
        Duration offset = Duration.ofMinutes(5);

        OffsetClock clock = new OffsetClock(offset);

        assertThat(clock.getOffset()).isEqualTo(offset);
    }

    @Test
    void shouldBeAbleToUpdateAndResetOffset() {
        OffsetClock clock = new OffsetClock();

        setOffsetAndAssertOffsetTimeReturned(clock, Duration.ofMinutes(3));
        setOffsetAndAssertOffsetTimeReturned(clock, Duration.ofHours(-7));

        clock.reset();

        assertThatInstant(clock.instant()).isCloseToCurrentTime();
    }

    private void setOffsetAndAssertOffsetTimeReturned(OffsetClock clock, Duration offset) {
        clock.setOffset(offset);
        assertThatInstant(clock.instant()).isCloseToCurrentTimeWithOffset(offset);
    }

}
