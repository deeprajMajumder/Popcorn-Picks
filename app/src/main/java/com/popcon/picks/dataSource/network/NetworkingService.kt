package com.popcon.picks.dataSource.network

import com.popcon.picks.model.MoviesParent
import com.popcon.picks.model.movieDetails.MovieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkingService {
    @GET("trending/movie/day")
    suspend fun getMoviesList(
        @Query("language") language: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int, //each page contains 20 movies
    ) : Response<MoviesParent>

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") apiKey: String,
    ): Response<MovieDetails>

}