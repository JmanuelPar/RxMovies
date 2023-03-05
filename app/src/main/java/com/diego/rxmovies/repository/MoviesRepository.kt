package com.diego.rxmovies.repository

import androidx.paging.PagingData
import com.diego.rxmovies.data.model.Movie
import io.reactivex.Flowable

interface MoviesRepository {

    fun getMovies(): Flowable<PagingData<Movie>>
}