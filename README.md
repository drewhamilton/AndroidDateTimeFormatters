# AndroidDateTimeFormatters

This library provides a `DateTimeFormatter` that respects Android's 12-/24-hour clock system setting. Separate versions
of the library exist depending on whether you are using java.time types or
[ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP).

This library was created to address ThreeTenABP issue [#16](https://github.com/JakeWharton/ThreeTenABP/issues/16) after
Jake indicated he
[didn't want to add this util](https://github.com/JakeWharton/ThreeTenABP/pull/103#issuecomment-498429319)
directly to ThreeTenABP.

## Download
[ ![Download](https://api.bintray.com/packages/drewhamilton/AndroidDateTimeFormatters/AndroidDateTimeFormatters-JavaTime/images/download.svg) ](https://bintray.com/drewhamilton/RxPreferences)

AndroidDateTimeFormatters is available on JCenter.

```groovy
// java.time formatters:
implementation "drewhamilton.androiddatetimeformatters:androiddatetimeformatters-javatime:$version"

// ThreeTenBP formatters:
implementation "drewhamilton.androiddatetimeformatters:androiddatetimeformatters-threetenbp:$version"
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
