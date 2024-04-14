package com.challenge.movieappchallenge.presentaion.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.challenge.movieappchallenge.R
import com.challenge.movieappchallenge.domain.models.Movie
import com.challenge.movieappchallenge.presentaion.models.MoviesType
import com.challenge.movieappchallenge.presentaion.models.SortingValue
import com.challenge.movieappchallenge.presentaion.util.compose.items
import com.challenge.movieappchallenge.presentaion.util.roundFirstDigitString
import com.challenge.movieappchallenge.presentaion.viewModel.MoviesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoviesScreen(
    moviesViewModel: MoviesViewModel,
    onMovieClicked: (Movie) -> Unit = {},
) {
    var movieType by rememberSaveable { mutableStateOf(MoviesType.POPULAR) }
    moviesViewModel.moviesType.trySend(movieType)

    var searchQuery by rememberSaveable { mutableStateOf("") }
    moviesViewModel.searchQuery.trySend(searchQuery)

    var sortingValue by rememberSaveable { mutableStateOf(SortingValue.ALPHABETICAL) }
    moviesViewModel.sortingValue.trySend(sortingValue)

    Scaffold(
        modifier = Modifier
            .testTag("movies_screen")
            .semantics {
                testTagsAsResourceId = true
            }
            .background(MaterialTheme.colors.background),
        topBar = {
            TopBar(
                onFilterItemClicked = {
                    movieType = it
                },
                onSortingItemCLicked = {
                    sortingValue = it
                }
            )
        },
        content = {
            it.toString()
            Column {
                SearchInput(onSearchClicked = { searchQuery = it })
                MoviesContent(moviesViewModel.moviesList, onMovieClicked, onRefresh = {
                    moviesViewModel.refreshData.trySend(true)
                })
            }

        })
}

@Composable
fun MoviesContent(
    moviesFlow: Flow<PagingData<Movie>>,
    onMovieClicked: (Movie) -> Unit,
    onRefresh: () -> Unit
) {
    val moviesItems: LazyPagingItems<Movie> = moviesFlow.collectAsLazyPagingItems()
    val isRefreshing = moviesItems.loadState.refresh is LoadState.Loading

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            onRefresh()
        },
    ) {
        MoviesUiStates(moviesItems)
        Box(modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                MoviesList(moviesFlow, onMovieClicked)
            }
        }
    }
}

@Composable
fun MoviesUiStates(moviesItems: LazyPagingItems<Movie>) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .height(maxHeight)
                .align(Alignment.Center)
        ) {
            with(moviesItems.loadState.refresh) {
                ShowLoadingState(this)
                ShowErrorState(this)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBar(
    onFilterItemClicked: (MoviesType) -> Unit = {},
    onSortingItemCLicked: (SortingValue) -> Unit = {}
) {
    TopAppBar(modifier = Modifier.semantics { contentDescription = "Movies_app_bar" }, title = {
        Title()
    }, actions = {
        AppBarActions(onFilterItemClicked)
        AppBarSortingAction(onSortingItemCLicked)
    })

}

@Composable
fun AppBarActions(onFilterItemClicked: (MoviesType) -> Unit) {
    var mDisplayMenu by remember { mutableStateOf(false) }
    DotsIcon(onFilterClicked = {
        mDisplayMenu = !mDisplayMenu
    })
    // Creating a dropdown menu
    DropdownMenu(
        modifier = Modifier.semantics { contentDescription = "popup_menu" },
        expanded = mDisplayMenu,
        onDismissRequest = { mDisplayMenu = false }
    ) {

        DropdownMenuItem(onClick = {
            onFilterItemClicked(MoviesType.TOP_RATED)
            mDisplayMenu = false
        }) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = Icons.Filled.Star, contentDescription = null)
                Text(text = stringResource(id = R.string.top_rated))
            }
        }

        DropdownMenuItem(onClick = {
            onFilterItemClicked(MoviesType.POPULAR)
            mDisplayMenu = false
        }) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = null)
                Text(text = stringResource(id = R.string.popular))
            }
        }
    }
}


@Composable
fun AppBarSortingAction(onSortingItemCLicked: (SortingValue) -> Unit) {
    var mSortingState by remember { mutableStateOf(false) }
    SortIcon(onFilterClicked = {
        mSortingState = !mSortingState
    })
    // Creating a dropdown menu
    DropdownMenu(
        modifier = Modifier.semantics { contentDescription = "popup_menu" },
        expanded = mSortingState,
        onDismissRequest = { mSortingState = false }
    ) {

        DropdownMenuItem(onClick = {
            onSortingItemCLicked(SortingValue.ALPHABETICAL)
            mSortingState = false
        }) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                Text(text = stringResource(id = R.string.sorting_alphabetical))
            }
        }

        DropdownMenuItem(onClick = {
            onSortingItemCLicked(SortingValue.DATE)
            mSortingState = false
        }) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = null)
                Text(text = stringResource(id = R.string.sorting_date))
            }
        }
    }
}

