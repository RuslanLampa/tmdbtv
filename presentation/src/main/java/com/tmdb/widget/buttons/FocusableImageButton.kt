package com.tmdb.widget.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import com.tmdb.common.R
import com.tmdb.theme.tmdbColors

@Composable
fun FocusableImageButton(
    iconResId: Int,
    selectedIconResId: Int,
    focusRequester: FocusRequester,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value

    val backgroundColor = when {
        isFocused -> MaterialTheme.tmdbColors.secondary
        isSelected -> MaterialTheme.tmdbColors.primary
        else -> MaterialTheme.tmdbColors.primary
    }

    val iconColor = when {
        isFocused -> MaterialTheme.tmdbColors.onSecondary
        isSelected -> MaterialTheme.tmdbColors.onPrimary
        else -> MaterialTheme.tmdbColors.accent
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color = backgroundColor, shape = CircleShape)
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource)
            .clickable(onClick = { 
                focusRequester.requestFocus()
                onClick()
            }),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(
                id = if (isSelected)
                    selectedIconResId else iconResId
            ),
            contentDescription = "Icon Button",
            tint = iconColor,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Preview
@Composable
fun PreviewDefaultImageButton() {
    FocusableImageButton(
        iconResId = R.drawable.ic_star,
        selectedIconResId = R.drawable.ic_star_filled,
        isSelected = false,
        focusRequester = remember { FocusRequester() },
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewSelectedImageButton() {
    FocusableImageButton(
        iconResId = R.drawable.ic_star,
        selectedIconResId = R.drawable.ic_star_filled,
        isSelected = true,
        focusRequester = remember { FocusRequester() },
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewFocusedImageButton() {
    FocusableImageButton(
        iconResId = R.drawable.ic_star,
        selectedIconResId = R.drawable.ic_star_filled,
        focusRequester = remember { FocusRequester() },
        onClick = {}
    )
}
