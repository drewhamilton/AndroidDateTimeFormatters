# AndroidDateTimeFormatters
[![](https://github.com/drewhamilton/AndroidDateTimeFormatters/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/drewhamilton/AndroidDateTimeFormatters/actions/workflows/ci.yml?query=branch%3Amain)

This library provides `DateTimeFormatter`s that make use of Android-specific features. Localized
time formatters respect Android's 12-/24-hour clock system setting. And "skeleton" formatters take a
format string skeleton and localize it based on the given Context.

Two versions of the library exist: one for `java.time` types and another for
[ThreeTenBP](https://github.com/ThreeTen/threetenbp) types.

With [core library
desugaring](https://developer.android.com/studio/write/java8-support#library-desugaring) supported
in Android Gradle Plugin 4+, the "datetimeformatters" artifact, using `java.time` types, is
considered the primary and preferred artifact and can be used down to Android SDK version 4.
However, "datetimeformatters-threetenbp" can be used in apps that still use ThreeTenBP, down to
Android SDK version 15.

## Download
[![Maven Central](https://img.shields.io/maven-metadata/v.svg?label=maven%20central&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fdev%2Fdrewhamilton%2Fandroidtime%2Fdatetimeformatters%2Fmaven-metadata.xml&color=blue)](https://central.sonatype.com/namespace/dev.drewhamilton.androidtime)

AndroidDateTimeFormatters is available on Maven Central.

```groovy
// java.time formatters:
implementation "dev.drewhamilton.androidtime:datetimeformatters:$version"

// ThreeTenBP formatters:
implementation "dev.drewhamilton.androidtime:datetimeformatters-threetenbp:$version"
```

## License
```
Copyright 2019–2026 Drew Hamilton

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
