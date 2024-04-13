package com.challenge.movieappchallenge.presentaion.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.challenge.movieappchallenge.domain.models.Movie
import com.challenge.movieappchallenge.domain.useCase.MovieUseCase
import com.challenge.movieappchallenge.presentaion.models.MoviesType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {

    private val _moviesFlow = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val moviesList: StateFlow<PagingData<Movie>> = _moviesFlow

    val moviesType: Channel<MoviesType> = Channel(capacity = Channel.UNLIMITED)
    val refreshData: Channel<Boolean> = Channel(capacity = Channel.UNLIMITED)


    init {
        viewModelScope.launch {
            moviesType.consumeAsFlow()
                .distinctUntilChanged()
                .collectLatest { movieType ->
                    getMovies(movieType)
                }

        }
        viewModelScope.launch {
            refreshData.consumeAsFlow()
                .collectLatest { mRefreshData ->
                    if (mRefreshData) {
                        _moviesFlow.value = PagingData.empty()
                        getMovies(MoviesType.POPULAR)
                        refreshData.trySend(false)
                    }
                }
        }
    }

    private suspend fun getMovies(movieType: MoviesType) {
        // Get the Movies if there are no movies in the flow
        viewModelScope.launch {
            val moviesResult = when (movieType) {
                MoviesType.TOP_RATED -> movieUseCase.getTopRatedMovies()
                MoviesType.POPULAR -> movieUseCase.getMostPopularMovies()
            }.cachedIn(viewModelScope)

            _moviesFlow.emit(moviesResult.first())
        }
    }
}