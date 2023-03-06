package com.diego.rxmovies.data.source

import com.diego.rxmovies.data.model.NetworkMovies
import io.reactivex.Observable

interface MoviesDataSource {

    fun getMovies(page: Int): Observable<NetworkMovies>
}