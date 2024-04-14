package com.challenge.movieappchallenge.presentaion.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.challenge.movieappchallenge.R
import com.challenge.movieappchallenge.domain.models.Movie

@Composable
@Preview(showBackground = true)
fun MovieDetailsScreen(movie: Movie = Movie(id = -1, name = "Dummy", voteAverage = 7.5)) {
    var isFavorite by remember { mutableStateOf(false) }
    Column(
        Modifier
            .background(MaterialTheme.colors.background)
            .testTag("movie_details"),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val scrollState = rememberScrollState()
        val transition =
            updateTransition(scrollState.value != 0, label = "")
        val size by transition.animateDp(label = "") { isScrolling ->
            if (isScrolling) 300.dp else 500.dp
        }
        Box(
            Modifier
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
                .height(size)
        ) {
            LoadImageAsync(
                modifier = Modifier
                    .fillMaxWidth(),
                imageUrl = movie.imageUrlFull
            )
            IconButton(
                onClick = {

                },
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = if (isFavorite) R.drawable.remove_favourite else R.drawable.add_favourite),
                    contentDescription = stringResource(id = R.string.favorite_button)
                )
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                KeyValueItem(stringResource(id = R.string.title), movie.name)
                KeyValueItem(
                    stringResource(id = R.string.overview),
                    movie.description
                )
            }
        }

    }
}

@Composable
fun KeyValueItem(key: String, name: String, maxLines: Int = Int.MAX_VALUE) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = key,
            fontSize = 20.sp,
            color = MaterialTheme.colors.onPrimary,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .padding(4.dp),
            text = name,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onPrimary,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}
