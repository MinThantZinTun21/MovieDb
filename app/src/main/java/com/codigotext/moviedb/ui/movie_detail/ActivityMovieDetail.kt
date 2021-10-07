package com.codigotext.moviedb.ui.movie_detail

import android.graphics.Movie
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import coil.load
import com.codigotext.moviedb.R
import com.codigotext.moviedb.data.repository.local.MovieEntity
import com.codigotext.moviedb.databinding.ActivityMainBinding
import com.codigotext.moviedb.databinding.ActivityMovieDetailBinding
import com.codigotext.moviedb.util.Constant
import com.codigotext.moviedb.util.INTENT_KEY
import com.codigotext.moviedb.util.ToBooleanInt
import com.codigotext.moviedb.util.ToInBoolean
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ActivityMovieDetail : AppCompatActivity() {
    private var currentMovie: MovieEntity? = null
    private lateinit var binding: ActivityMovieDetailBinding
    private val viewModel: MovieDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        currentMovie = getMovie()
        bindView(currentMovie!!)
        binding.apply {
            btnFav.setOnClickListener {
                updateMovie()
                //rebind view for update fav status
                bindView(currentMovie!!)
            }
        }
    }

    private fun updateMovie() {
        var currentFav = currentMovie!!.isFavourite
        val updateMovie =
            currentMovie!!.copy(isFavourite = (currentFav.ToInBoolean().not()).ToBooleanInt())
        currentMovie = updateMovie
        viewModel.toggleFav(currentMovie!!)
    }

    private fun bindView(movie: MovieEntity) {
        binding.apply {
            tvMovieTitle.text = movie.title
            tvOverView.text = movie.overview
            ivPoster.load("${Constant.IMAGE_URL}${movie.poster_path}")
            var favText = if (movie.isFavourite.ToInBoolean()) "Remove From Fav" else "Add To Fav"
            btnFav.text = favText
        }
    }

    private fun getMovie(): MovieEntity {
        return intent.getBundleExtra(INTENT_KEY.DATA.name)?.get(INTENT_KEY.DATA.name) as MovieEntity
    }
    override fun onBackPressed() {
        finish()
    }
}