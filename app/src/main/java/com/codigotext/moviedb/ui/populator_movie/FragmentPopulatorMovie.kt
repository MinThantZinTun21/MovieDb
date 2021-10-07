package com.codigotext.moviedb.ui.populator_movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.codigotext.moviedb.R
import com.codigotext.moviedb.common.adapter.AdapterPageLoading
import com.codigotext.moviedb.common.adapter.MovieListAdapter
import com.codigotext.moviedb.common.adapter.MoviePagingAdapter
import com.codigotext.moviedb.databinding.FragmentMoviesBinding
import com.codigotext.moviedb.databinding.FragmentPopulatorMovieBinding
import com.codigotext.moviedb.ui.movie_detail.ActivityMovieDetail
import com.codigotext.moviedb.ui.upcoming_movie.FragmentUpcomeMovies
import com.codigotext.moviedb.util.INTENT_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class FragmentPopulatorMovie : Fragment(R.layout.fragment_populator_movie) {
    private val viewModel: PopulatorMovieViewModel by viewModels()
    private var currentbinding: FragmentPopulatorMovieBinding? = null
    private val binding get() = currentbinding!!
    private val movieAdapter by lazy {
        MoviePagingAdapter {
            val detailMovieIntent = Intent(requireActivity(), ActivityMovieDetail::class.java)
            val bundle=Bundle()
            bundle.putSerializable(INTENT_KEY.DATA.name,it)
            detailMovieIntent.putExtra(INTENT_KEY.DATA.name,bundle)
            startActivity(detailMovieIntent)
        }
    }
    companion object {
        fun newInstance(): FragmentPopulatorMovie {
            val fragment = FragmentPopulatorMovie()
            return fragment
        }
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentbinding = FragmentPopulatorMovieBinding.bind(view)
        binding.apply {
            recMovie.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = movieAdapter
                adapter = movieAdapter.withLoadStateHeaderAndFooter(
                    header = AdapterPageLoading { movieAdapter.refresh() },
                    footer = AdapterPageLoading { movieAdapter.retry() }
                )
                setHasFixedSize(true)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.movie.collectLatest {
                    //hide loading
                    swipeRefreshLayout.isRefreshing = false
                    movieAdapter.submitData(it)

                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                movieAdapter.refresh()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()

    }


}