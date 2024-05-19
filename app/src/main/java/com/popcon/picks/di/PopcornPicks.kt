package com.popcon.picks.di

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.popcon.picks.worker.WorkManagerHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class PopcornPicks : Application(), Configuration.Provider {
    companion object {
        var instance: PopcornPicks? = null
    }
    private val TAG = PopcornPicks::class.java.simpleName

    @Inject
    lateinit var workerFactory : HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        instance = this
        WorkManagerHelper.startFetchMovieListWorker(applicationContext) //start work manager to fetch movie list every 30 min
    }
    override fun onTerminate() {
        super.onTerminate()
        Log.d(TAG,"onTerminate called")
        WorkManagerHelper.stopFetchMovieListWorker(applicationContext) //stop work manager on app termination
        instance = null
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}