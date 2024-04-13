package com.challenge.movieappchallenge.presentaion.di

import com.challenge.movieappchallenge.data.local.MoviesDao
import com.challenge.movieappchallenge.data.paging.LocalMoviesPagingSource
import com.challenge.movieappchallenge.data.paging.RemoteMoviesPagingSource
import com.challenge.movieappchallenge.data.remote.RetrofitApi
import com.challenge.movieappchallenge.data.repository.MoviesRepositoryImpl
import com.challenge.movieappchallenge.domain.repo.MoviesRepository
import com.challenge.movieappchallenge.domain.useCase.MovieUseCase
import com.challenge.movieappchallenge.domain.useCase.MoviesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
abstract class MoviesModule {

    /**
     * Provide an instance of MoviesRepositoryImpl
     */
    @Binds
    abstract fun bindMoviesRepo(moviesRepositoryImpl: MoviesRepositoryImpl): MoviesRepository

    companion object {
        @Provides
        fun providesMoviesRepo(
            localPagingSource: LocalMoviesPagingSource,
            remotePagingSource: RemoteMoviesPagingSource
        ): MoviesRepositoryImpl {
            return MoviesRepositoryImpl(localPagingSource, remotePagingSource)
        }
    }


    /**
     * Provide an instance of MoviesUseCaseImpl
     */
    @Binds
    abstract fun bindMoviesUseCase(moviesUseCaseImpl: MoviesUseCaseImpl): MovieUseCase
}

@Module
@InstallIn(ViewModelComponent::class)
class MoviesImpl {

    @Provides
    @ViewModelScoped
    fun bindMoviesPagingLocal(
        moviesDao: MoviesDao,
    ): LocalMoviesPagingSource {
        return LocalMoviesPagingSource(moviesDao)
    }

    @Provides
    @ViewModelScoped
    fun bindMoviesPagingRemote(
        moviesDao: MoviesDao,
        retrofitApi: RetrofitApi
    ): RemoteMoviesPagingSource {
        return RemoteMoviesPagingSource(retrofitApi, moviesDao)
    }

}
