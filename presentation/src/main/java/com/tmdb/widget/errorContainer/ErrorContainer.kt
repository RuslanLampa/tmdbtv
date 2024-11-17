package com.tmdb.widget.errorContainer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.tmdb.common.R
import com.tmdb.widget.buttons.FocusableButton
import com.tmdb.theme.tmdbColors

@Composable
fun ErrorContainer(
    title: String = stringResource(R.string.error_title),
    message: String = stringResource(R.string.error_message),
    buttonText: String = stringResource(R.string.retry),
    onClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        Modifier
            .focusRequester(focusRequester)
            .fillMaxSize()
            .background(MaterialTheme.tmdbColors.background),
        contentAlignment = Alignment.Center
    ) {
        Box(
            Modifier
                .padding(16.dp)
                .background(MaterialTheme.tmdbColors.primary, shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.tmdbColors.onPrimary
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.tmdbColors.onPrimary,
                    textAlign = TextAlign.Center
                )

                FocusableButton(
                    text = buttonText,
                    focusRequester = FocusRequester(),
                    onClick = onClick
                )
            }
        }
    }
}

@Preview(device = Devices.TV_1080p)
@Composable
private fun PreviewErrorContainer() {
    ErrorContainer(
        title = "Error",
        message = "Something went wrong. Please try again.",
        onClick = {}
    )
}