package com.codigotext.moviedb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.codigotext.moviedb.databinding.ActivityMainBinding
import com.codigotext.moviedb.ui.populator_movie.FragmentPopulatorMovie
import com.codigotext.moviedb.ui.upcoming_movie.FragmentUpcomeMovies
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

private const val UPCOMING_MOVIE_FRA_TAG = "upcominmovie"
private const val POPULATOR_MOVIE_FRA_TAG = "populator"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val fragmentUpcomeMovies: FragmentUpcomeMovies = FragmentUpcomeMovies.newInstance()
    private val fragmentPopulatorMovie: FragmentPopulatorMovie =
        FragmentPopulatorMovie.newInstance()
    private var activeFragment: Fragment = fragmentUpcomeMovies
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initTabFragment()

        findViewById<BottomNavigationView>(R.id.bottom_nav).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_poppulator -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(fragmentPopulatorMovie)
                        .commit()
                    activeFragment = fragmentPopulatorMovie

                }
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(fragmentUpcomeMovies)
                        .commit()
                    activeFragment = fragmentUpcomeMovies

                }
            }
            true
        }
        //bind not working why this happen ? need fix
        /* binding.bottomNav.setOnNavigationItemSelectedListener {
             when (it.itemId) {
                 R.id.nav_poppulator -> {
                     supportFragmentManager.beginTransaction().replace(
                         R.id.fragment_container, FragmentPopulatorMovie.newInstance(),
                         POPULATOR_MOVIE_FRA_TAG
                     )
                         .commit()
                 }
                 R.id.nav_home -> {
                     supportFragmentManager.beginTransaction().replace(
                         R.id.fragment_container, FragmentUpcomeMovies.newInstance(),
                         UPCOMING_MOVIE_FRA_TAG
                     ).commit()
                 }
             }
             false
         }*/
    }

    private fun initTabFragment() {
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                fragmentUpcomeMovies,
                UPCOMING_MOVIE_FRA_TAG
            )
            .commit()
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                fragmentPopulatorMovie,
                POPULATOR_MOVIE_FRA_TAG
            )
            .hide(fragmentPopulatorMovie)
            .commit()
    }

}