package com.prasoon.themoviedatabasejetpackcompose.ui.movies

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.prasoon.themoviedatabasejetpackcompose.ui.theme.spacing
import com.prasoon.themoviedatabasejetpackcompose.domain.model.Movie
import com.prasoon.themoviedatabasejetpackcompose.ui.theme.TheMovieDatabaseJetpackComposeTheme
import java.math.RoundingMode

@Composable
fun MovieItem(movie: Movie, onItemClick: (Movie) -> Unit) {
    val spacing = MaterialTheme.spacing
    Box(
        modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.surface
                    ),
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(spacing.extraSmall)
            .clip(RoundedCornerShape(spacing.small))
            .shadow(elevation = 1.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(spacing.small)
                .fillMaxWidth()
                .clickable { onItemClick(movie) }
        ) {

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.fullPosterPath)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier.weight(0.4f),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 128.dp),
                        color = Color.Green
                    )
                }
            )

            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(start = spacing.medium)
            ) {
                Text(
                    text = movie.originalTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.size(spacing.medium))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 7,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.size(spacing.medium))

                Text(
                    text = "IMDB ${
                        movie.voteAverage.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(spacing.extraSmall))
                        .background(Color.Yellow)
                        .padding(
                            start = spacing.small,
                            end = spacing.small,
                            top = spacing.extraSmall,
                            bottom = spacing.extraSmall
                        )
                )
            }
        }
    }
}

val movie =
    Movie(
        id = 126889,
        originalLanguage = "en",
        originalTitle = "Alien: Covenant",
        title = "Alien: Covenant",
        overview = "The crew of the colony ship Covenant, bound for a remote planet on the far side of the galaxy, discovers what they think is an uncharted paradise but is actually a dark, dangerous world.",
        voteAverage = 6.133,
        releaseDate = "2017-05-09",
        fullPosterPath = "https://image.tmdb.org/t/p/original//zecMELPbU5YMQpC81Z8ImaaXuf9.jpg"
    )

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MovieItemPreviewDark() {
    TheMovieDatabaseJetpackComposeTheme {
        MovieItem(movie, {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MovieItemPreviewLight() {
    TheMovieDatabaseJetpackComposeTheme {
        MovieItem(movie, {})
    }
}