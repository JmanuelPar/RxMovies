package com.diego.rxmovies.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diego.rxmovies.data.model.Movie
import com.diego.rxmovies.repository.MoviesRepository
import com.diego.rxmovies.utils.UIText
import com.diego.rxmovies.utils.asDomainModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

class MainActivityViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    var page = 1

    private val mDisposable = CompositeDisposable()

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _showRv = MutableLiveData<Boolean>()
    val showRv: LiveData<Boolean>
        get() = _showRv

    private val _showLayoutNoResult = MutableLiveData<Boolean>()
    val showLayoutNoResult: LiveData<Boolean>
        get() = _showLayoutNoResult

    private val _showLayoutError = MutableLiveData<Boolean>()
    val showLayoutError: LiveData<Boolean>
        get() = _showLayoutError

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean>
        get() = _showProgressBar

    private val _showErrorMessage = MutableLiveData<UIText>()
    val showErrorMessage: LiveData<UIText>
        get() = _showErrorMessage

    init {
        getMovies()
    }

    private fun getMovies() {
        showProgress()
        mDisposable.add(repository.getMovies(page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).map { networkMovies -> networkMovies.asDomainModel() }
            .subscribe({ movies ->
                if (movies.isEmpty()) showEmpty() else showSuccess(movies)
            }, { error ->
                val uiText = when (error) {
                    is IOException -> UIText.NoConnect
                    is HttpException -> error.localizedMessage?.let {
                        UIText.MessageException(it)
                    } ?: UIText.UnknownError
                    else -> UIText.UnknownError
                }

                showError(uiText)
            })
        )
    }

    private fun showProgress() {
        showRv(false)
        showLayoutNoResult(false)
        showLayoutError(false)
        showProgressBar(true)
    }

    private fun showEmpty() {
        showProgressBar(false)
        showLayoutNoResult(true)
    }

    private fun showSuccess(movies: List<Movie>) {
        showProgressBar(false)
        showRv(true)
        showMovies(movies)
    }

    private fun showError(uiText: UIText) {
        showProgressBar(false)
        showErrorMessage(uiText)
        showLayoutError(true)
    }

    private fun showProgressBar(visibility: Boolean) {
        _showProgressBar.postValue(visibility)
    }

    private fun showMovies(movies: List<Movie>) {
        _movies.postValue(movies)
    }

    private fun showRv(visibility: Boolean) {
        _showRv.postValue(visibility)
    }

    private fun showLayoutNoResult(visibility: Boolean) {
        _showLayoutNoResult.postValue(visibility)
    }

    private fun showLayoutError(visibility: Boolean) {
        _showLayoutError.postValue(visibility)
    }

    private fun showErrorMessage(uiText: UIText) {
        _showErrorMessage.postValue(uiText)
    }

    fun buttonRetryClicked() {
        getMovies()
    }

    override fun onCleared() {
        mDisposable.dispose()
        super.onCleared()
    }
}