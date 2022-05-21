package uk.co.mruoc.test.clock;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FixedClockMother {

    public static Clock utcNow() {
        return utc(Instant.now());
    }

    public static Clock utc(Instant now) {
        return Clock.fixed(now, ZoneOffset.UTC);
    }
}
