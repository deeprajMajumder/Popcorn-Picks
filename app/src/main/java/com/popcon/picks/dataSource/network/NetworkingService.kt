package com.popcon.picks.dataSource.network

import android.graphics.Bitmap
import com.popcon.picks.model.Movies
import com.popcon.picks.model.MoviesParent
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkingService {
    @GET("trending/movie/day?")
    suspend fun getMoviesList(
        @Query("language") language: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int, //each page contains 20 movies
    ) : Response<MoviesParent>

    @GET("movie/")
    suspend fun getMovieDetails(
        @Query("") movieId: Int,
        @Query("api_key") apiKey: String,
    ) : Response<Movies>

    @GET
    suspend fun getThumbnail(
        @Query("poster_path") posterPath: String
    ) : Response<Bitmap>
}