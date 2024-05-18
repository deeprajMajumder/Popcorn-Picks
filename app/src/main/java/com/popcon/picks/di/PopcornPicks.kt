package com.popcon.picks.di

import android.app.Application
import com.popcon.picks.dataSource.localDataBase.AppDataBase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PopcornPicks : Application() {
    companion object {
        var instance: PopcornPicks? = null
    }
    private var mAppDatabase: AppDataBase? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        mAppDatabase = AppDataBase.invoke(this)
    }
}