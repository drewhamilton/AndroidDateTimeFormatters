# Changelog

## 2.1.0
_2020-04-18_

Add additional factory methods for time, date, and date-time formatters. Time and date-time
formatters created with the "short" time style will use the system 12-/24-hour time setting to
format times.

In the rare case that a short time pattern respecting the system 12-/24-hour time
setting can not be found, a fallback based on the Context's locale will be used instead.

## 2.0.1
_2020-04-13_

Lower the minSdk for the `java.time` artifact from 15 to 4.

## 2.0.0
_2020-04-11_

Support core library desugaring for the `java.time` artifact, allowing its use on Android 15+. Add
nullability annotations to all public methods.

Change group to `dev.drewhamilton.androidtime`, and change package names similarly. Rename artifacts
to "datetimeformatters" and "datetimeformatters-threetenbp". Publish to Maven Central instead of
JCenter.

## 1.0.0
_2019-07-22_

Initial release. Get a time format based on the given Context's 12/24-hour time setting.

Use the `javatime` artifact for Android API 26+, or the `threetenbp` artifact for Android API 15+.
