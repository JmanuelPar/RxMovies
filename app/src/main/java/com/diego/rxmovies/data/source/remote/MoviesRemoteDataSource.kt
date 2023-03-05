package com.diego.rxmovies.data.source.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.rxjava2.flowable
import com.diego.rxmovies.data.source.MoviesDataSource
import com.diego.rxmovies.network.TmdbApiService
import com.diego.rxmovies.utils.Constants.NETWORK_TMDB_PAGE_SIZE

class MoviesRemoteDataSource internal constructor(
    private val apiService: TmdbApiService
) : MoviesDataSource {

    override fun getMovies() =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_TMDB_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TmdbPagingSourceMovies(apiService)
            }
        ).flowable
}