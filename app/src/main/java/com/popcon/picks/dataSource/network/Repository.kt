package com.popcon.picks.dataSource.network

import com.popcon.picks.model.Movies
import com.popcon.picks.model.MoviesParent
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val networkingService: NetworkingService) {
    suspend fun getMoviesList(language: String, apiKey: String, page: Int) : Response<MoviesParent> =
        networkingService.getMoviesList(language,apiKey,page)

    suspend fun getMovieDetails(id : Int, apiKey: String) : Response<Movies> =
        networkingService.getMovieDetails(id, apiKey)

}