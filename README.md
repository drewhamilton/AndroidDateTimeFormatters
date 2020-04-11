# AndroidDateTimeFormatters
[![](https://github.com/drewhamilton/AndroidDateTimeFormatters/workflows/CI/badge.svg?branch=master)](https://github.com/drewhamilton/AndroidDateTimeFormatters/actions?query=workflow%3ACI+branch%3Amaster)

This library provides a `DateTimeFormatter` that respects Android's 12-/24-hour clock system
setting. Two versions of the library exist—one for `java.time` types and another for
[ThreeTenBP](https://github.com/ThreeTen/threetenbp) types.

With [core library desugaring](https://developer.android.com/studio/preview/features#j8-desugar)
supported in Android Gradle Plugin 4.0+, the "datetimeformatters" artifact, using `java.time` types,
is considered the primary and preferred artifact. However, "datetimeformatters-threetenbp" can be
used in apps that still use ThreeTenBP. Both artifacts require a minimum Android SDK version of at
least 15.

To enable core library desugaring, include the following in your app module's build.gradle file:
```groovy
android {
    defaultConfig {
        // Only required if your minSdk is less than 21:
        multidexEnabled true
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
        coreLibraryDesugaringEnabled true
    }
}
dependencies {
    // Latest version is 1.0.5 at the time of writing this:
    coreLibraryDesugaring "com.android.tools:desugar_jdk_libs:$coreLibraryDesugaringVersion"
}
```

## Download
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.drewhamilton.androidtime/datetimeformatters/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.drewhamilton.androidtime/datetimeformatters)

AndroidDateTimeFormatters is available on Maven Central.

```groovy
// java.time formatters:
implementation "dev.drewhamilton.androidtime:datetimeformatters:$version"

// ThreeTenBP formatters:
implementation "dev.drewhamilton.androidtime:datetimeformatters-threetenbp:$version"
```

## License
```
Copyright 2019 Drew Hamilton

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
