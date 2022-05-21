package uk.co.mruoc.test.clock;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.mruoc.test.clock.InstantAssert.assertThatInstant;

import java.time.Clock;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class FixedClockMotherTest {

    @Test
    void shouldReturnFixedClockWithUtcZone() {
        Clock clock = FixedClockMother.utc(Instant.parse("2022-05-20T05:10:34Z"));

        assertThat(clock.getZone()).isEqualTo(UTC);
    }

    @Test
    void shouldReturnFixedClockTime() {
        Instant now = Instant.parse("2022-05-21T06:21:45Z");
        Clock clock = FixedClockMother.utc(now);

        Instant instant1 = clock.instant();
        Instant instant2 = clock.instant();
        Instant instant3 = clock.instant();

        assertThatInstant(instant1).isEqualTo(now);
        assertThat(instant1).isEqualTo(instant2);
        assertThat(instant2).isEqualTo(instant3);
    }

    @Test
    void shouldReturnFixedClockWithUtcZoneNow() {
        Clock clock = FixedClockMother.utcNow();

        assertThat(clock.getZone()).isEqualTo(UTC);
    }

    @Test
    void shouldReturnFixedClockTimeNow() {
        Clock clock = FixedClockMother.utcNow();

        Instant instant1 = clock.instant();
        Instant instant2 = clock.instant();
        Instant instant3 = clock.instant();

        assertThatInstant(instant1).isCloseToCurrentTime();
        assertThat(instant1).isEqualTo(instant2);
        assertThat(instant2).isEqualTo(instant3);
    }
}
