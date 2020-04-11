# Changelog

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
