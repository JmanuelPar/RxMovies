package com.diego.rxmovies

import android.app.Application
import com.diego.rxmovies.repository.MoviesRepository
import com.diego.rxmovies.utils.ServiceLocator
import timber.log.Timber

class MyApplication : Application() {

    val moviesRepository: MoviesRepository
        get() = ServiceLocator.provideMoviesRepository()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}