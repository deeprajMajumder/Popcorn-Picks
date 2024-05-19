package com.popcon.picks.dataSource

import android.util.Log
import com.popcon.picks.dataSource.localDataBase.AppDataBase
import com.popcon.picks.dataSource.localDataBase.OfflineEntity
import com.popcon.picks.dataSource.localDataBase.OfflineMovieEntity
import com.popcon.picks.dataSource.network.NetworkRepository
import javax.inject.Inject

class Repository @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val appdataBase: AppDataBase
) {
    private val TAG = Repository::class.java.simpleName
    suspend fun getMovieList(language: String, apiKey: String, page: Int): List<OfflineEntity?> {
        if (appdataBase.getOfflineDataDao().getAll().isEmpty()) {
            val response = networkRepository.getMoviesList(language, apiKey, page)
            if (response.isSuccessful) {
                response.body()?.results?.let {
                    val offlineEntities = response.body()?.results?.map { movie ->
                        OfflineEntity(
                            movieId = movie.id,
                            backdropPath = movie.backdropPath,
                            originalTitle = movie.originalTitle,
                            overview = movie.overview,
                            posterPath = movie.posterPath,
                            mediaType = movie.mediaType,
                            adult = movie.adult,
                            title = movie.title,
                            originalLanguage = movie.originalLanguage,
                            genreIds = movie.genreIds,
                            popularity = movie.popularity,
                            releaseDate = movie.releaseDate,
                            video = movie.video,
                            voteAverage = movie.voteAverage,
                            voteCount = movie.voteCount
                        )
                    } ?: emptyList()

                    appdataBase.getOfflineDataDao().insertAll(offlineEntities)

                    return offlineEntities
                }
            } else {
                Log.e(TAG, "Network call failed: ${response.errorBody()?.string()}")
            }
        } else {
            return appdataBase.getOfflineDataDao().getAll()
        }
        return emptyList()
    }

    suspend fun getMovieDetails(movieId: Int, apiKey: String): OfflineMovieEntity {
        val localMovie = appdataBase.getOfflineMovieDao().getMovieById(movieId)
        return if (localMovie == null) {
            // If not found, fetch from the network
            val response = networkRepository.getMovieDetails(movieId, apiKey)
            if (response.isSuccessful) {
                Log.d(TAG,"Movie details response is: ${response.body()}")
                response.body()?.let { movieResponse ->
                    // Map the network response to an OfflineMovieEntity
                    val offlineMovieEntity = OfflineMovieEntity(
                        movieId = movieResponse.movieId,
                        adult = movieResponse.adult,
                        backdropPath = movieResponse.backdropPath,
                        belongsToCollection = movieResponse.belongsToCollection,
                        budget = movieResponse.budget,
                        genres = movieResponse.genres,
                        homepage = movieResponse.homepage,
                        imdbId = movieResponse.imdbId,
                        originCountry = movieResponse.originCountry,
                        originalLanguage = movieResponse.originalLanguage,
                        originalTitle = movieResponse.originalTitle,
                        overview = movieResponse.overview,
                        popularity = movieResponse.popularity,
                        posterPath = movieResponse.posterPath,
                        productionCompanies = movieResponse.productionCompanies,
                        productionCountries = movieResponse.productionCountries,
                        releaseDate = movieResponse.releaseDate,
                        revenue = movieResponse.revenue,
                        runtime = movieResponse.runtime,
                        spokenLanguages = movieResponse.spokenLanguages,
                        status = movieResponse.status,
                        tagline = movieResponse.tagline,
                        title = movieResponse.title,
                        video = movieResponse.video,
                        voteAverage = movieResponse.voteAverage,
                        voteCount = movieResponse.voteCount
                    )
                    // Insert the new entity into the database
                    appdataBase.getOfflineMovieDao().insert(offlineMovieEntity)
                    // Return the newly inserted entity
                    return offlineMovieEntity
                } ?: throw Exception("Response body is null")
            } else {
                Log.e(TAG, "Network call failed: ${response.errorBody()?.string()}")
                throw Exception("Network request failed")
            }
        } else {
            // If found in the database, return the local movie
            localMovie
        }
    }
}