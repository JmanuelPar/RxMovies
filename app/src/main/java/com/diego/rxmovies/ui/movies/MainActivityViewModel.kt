package com.diego.rxmovies.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.diego.rxmovies.data.model.Movie
import com.diego.rxmovies.repository.MoviesRepository
import io.reactivex.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi

class MainActivityViewModel(private val repository: MoviesRepository) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun movies(): Flowable<PagingData<Movie>> {
        return repository.getMovies().cachedIn(viewModelScope)
    }
}