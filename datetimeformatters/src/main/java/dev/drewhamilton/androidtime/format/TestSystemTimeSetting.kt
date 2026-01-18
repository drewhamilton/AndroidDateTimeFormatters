package dev.drewhamilton.androidtime.format

internal var testSystemTimeSetting: TestSystemTimeSetting = TestSystemTimeSetting.Unset

internal sealed interface TestSystemTimeSetting {
    data object Unset : TestSystemTimeSetting

    data class Set(
        val value: String?,
    ) : TestSystemTimeSetting
}
