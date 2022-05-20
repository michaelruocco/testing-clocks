# Testing Clocks

[![Build](https://github.com/michaelruocco/testing-clocks/workflows/pipeline/badge.svg)](https://github.com/michaelruocco/testing-clocks/actions)
[![codecov](https://codecov.io/gh/michaelruocco/testing-clocks/branch/master/graph/badge.svg?token=FWDNP534O7)](https://codecov.io/gh/michaelruocco/testing-clocks)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/272889cf707b4dcb90bf451392530794)](https://www.codacy.com/gh/michaelruocco/testing-clocks/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=michaelruocco/testing-clocks&amp;utm_campaign=Badge_Grade)
[![BCH compliance](https://bettercodehub.com/edge/badge/michaelruocco/testing-clocks?branch=master)](https://bettercodehub.com/)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_testing-clocks&metric=alert_status)](https://sonarcloud.io/dashboard?id=michaelruocco_testing-clocks)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_testing-clocks&metric=sqale_index)](https://sonarcloud.io/dashboard?id=michaelruocco_testing-clocks)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_testing-clocks&metric=coverage)](https://sonarcloud.io/dashboard?id=michaelruocco_testing-clocks)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=michaelruocco_testing-clocks&metric=ncloc)](https://sonarcloud.io/dashboard?id=michaelruocco_testing-clocks)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.michaelruocco/testing-clocks.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.michaelruocco%22%20AND%20a:%22testing-clocks%22)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Overview

This library contains some clock implementations that are using for testing in different scenarios. Assuming that you
are using [Clock](https://docs.oracle.com/javase/8/docs/api/java/time/Clock.html) provided by the Java platform to make
your code easier to test rather than using ```Instant.now()``` or ```new Date()```, and if you aren't then I would
recommend that you should. A nice simple explanation of why can be found
[here](https://phauer.com/2019/modern-best-practices-testing-java/#dont-use-instantnow-or-new-date).

This library contains three implementations:

### OffsetClock

Java itself offers
[this](https://docs.oracle.com/javase/8/docs/api/java/time/Clock.html#offset-java.time.Clock-java.time.Duration-)
method. Which is useful for unit testing, but in some integration testing scenarios you want a single instance of a
clock which allows you to "fast-forward" or "rewind" the current time by an offset.
    
```java
OffsetClock clock = new OffsetClock();

//fast-foward current time by 5 minutes
clock.setOffset(Duration.ofMinutes(5));
Instant instant1 = clock.instant(); // will return current time + 5 minutes

//rewind current time by 5 minutes
clock.setOffset(Duration.ofMinutes(-5));
Instant instant2 = clock.instant(); // will return current time - 5 minutes
```
    
### PreConfiguredClock

In some scenarios you may simply have code that makes a couple of calls to get the current time, and you care about the
difference in time between each call. For example, some code that measure the duration of a task by calling to get
the current time at the start, and then the current time at the end, and calculating the difference between the two.
In a scenario like this it can be useful to pre-configure the times returned by the clock, you can do that by doing
the following.

```java
Clock clock = new PreconfiguredClock(
        Instant.parse("2021-01-07T08:00:00.000Z"),
        Instant.parse("2021-01-07T08:05:00.000Z")
);

// each time clock.instant() is called the "next" preconfigured value will be returned, once all the preconfigured
// values have been returned then the clock will reset and start from the first value again
Instant instant1 = clock.instant(); // will return 2021-01-07T08:00:00.000Z
Instant instant2 = clock.instant(); // will return 2021-01-07T08:05:00.000Z
Instant instant3 = clock.instant(); // will return 2021-01-07T08:00:00.000Z
Instant instant4 = clock.instant(); // will return 2021-01-07T08:05:00.000Z
```

### OverridableClock

```OverridableClock``` is very similar to ```OffsetClock```, except as well as allowing you to set an offset that is
applied to the current time, you can also directly override the current value time to be returned.

```java
OverridableClock clock = new OverridableClock();

clock.setOverride(Instant.parse("2021-01-07T08:00:00.000Z"));
Instant instant1 = clock.instant(); //will return 2021-01-07T08:00:00.000Z
        
clock.reset();
Instant instant2 = clock.instant(); //will return current time
```

## Useful Commands

```gradle
// cleans build directories
// prints currentVersion
// formats code
// builds code
// runs tests
// checks for gradle issues
// checks dependency versions
./gradlew clean currentVersion dependencyUpdates lintGradle spotlessApply build
```