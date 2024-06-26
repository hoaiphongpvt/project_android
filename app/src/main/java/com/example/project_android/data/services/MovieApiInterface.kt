package com.example.project_android.data.services

import com.example.project_android.data.models.entity.Movie
import com.example.project_android.data.models.entity.RatingRequest
import com.example.project_android.data.models.network.AccountStateResponse
import com.example.project_android.data.models.network.BaseResponse
import com.example.project_android.data.models.network.CastResponse
import com.example.project_android.data.models.network.MovieResponse
import com.example.project_android.data.models.network.RatingResponse
import com.example.project_android.data.models.network.ReviewResponse
import com.example.project_android.data.models.network.VideoResponse
import com.example.project_android.data.remote.TheMovieDatabaseAPI
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiInterface {

    //GET Popular Movies
    @GET("/${TheMovieDatabaseAPI.API_VERSION}/movie/popular?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getPopularMovieList() :Call<MovieResponse>

    //GET Upcoming Movies
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/upcoming?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getUpcomingMovieList() :Call<MovieResponse>

    //GET Top Rated Movies
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/top_rated?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getTopRatedMovieList() :Call<MovieResponse>

    //Get Now Playing Movies
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/now_playing?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getNowPlayingMovieList() :Call<MovieResponse>

    //Get Trending Movies
    @GET("${TheMovieDatabaseAPI.API_VERSION}/trending/movie/day?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getTrendingMovieList() :Call<MovieResponse>

    //Get Movie Details
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/{movie_id}?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getMovieDetails(@Path("movie_id") id: String): Call<Movie>

    //Get List Casts
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/{movie_id}/credits?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getListCasts(@Path("movie_id") id: String) : Call<CastResponse>

    //Get List Video
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/{movie_id}/videos?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getListVideos(@Path("movie_id") id: String) : Call<VideoResponse>

    //Recommend movies
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/{movie_id}/recommendations?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getRecommendMovie(@Path("movie_id") id : String) : Call<MovieResponse>

    //Get Reviews
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/{movie_id}/reviews?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun getReviews(@Path("movie_id") id : String) : Call<ReviewResponse>

    //Check Account State
    @GET("${TheMovieDatabaseAPI.API_VERSION}/movie/{movie_id}/account_states?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun checkAccountState(@Path("movie_id") id : String, @Query("session_id") sessionId : String) : Call<AccountStateResponse>

    //Add Rating
    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("${TheMovieDatabaseAPI.API_VERSION}/movie/{movie_id}/rating?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun addRating(@Path("movie_id") id : String, @Query("session_id") sessionID : String, @Body ratingRequest: RatingRequest) : Call<RatingResponse>

    //Delete Rating
    @DELETE("${TheMovieDatabaseAPI.API_VERSION}/movie/{movie_id}/rating?api_key=${TheMovieDatabaseAPI.API_KEY}")
    fun deleteRating(@Path("movie_id") id : String, @Query("session_id") sessionID : String) : Call<BaseResponse>
}