package com.example.project_android.viewmodel

import androidx.lifecycle.ViewModel
import com.example.project_android.data.models.entity.Cast
import com.example.project_android.data.models.entity.Movie
import com.example.project_android.data.models.entity.Review
import com.example.project_android.data.models.entity.Video
import com.example.project_android.data.models.network.AccountStateResponse
import com.example.project_android.data.models.network.CastResponse
import com.example.project_android.data.models.network.MovieResponse
import com.example.project_android.data.models.network.ReviewResponse
import com.example.project_android.data.models.network.VideoResponse
import com.example.project_android.data.services.ApiServices
import com.example.project_android.data.services.MovieApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel : ViewModel() {

    private  val apiService = ApiServices.getInstance().create(MovieApiInterface::class.java)
     fun getMovieDetailsData(movieID: String, callback: (Movie?) -> Unit) {
        val call: Call<Movie> = apiService.getMovieDetails(movieID)

        call.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    callback(movieResponse)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                callback(null)
            }
        })
    }

     fun getListCastsData(movieID: String, callback: (List<Cast>) -> Unit) {
        val call: Call<CastResponse> = apiService.getListCasts(movieID)

        call.enqueue(object: Callback<CastResponse> {
            override fun onResponse(call: Call<CastResponse>, response: Response<CastResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.cast ?: emptyList())
                }
            }

            override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

     fun getListVideosData(movieID: String, callback: (List<Video>) -> Unit) {
        val call: Call<VideoResponse> = apiService.getListVideos(movieID)

        call.enqueue(object: Callback<VideoResponse> {
            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.video ?: emptyList())
                }
            }

            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
     }

    fun getListRecommendMovies(movieID: String, callback: (List<Movie>) -> Unit) {
        val call: Call<MovieResponse> = apiService.getRecommendMovie(movieID)

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val listRecommendMovies = response.body()?.movie
                    if (listRecommendMovies != null) {
                        callback(listRecommendMovies)
                    }
                } else {
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
               callback(emptyList())
            }
        })
    }

    fun getReviews(movieID: String, callback: (List<Review>, Int) -> Unit) {
        val call: Call<ReviewResponse> = apiService.getReviews(movieID)

        call.enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                if (response.isSuccessful) {
                    val reviews = response.body()?.reviews
                    val totalReviews = response.body()?.totalReviews
                    callback(reviews!!, totalReviews!!)
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                callback(emptyList(), 0)
            }
        })
    }

    fun checkAccountState(movieID: String, sessionID: String, callback: (AccountStateResponse?, String) -> Unit) {
        val call: Call<AccountStateResponse> = apiService.checkAccountState(movieID, sessionID)

        call.enqueue(object : Callback<AccountStateResponse> {
            override fun onResponse(
                call: Call<AccountStateResponse>,
                response: Response<AccountStateResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        callback(result, "Success")
                    }
                }
            }

            override fun onFailure(call: Call<AccountStateResponse>, t: Throwable) {
                callback(null, "Fail!")
            }
        })
    }
}