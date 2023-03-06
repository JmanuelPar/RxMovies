package com.diego.rxmovies.data.source.remote


import com.diego.rxmovies.BuildConfig
import com.diego.rxmovies.data.model.NetworkMovies
import com.diego.rxmovies.data.source.MoviesDataSource
import com.diego.rxmovies.network.TmdbApiService
import com.diego.rxmovies.utils.Constants.LANGUAGE
import io.reactivex.Observable

class MoviesRemoteDataSource internal constructor(
    private val apiService: TmdbApiService
) : MoviesDataSource {

    override fun getMovies(page: Int): Observable<NetworkMovies> {
        return apiService.getPopularMovies(
            apiKey = BuildConfig.API_KEY,
            page = page,
            language = LANGUAGE
        )
    }
}