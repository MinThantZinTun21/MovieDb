package com.codigotext.moviedb.ui.upcoming_movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.codigotext.moviedb.R
import com.codigotext.moviedb.common.adapter.MovieListAdapter
import com.codigotext.moviedb.data.model.Event
import com.codigotext.moviedb.databinding.FragmentMoviesBinding
import com.codigotext.moviedb.ui.movie_detail.ActivityMovieDetail
import com.codigotext.moviedb.util.INTENT_KEY
import com.codigotext.moviedb.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentUpcomeMovies : Fragment(R.layout.fragment_movies) {
    private val viewModel: UpcomingMovieViewModel by viewModels()
    private var currentBinding: FragmentMoviesBinding? = null
    private val binding get() = currentBinding!!
    private val movieAdapter by lazy {
        MovieListAdapter {
            val detailMovieIntent = Intent(requireActivity(), ActivityMovieDetail::class.java)
            val bundle=Bundle()
            bundle.putSerializable(INTENT_KEY.DATA.name,it)
            detailMovieIntent.putExtra(INTENT_KEY.DATA.name,bundle)
            startActivity(detailMovieIntent)
        }

    }

    companion object {
        fun newInstance(): FragmentUpcomeMovies {
            val fragment = FragmentUpcomeMovies()
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentBinding = FragmentMoviesBinding.bind(view)
        binding.apply {
            recMoive.apply {
                adapter = movieAdapter
                layoutManager = GridLayoutManager(activity, 2)
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.upcomingMovie.collect {
                    val result = it ?: return@collect
                    refreshView.isRefreshing = result is Resource.Loading
                    recMoive.isVisible = !result.data.isNullOrEmpty()
                    tvErrorView.isVisible = result.error != null && result.data.isNullOrEmpty()
                    btnRetry.isVisible = result.error != null && result.data.isNullOrEmpty()
                    tvErrorView.text = result.error?.localizedMessage

                    movieAdapter.submitList(result.data) {
                        if (viewModel.scrollingToTopAfterRefresh) {
                            recMoive.scrollToPosition(0)
                            viewModel.scrollingToTopAfterRefresh = false

                        }
                    }


                }

            }

            refreshView.setOnRefreshListener {
                viewModel.onRefresh()
            }

            btnRetry.setOnClickListener {
                viewModel.onRefresh()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.events.collect {
                    when (it) {
                        is Event.showErrorMessage -> {
                            Toast.makeText(
                                requireActivity(),
                                it.error.localizedMessage ?: getString(R.string.error_unknow),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }


        }


    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}