package com.diego.rxmovies.utils

import com.diego.rxmovies.data.source.remote.MoviesRemoteDataSource
import com.diego.rxmovies.network.TmdbApi
import com.diego.rxmovies.repository.DefaultMoviesRepository
import com.diego.rxmovies.repository.MoviesRepository

object ServiceLocator {

    @Volatile
    var moviesRepository: MoviesRepository? = null

    fun provideMoviesRepository(): MoviesRepository {
        synchronized(this) {
            return moviesRepository ?: createMoviesRepository()
        }
    }

    private fun createMoviesRepository(): MoviesRepository {
        val newRepo = DefaultMoviesRepository(
            moviesRemoteDataSource = createMoviesRemoteDataSource()
        )
        moviesRepository = newRepo
        return newRepo
    }

    private fun createMoviesRemoteDataSource() = MoviesRemoteDataSource(provideApiService())

    private fun provideApiService() = TmdbApi.retrofitService
}