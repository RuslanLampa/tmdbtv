package com.tmdb.widget.movieCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.tmdb.theme.tmdbColors

@Composable
fun MovieCard(
    imageUrl: String,
    onClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(CardDefaults.VerticalImageAspectRatio),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.tmdbColors.accent
                ),
                shape = RoundedCornerShape(6),
            ),
        ),
        scale = CardDefaults.scale(
            focusedScale = 1.05f,
        )
    ) {
        Image(
            painter = painter,
            contentDescription = null
        )
    }
}

@Preview(device = Devices.TV_1080p)
@Composable
private fun Preview() {
    MovieCard(
        imageUrl = "",
        onClick = {}
    )
}