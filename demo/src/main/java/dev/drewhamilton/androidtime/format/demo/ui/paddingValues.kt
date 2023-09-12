package dev.drewhamilton.androidtime.format.demo.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Returns a [PaddingValues] that adds the receiver's values to [other]'s values.
 *
 * For example, if the receiver calculates a top padding of 10 and [other] calculates a top padding
 * of 5, the returned instance calculates a top padding of 15.
 */
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = AddedPaddingValues(
    first = this,
    second = other,
)

/**
 * A [PaddingValues] implementation that lazily adds the calculated values of [first] and [second].
 */
private class AddedPaddingValues(
    private val first: PaddingValues,
    private val second: PaddingValues,
): PaddingValues {
    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return first.calculateLeftPadding(layoutDirection) +
            second.calculateLeftPadding(layoutDirection)
    }

    override fun calculateTopPadding(): Dp {
        return first.calculateTopPadding() +
            second.calculateTopPadding()
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return first.calculateRightPadding(layoutDirection) +
            second.calculateRightPadding(layoutDirection)
    }

    override fun calculateBottomPadding(): Dp {
        return first.calculateBottomPadding() +
            second.calculateBottomPadding()
    }
}
