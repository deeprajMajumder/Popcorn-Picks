package com.popcon.picks.worker

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.popcon.picks.dataSource.localDataBase.AppDataBase
import com.popcon.picks.dataSource.localDataBase.OfflineEntity
import com.popcon.picks.dataSource.network.NetworkRepository
import com.popcon.picks.utils.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FetchMovieListWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val appDataBase: AppDataBase,
    private val networkRepository: NetworkRepository,
    private val sharedPreferences: SharedPreferences
) : CoroutineWorker(context, workerParams) {
    private val TAG = FetchMovieListWorker::class.java.simpleName
    private var workId = workerParams.id
    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "doWork id: $workId")
            // Retrieve last fetched page number from SharedPreferences
            val lastFetchedPage = sharedPreferences.getInt(Constants.LAST_FETCHED_PAGE_KEY, 1)
            val nextPage = lastFetchedPage + 1
            val response = networkRepository.getMoviesList(Constants.language, Constants.apiKey, nextPage)

            if (response.isSuccessful) {
                Log.d(TAG, "doWork: ${response.body()}")
                if(isPageAlreadyFetched(nextPage)){
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

                    appDataBase.getOfflineDataDao().insertAll(offlineEntities)
                    // Update last fetched page number in SharedPreferences
                    sharedPreferences.edit().putInt(Constants.LAST_FETCHED_PAGE_KEY, nextPage).apply()
                    Result.success()
                } else {
                  Result.success()
                }
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
    // Helper function to check if page already fetched
    private fun isPageAlreadyFetched(pageNumber: Int): Boolean {
        val lastFetchedPage = sharedPreferences.getInt(Constants.LAST_FETCHED_PAGE_KEY, 1)
        return pageNumber <= lastFetchedPage
    }

}