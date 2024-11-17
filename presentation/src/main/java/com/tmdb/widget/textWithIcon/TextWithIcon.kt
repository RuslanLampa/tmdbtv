package com.tmdb.widget.textWithIcon

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.tmdb.common.R
import com.tmdb.theme.tmdbColors

@Composable
fun TextWithIcon(
    text: String,
    iconResId: Int,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier.size(12.dp),
    contentDescription: String? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    spacing: Dp = 2.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = iconModifier,
            tint = MaterialTheme.tmdbColors.secondary
        )
        Spacer(modifier = Modifier.width(spacing))
        Text(
            text = text,
            style = textStyle,
            color = MaterialTheme.tmdbColors.text
        )
    }
}

@Preview
@Composable
fun PreviewTextWithIcon() {
    TextWithIcon(text = "Sample text", iconResId = R.drawable.ic_star)
}