package com.popcon.picks.worker

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object WorkManagerHelper {
    private const val WORK_TAG = "fetch_movie_list"

    fun startFetchMovieListWorker(context: Context) {
        val fetchMovieListWork = PeriodicWorkRequestBuilder<FetchMovieListWorker>(
            30, TimeUnit.MINUTES
        ).addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueue(fetchMovieListWork)
    }

    fun stopFetchMovieListWorker(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
    }
}