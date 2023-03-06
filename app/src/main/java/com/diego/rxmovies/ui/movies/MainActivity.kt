package com.diego.rxmovies.ui.movies

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.diego.rxmovies.MyApplication
import com.diego.rxmovies.R
import com.diego.rxmovies.adapter.MovieAdapter
import com.diego.rxmovies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val mMainActivityViewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(
            (applicationContext as MyApplication).moviesRepository
        )
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        initView()
        showMovies()
    }

    private fun initView() {
        binding.lifecycleOwner = this
        binding.apply {
            mainActivityViewModel = mMainActivityViewModel
            toolbar.title = getString(R.string.popular_movies)
        }

        setSupportActionBar(binding.toolbar)
        val mRvMovies = binding.rvMovies
        movieAdapter = MovieAdapter()
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(mRvMovies.context, 2)
            adapter = movieAdapter
            setHasFixedSize(true)
        }
    }

    private fun showMovies() {
        mMainActivityViewModel.movies.observe(this) { movies ->
            movieAdapter.submitList(movies)
        }
    }
}








