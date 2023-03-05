package com.diego.rxmovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.diego.rxmovies.data.model.Movie
import com.diego.rxmovies.databinding.ItemMovieCardBinding

class MovieAdapter() :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MOVIE_DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { item -> holder.bind(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    class MovieViewHolder private constructor(private val binding: ItemMovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            binding.apply {
                movie = item
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): MovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieCardBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return MovieViewHolder(binding)
            }
        }
    }

    companion object {
        val MOVIE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.idMovie == newItem.idMovie

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }
}