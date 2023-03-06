package com.diego.rxmovies.repository

import com.diego.rxmovies.data.source.MoviesDataSource

class DefaultMoviesRepository(
    private val moviesRemoteDataSource: MoviesDataSource
) : MoviesRepository {

    override fun getMovies(page: Int) = moviesRemoteDataSource.getMovies(page)
}