@Composable
fun Title() {
    Text(
        text = stringResource(id = R.string.app_name),
        color = Color.White,
        fontSize = 22.sp
    )
}

@Composable
fun MoviesList(moviesFlow: Flow<PagingData<Movie>>, onMovieClicked: (Movie) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp, bottom = 4.dp)
    ) {

        val moviesItems = moviesFlow.collectAsLazyPagingItems()

        LazyVerticalGrid(
            modifier = Modifier.testTag("myLazyVerticalGrid"),
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(
                start = 8.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 32.dp
            ),
        ) {
            // Movies items
            items(moviesItems) { movie ->
                MovieItem(movie, onMovieClicked)
            }
            item {
                ShowLoadingProgress(moviesItems.loadState.refresh)
            }

        }
    }
}

@Composable
fun ShowLoadingProgress(loadState: LoadState) {
    if (loadState is LoadState.Loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.size(35.dp),
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun ShowErrorState(loadState: LoadState) {
    if (loadState is LoadState.Error) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = painterResource(id = R.drawable.ic_cloud_server_antenna),
                    contentDescription = null
                )
                Text(
                    text = stringResource(id = R.string.no_movies),
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }
}

@Composable
fun ShowLoadingState(loadState: LoadState) {
    if (loadState is LoadState.Loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun MovieItem(movie: Movie?, onMovieClicked: (Movie) -> Unit) {
    movie?.let {
        Column(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .padding(bottom = 30.dp)
                .clickable {
                    onMovieClicked(movie)
                },
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MovieImage(movie.imageUrlFull)
            Row {
                VoteAverage(review = movie.voteAverage, icon = R.drawable.star)
                Spacer(modifier = Modifier.width(10.dp))
                VoteCount(count = movie.voteCount, icon = R.drawable.user)
            }
            Text(
                text = movie.name,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun MovieImage(imageUrl: String) {
    LoadImageAsync(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(320.dp)
            .clip(RoundedCornerShape(2)),
        imageUrl
    )
}

@Composable
fun LoadImageAsync(
    modifier: Modifier,
    imageUrl: String?,
    @DrawableRes placeholder: Int = R.drawable.placeholder_image,
    @DrawableRes onError: Int = R.drawable.error_image,
    cacheEnabled: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .also {
                if (cacheEnabled) {
                    it.diskCachePolicy(CachePolicy.ENABLED)
                }
            }
            .build(),
        placeholder = painterResource(placeholder),
        contentDescription = "Image",
        contentScale = contentScale,
        error = painterResource(onError),
        modifier = modifier
    )
}

@Composable
fun DotsIcon(
    onFilterClicked: () -> Unit,
    size: Dp = 50.dp
) {
    Box(
        modifier = Modifier
            .semantics { contentDescription = "filter_icon" }
            .size(size)
            .clip(RoundedCornerShape(50))
            .padding(4.dp)
            .clickable {
                onFilterClicked()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.filter),
            tint = MaterialTheme.colors.secondary,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SortIcon(
    onFilterClicked: () -> Unit,
    size: Dp = 50.dp
) {
    Box(
        modifier = Modifier
            .semantics { contentDescription = "sort_icon" }
            .size(size)
            .clip(RoundedCornerShape(50))
            .padding(4.dp)
            .clickable {
                onFilterClicked()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.sorting),
            tint = MaterialTheme.colors.secondary,
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = MaterialTheme.colors.primary
    )
}

@Composable
fun VoteAverage(review: Double, icon: Int?) {
    Row {
        Image(
            painter = painterResource(id = icon ?: R.drawable.star),
            contentDescription = null,
            modifier = Modifier
                .width(24.dp)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(text = review.roundFirstDigitString())
    }
}

@Composable
fun VoteCount(count: Int, icon: Int?) {
    Row {
        Image(
            painter = painterResource(id = icon ?: R.drawable.user),
            contentDescription = null, // Provide appropriate content description
            modifier = Modifier
                .width(24.dp)
                .padding(4.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary) // Specify the tint color
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(text = count.toString())
    }
}

@Composable
fun SearchInput(onSearchClicked: (String) -> Unit) {
    val searchTextState = remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchTextState.value,
            onValueChange = { searchTextState.value = it },
            label = { Text("Search") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        onSearchClicked(searchTextState.value)
    }
}
