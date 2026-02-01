# AndroidDateTimeFormatters
[![](https://github.com/drewhamilton/AndroidDateTimeFormatters/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/drewhamilton/AndroidDateTimeFormatters/actions/workflows/ci.yml?query=branch%3Amain)

This library provides `java.time.DateTimeFormatter`s that make use of Android-specific features.
Localized time formatters respect Android's 12-/24-hour clock system setting. And "skeleton"
formatters take a format string skeleton and localize it based on the given `Context`.

This library has a `minSdk` of 26, because `java.time` is not desugared on Android API 26+. On lower
`minSdk`s, `java.time` is desugared, and takes this system setting into account for short-format
times by default.

## Download
[![Maven Central](https://img.shields.io/maven-metadata/v.svg?label=maven%20central&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fdev%2Fdrewhamilton%2Fandroidtime%2Fdatetimeformatters%2Fmaven-metadata.xml&color=blue)](https://central.sonatype.com/namespace/dev.drewhamilton.androidtime)

AndroidDateTimeFormatters is available on Maven Central.

```groovy
implementation "dev.drewhamilton.androidtime:datetimeformatters:$version"
```

## Legacy versions

For a lower `minSdk`, or for use with [ThreeTenBP](https://github.com/ThreeTen/threetenbp), use
`AndroidDateTimeFormatters` version 2.2.0.

```groovy
// java.time formatters:
implementation "dev.drewhamilton.androidtime:datetimeformatters:2.2.0"

// ThreeTenBP formatters:
implementation "dev.drewhamilton.androidtime:datetimeformatters-threetenbp:2.2.0"
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
