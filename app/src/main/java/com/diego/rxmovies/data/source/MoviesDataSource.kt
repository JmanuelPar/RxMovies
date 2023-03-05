package com.diego.rxmovies.data.source

import androidx.paging.PagingData
import com.diego.rxmovies.data.model.Movie
import io.reactivex.Flowable

interface MoviesDataSource {

    fun getMovies(): Flowable<PagingData<Movie>>
}