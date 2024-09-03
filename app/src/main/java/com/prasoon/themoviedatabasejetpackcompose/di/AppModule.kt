package com.prasoon.themoviedatabasejetpackcompose.di

import com.prasoon.themoviedatabasejetpackcompose.domain.repository.MoviesRepository
import com.prasoon.themoviedatabasejetpackcompose.data.repository.MoviesRepositoryImpl
import com.prasoon.themoviedatabasejetpackcompose.data.remote.TmdbHttpClient
import com.prasoon.themoviedatabasejetpackcompose.domain.use_case.GetPopularMoviesUseCase
import com.prasoon.themoviedatabasejetpackcompose.domain.use_case.SearchMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(httpClient: TmdbHttpClient): HttpClient = httpClient.getHttpClient()

    @Provides
    @Singleton
    fun provideMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository = impl


    @Provides
    @Singleton
    fun provideGetPopularMoviesUseCase(repository: MoviesRepository) =
        GetPopularMoviesUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchMoviesUseCase(repository: MoviesRepository) =
        SearchMoviesUseCase(repository)
}