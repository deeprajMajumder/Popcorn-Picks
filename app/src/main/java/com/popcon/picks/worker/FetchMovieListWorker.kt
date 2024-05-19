package com.popcon.picks.worker

import android.content.Context
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
    private val networkRepository: NetworkRepository) :
    CoroutineWorker(context, workerParams) {
    private val TAG = FetchMovieListWorker::class.java.simpleName
    private var workId = workerParams.id
    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "doWork id: $workId")
            val response = networkRepository.getMoviesList(Constants.language, Constants.apiKey, 2)
            if (response.isSuccessful) {
                Log.d(TAG, "doWork: ${response.body()}")
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
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

}