package com.tmdb.widget.buttons

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import com.tmdb.theme.tmdbColors

@Composable
fun PlayerButton(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    enabled: Boolean = true,
    size: Dp = 38.dp,
    focusedSize: Dp = 46.dp,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onItemClick: () -> Unit,
    selected: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val focused by interactionSource.collectIsFocusedAsState()

    val animatedSize by animateDpAsState(
        targetValue = if (focused || pressed) focusedSize else size,
        label = ""
    )

    Box(
        modifier = modifier.size(focusedSize),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .focusRequester(focusRequester)
                .focusable(enabled = enabled, interactionSource = interactionSource)
                .size(animatedSize)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled
                ) {
                    onItemClick()
                    focusRequester.requestFocus()
                }
        ) {
            Icon(
                painter = painterResource(iconId),
                tint = when {
                    selected -> MaterialTheme.tmdbColors.primary
                    focused -> MaterialTheme.tmdbColors.accent
                    else -> MaterialTheme.tmdbColors.accent.copy(alpha = 0.5f)
                },
                contentDescription = null,
            )
        }
    }
}