package com.tmdb.widget.buttons

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.tmdb.theme.tmdbColors

@Composable
fun FocusableButton(
    text: String,
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

    val textColor = when {
        isFocused -> MaterialTheme.tmdbColors.onSecondary
        isSelected -> MaterialTheme.tmdbColors.onPrimary
        else -> MaterialTheme.tmdbColors.accent
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.colors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource)
            .padding(8.dp),
        interactionSource = interactionSource
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
fun PreviewDefaultButton() {
    FocusableButton(
        text = "Default",
        isSelected = false,
        focusRequester = remember { FocusRequester() },
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewSelectedButton() {
    FocusableButton(
        text = "Selected",
        isSelected = true,
        focusRequester = remember { FocusRequester() },
        onClick = {}
    )
}

@Preview
@Composable
fun PreviewFocusedButton() {
    FocusableButton(
        text = "Focused",
        focusRequester = remember { FocusRequester() },
        onClick = {}
    )
}