package com.example.project_android.ui.activity


import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_android.R
import com.example.project_android.data.models.entity.Cast
import com.example.project_android.data.models.entity.Movie
import com.example.project_android.data.models.entity.Video
import com.example.project_android.data.remote.TheMovieDatabaseAPI.BASE_IMG
import com.example.project_android.ui.adapters.CastAdapter
import com.example.project_android.ui.adapters.VideoAdapter
import com.example.project_android.utils.convertDateFormat
import com.example.project_android.viewmodel.MovieDetailsViewModel

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private lateinit var titlePage : TextView
    private lateinit var backButton : ImageButton
    private lateinit var movieBackground : ImageView
    private lateinit var movieTitle : TextView
    private lateinit var genreText : TextView
    private lateinit var rating : RatingBar
    private lateinit var numOfVotes : TextView
    private lateinit var episodeText: TextView
    private lateinit var seasonText: TextView
    private lateinit var movieLanguage: TextView
    private lateinit var overviewText: TextView
    private lateinit var castRecyclerView: RecyclerView
    private lateinit var videoRecyclerView: RecyclerView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        movieDetailsViewModel = ViewModelProvider(this).get(MovieDetailsViewModel::class.java)

        titlePage = findViewById(R.id.titlePage)
        backButton = findViewById(R.id.backButton)
        movieBackground = findViewById(R.id.movie_background)
        movieTitle = findViewById(R.id.movieTitle)
        genreText = findViewById(R.id.genresText)
        rating = findViewById(R.id.ratingBar)
        numOfVotes = findViewById(R.id.numOfVotes)
        episodeText = findViewById(R.id.episodeText)
        seasonText = findViewById(R.id.seasonText)
        movieLanguage = findViewById(R.id.movieLanguage)
        overviewText = findViewById(R.id.overviewText)
        castRecyclerView = findViewById(R.id.castRecyclerView)
        videoRecyclerView = findViewById(R.id.videosRecyclerView)

        val movie = intent.getParcelableExtra<Movie>("movie")

        if (movie != null) {
            movieDetailsViewModel.getMovieDetailsData(movie.id.toString()) { movieDetails ->
                if (movieDetails != null) {
                    titlePage.text = movieDetails.title
                    movieTitle.text = movieDetails.title
                    genreText.text = movieDetails.genres?.map { it.name }?.joinToString(" / ")
                    Glide.with(movieBackground).load(BASE_IMG + movieDetails.backdropPath).into(movieBackground)
                    rating.rating = movieDetails.voteAverage / 2
                    numOfVotes.text = movieDetails.voteCount.toString() + " votes"
                    episodeText.text = movieDetails.releaseDate?.convertDateFormat()
                    seasonText.text = movieDetails.runtime.toString() + " min"
                    movieLanguage.text =
                        movieDetails.spokenLanguages?.joinToString(" / ") { it.englishName }
                    overviewText.text = movieDetails.overview

                    backButton.setOnClickListener {
                        onBackPressed()
                    }
                }
            }

            movieDetailsViewModel.getListCastsData(movie.id.toString()) { casts: List<Cast> ->
                setupCastAdapter(castRecyclerView, casts)
            }

            movieDetailsViewModel.getListVideosData(movie.id.toString()) { videos: List<Video> ->
                setupVideoAdapter(videoRecyclerView, videos)
            }
        }
    }
    private fun setupCastAdapter(recyclerView: RecyclerView, casts: List<Cast>) {
        recyclerView.adapter = CastAdapter(casts) { cast ->
            val intent = Intent(this, CastDetailsActivity::class.java)
            intent.putExtra("cast", cast)
            startActivity(intent)
        }
    }

    private fun setupVideoAdapter(recyclerView: RecyclerView, videos: List<Video>) {
        recyclerView.adapter = VideoAdapter(videos)
    }


}