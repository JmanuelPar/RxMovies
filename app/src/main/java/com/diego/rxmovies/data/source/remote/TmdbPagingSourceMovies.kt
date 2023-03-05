package com.diego.rxmovies.data.source.remote

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.diego.rxmovies.BuildConfig
import com.diego.rxmovies.data.model.Movie
import com.diego.rxmovies.data.model.NetworkMovies
import com.diego.rxmovies.network.TmdbApiService
import com.diego.rxmovies.utils.Constants.LANGUAGE
import com.diego.rxmovies.utils.Constants.NETWORK_TMDB_PAGE_SIZE
import com.diego.rxmovies.utils.asDomainModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

private const val TMDB_STARTING_PAGE_INDEX = 1

class TmdbPagingSourceMovies(
    private val apiService: TmdbApiService
) : RxPagingSource<Int, Movie>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Movie>> {
        val page = params.key ?: TMDB_STARTING_PAGE_INDEX

        return apiService.getPopularMovies(
            apiKey = BuildConfig.API_KEY,
            page = page,
            language = LANGUAGE
        )
            .subscribeOn(Schedulers.io())
            .map { networkMovies -> toLoadResult(networkMovies, page, params) }
            .onErrorReturn { LoadResult.Error(it) }
    }

    private fun toLoadResult(
        networkMovies: NetworkMovies,
        page: Int,
        params: LoadParams<Int>
    ): LoadResult<Int, Movie> {
        val tmdbTotalPage =
            networkMovies.totalPages ?: (page + (params.loadSize / NETWORK_TMDB_PAGE_SIZE))

        val prevKey = when (page) {
            TMDB_STARTING_PAGE_INDEX -> null
            else -> page - 1
        }

        val nextKey = when {
            page == TMDB_STARTING_PAGE_INDEX && tmdbTotalPage == 1 -> null
            page < tmdbTotalPage -> page + 1
            else -> null
        }

        return LoadResult.Page(
            data = networkMovies.asDomainModel(),
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}