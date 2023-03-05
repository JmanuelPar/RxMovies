package com.diego.rxmovies.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.diego.rxmovies.R
import com.diego.rxmovies.data.model.Movie
import com.diego.rxmovies.utils.Constants.API_IMG_BASE_URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@BindingAdapter("movieImage")
fun ImageView.setMovieImage(item: Movie?) {
    item?.let { movie ->
        val imgUri = (API_IMG_BASE_URL + movie.posterPath).toUri()
            .buildUpon()
            .scheme("https")
            .build()

        val request = ImageRequest.Builder(context)
            .crossfade(true)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_place_holder)
            .data(imgUri)
            .target(
                onStart = { placeholder ->
                    this.apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        load(placeholder)
                    }
                },
                onSuccess = { result ->
                    this.apply {
                        scaleType = ImageView.ScaleType.FIT_XY
                        load(result)
                    }
                },
                onError = { error ->
                    this.apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        load(error)
                    }
                }
            )
            .build()
        context.imageLoader.enqueue(request)
    }
}

@BindingAdapter("movieTitle")
fun TextView.setMovieTitle(item: Movie?) {
    item?.let { movie ->
        text = movie.title.ifEmpty { context.getString(R.string.not_specified) }
    }
}

@BindingAdapter("movieReleaseDate")
fun TextView.setMovieReleaseDate(item: Movie?) {
    item?.let { movie ->
        text = try {
            when {
                movie.releaseDate.isEmpty() -> context.getString(R.string.not_specified)
                else -> {
                    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                    LocalDate.parse(movie.releaseDate, DateTimeFormatter.ISO_DATE).format(formatter)
                }
            }
        } catch (e: Exception) {
            context.getString(R.string.not_specified)
        }
    }
}

@BindingAdapter("movieRating")
fun TextView.setMovieRating(item: Movie?) {
    item?.let { movie ->
        text =
            if (movie.rating == -1.0) context.getString(R.string.not_rated)
            else movie.rating.toString()
    }
}

@BindingAdapter("errorMessage")
fun TextView.setErrorMessage(item: UIText?) {
    item?.let { uiText ->
        text = context.getMyUIText(uiText)
    }
}