package com.example.project_android.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import com.example.project_android.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.loadinganimation.LoadingAnimation
import com.example.project_android.ui.adapters.MovieAdapter
import com.example.project_android.ui.adapters.MovieBannerAutoScroll
import com.example.project_android.data.models.entity.Movie
import com.example.project_android.ui.activity.MovieDetailsActivity
import com.example.project_android.ui.activity.ShowAllActivity
import com.example.project_android.viewmodel.HomeViewModel
import java.util.Timer
import java.util.TimerTask
class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private var sessionId : String? = null
    private var guestSessionId : String? = null
    private lateinit var loadingAnim: LoadingAnimation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (arguments?.getString("sessionId")?.isNotEmpty() == true) {
            sessionId = arguments?.getString("sessionId")
        } else if(arguments?.getString("guestSessionId")?.isNotEmpty() == true) {
            guestSessionId = arguments?.getString("guestSessionId")
        }
        setupMovieList()
    }
    private fun goToShowAll(type: String, movies: List<Movie>) {
        val intent = Intent(requireContext(), ShowAllActivity::class.java)
        intent.putExtra("type", type)
        intent.putExtra("movies", ArrayList(movies))
        intent.putExtra("sessionID", sessionId)
        startActivity(intent)
    }
    private fun setupMovieList() {
        val movieList = requireView().findViewById<RecyclerView>(R.id.rm_movies_list)
        val movieListUpcoming = requireView().findViewById<RecyclerView>(R.id.rm_movies_list_upcoming)
        val movieListTopRated = requireView().findViewById<RecyclerView>(R.id.rm_movies_list_toprated)
        val viewPager = requireView().findViewById<ViewPager>(R.id.rm_movies_list_banner)

        val btnShowAllPopular : ImageButton = requireView().findViewById(R.id.showAllPopular)
        val btnShowAllUpcoming : ImageButton = requireView().findViewById(R.id.showAllUpcoming)
        val btnShowAllTopRated : ImageButton = requireView().findViewById(R.id.showAllTopRated)

        loadingAnim = requireView().findViewById(R.id.loadingAnim)

        var completedRequests = 0
        val totalRequests = 4

        fun checkAllRequestsCompleted() {
            completedRequests++
            if (completedRequests == totalRequests) {
                loadingAnim.visibility = View.GONE // Ẩn loadingAnim khi tất cả request đã hoàn thành
            }
        }

        btnShowAllPopular.setOnClickListener {
            homeViewModel.getMovieData("popular") { movies ->
                goToShowAll("popular", movies)
            }
        }

        btnShowAllUpcoming.setOnClickListener {
            homeViewModel.getMovieData("upcoming") { movies ->
                goToShowAll("upcoming", movies)
            }
        }

        btnShowAllTopRated.setOnClickListener {
            homeViewModel.getMovieData("topRated") { movies ->
                goToShowAll("topRated", movies)
            }
        }

        movieList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        movieList.setHasFixedSize(true)
        movieListUpcoming.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        movieListUpcoming.setHasFixedSize(true)
        movieListTopRated.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
        movieListTopRated.setHasFixedSize(true)

        homeViewModel.getMovieData("popular") { movies: List<Movie> ->
            setupMovieAdapter(movieList, movies)
            checkAllRequestsCompleted()
        }

        homeViewModel.getMovieData("upcoming") { movies: List<Movie> ->
            setupMovieAdapter(movieListUpcoming, movies)
            checkAllRequestsCompleted()
        }

        homeViewModel.getMovieData("topRated") { movies: List<Movie> ->
            setupMovieAdapter(movieListTopRated, movies)
            checkAllRequestsCompleted()
        }

        homeViewModel.getMovieData("trending") { movies: List<Movie> ->
            viewPager.adapter = MovieBannerAutoScroll(movies, requireContext()) { movie ->
                val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
                intent.putExtra("movieID", movie.id.toString())
                intent.putExtra("sessionID", sessionId)
                startActivity(intent)
            }
            // Tự động trượt các item sau một khoảng thời gian
            val handler = Handler()
            val update = Runnable {
                if (viewPager.currentItem == movies.size - 1) {
                    viewPager.currentItem = 0
                } else {
                    viewPager.currentItem = viewPager.currentItem + 1
                }
            }
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    handler.post(update)
                }
            }, 1000, 4000)
            checkAllRequestsCompleted()
        }
    }

    private fun setupMovieAdapter(recyclerView: RecyclerView, movies: List<Movie>) {
        recyclerView.adapter = MovieAdapter(movies) { movie ->
            val intent = Intent(requireContext(), MovieDetailsActivity::class.java)
            intent.putExtra("movieID", movie.id.toString())
            intent.putExtra("sessionID", sessionId)
            startActivity(intent)
        }
    }
}