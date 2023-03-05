package com.diego.rxmovies.ui.movies

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.diego.rxmovies.MyApplication
import com.diego.rxmovies.R
import com.diego.rxmovies.adapter.MovieAdapter
import com.diego.rxmovies.adapter.MovieLoadStateAdapter
import com.diego.rxmovies.databinding.ActivityMainBinding
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val mMainActivityViewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(
            (applicationContext as MyApplication).moviesRepository
        )
    }

    private val mDisposable = CompositeDisposable()
    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        showMovies()
        showUI()
    }

    override fun onStop() {
        super.onStop()
        mDisposable.clear()
    }

    private fun initView() {
        binding.toolbar.title = getString(R.string.popular_movies)
        setSupportActionBar(binding.toolbar)
        val mRvMovies = binding.rvMovies
        movieAdapter = MovieAdapter()
        val headerAdapter = MovieLoadStateAdapter { movieAdapter.retry() }
        val footerAdapter = MovieLoadStateAdapter { movieAdapter.retry() }

        mRvMovies.apply {
            layoutManager = GridLayoutManager(mRvMovies.context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when {
                            position == 0 && headerAdapter.itemCount > 0 -> 2
                            position == movieAdapter.itemCount && footerAdapter.itemCount > 0 -> 2
                            else -> 1
                        }
                    }
                }
            }
            adapter = movieAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )
            setHasFixedSize(true)
        }

        binding.buttonRetry.setOnClickListener {
            showLayoutError(false)
            movieAdapter.retry()
        }
    }

    private fun showMovies() {
        mDisposable.add(mMainActivityViewModel.movies()
            .subscribe { pagingData -> movieAdapter.submitData(lifecycle, pagingData) })
    }

    private fun showUI() {
        movieAdapter.addLoadStateListener { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && movieAdapter.itemCount == 0

            showProgressBar(loadState.source.refresh is LoadState.Loading)
            showRecyclerView(!isListEmpty)
            showLayoutNoResult(isListEmpty)

            val errorState = loadState.refresh as? LoadState.Error
            errorState?.let { loadStateError ->
                val errorMessage = when (loadStateError.error) {
                    is IOException -> getString(R.string.no_connect_message)
                    is HttpException -> String.format(
                        getString(R.string.error_result_message_retry),
                        loadStateError.error.localizedMessage
                    )
                    else -> getString(R.string.error_result_message_unknown_retry)
                }

                showError(errorMessage)
            }
        }
    }

    private fun showProgressBar(visibility: Boolean) {
        binding.progressBar.isVisible = visibility
    }

    private fun showLayoutNoResult(visibility: Boolean) {
        binding.layoutNoResult.isVisible = visibility
    }

    private fun showRecyclerView(visibility: Boolean) {
        binding.rvMovies.isVisible = visibility
    }

    private fun showError(errorMessage: String) {
        showErrorMessage(errorMessage)
        showLayoutError(true)
    }

    private fun showErrorMessage(errorMessage: String) {
        binding.tvErrorMessage.text = errorMessage
    }

    private fun showLayoutError(visibility: Boolean) {
        binding.layoutError.isVisible = visibility
    }
}



