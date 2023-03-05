package com.diego.rxmovies.utils

import com.diego.rxmovies.data.model.Movie
import com.diego.rxmovies.data.model.NetworkMovies
import com.diego.rxmovies.data.model.Result

// Convert NetworkMovies to List Movie
fun NetworkMovies.asDomainModel() =
    results?.map { result ->
        processingItems(result)
    } ?: emptyList()

private fun processingItems(result: Result) =
    Movie(
        idMovie = result.id!!,
        title = result.title ?: "",
        posterPath = result.posterPath ?: "",
        releaseDate = result.releaseDate ?: "",
        rating = result.voteAverage ?: -1.0
    )