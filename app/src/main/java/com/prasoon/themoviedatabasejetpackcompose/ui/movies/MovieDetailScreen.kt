package com.prasoon.themoviedatabasejetpackcompose.ui.movies

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.prasoon.themoviedatabasejetpackcompose.common.DateUtils.getMonthYearFromDate
import com.prasoon.themoviedatabasejetpackcompose.ui.theme.spacing
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.PopularMoviesViewModel
import com.prasoon.themoviedatabasejetpackcompose.ui.viewmodel.SearchViewModel
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Int?,
    source: String,
    popularMoviesViewModel: PopularMoviesViewModel,
    searchViewModel: SearchViewModel,
    onBackClick: () -> Unit
) {
    val TAG = "MovieDetailScreen"
    val spacing = MaterialTheme.spacing
    Log.i(TAG, "movieId: $movieId, source: $source")

    val movie = when (source) {
        "home" -> {
            popularMoviesViewModel.getMovieById(movieId)
        }

        "search" -> {
            searchViewModel.getMovieById(movieId)
        }

        else -> null
    }
    Log.i(TAG, "movie found: $movie")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (movie != null) {
                        Text(text = movie.title)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(5.dp)
        ) {
            if (movie != null) {
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

                    Column(
                        modifier = Modifier
                            .padding(spacing.small)
                            .fillMaxWidth()
                    ) {

                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(movie.fullPosterPath)
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
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
                                .padding(start = spacing.medium)
                        ) {

                            Text(
                                text = movie.originalTitle,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(text = getMonthYearFromDate(movie.releaseDate))
                            Spacer(modifier = Modifier.height(8.dp))
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
                                    movie.voteAverage.toBigDecimal().setScale(1, RoundingMode.UP)
                                        .toDouble()
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
        }
    }
}