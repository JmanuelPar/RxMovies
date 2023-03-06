package com.diego.rxmovies.repository

import com.diego.rxmovies.data.model.NetworkMovies
import io.reactivex.Observable

interface MoviesRepository {

    fun getMovies(page: Int): Observable<NetworkMovies>
}