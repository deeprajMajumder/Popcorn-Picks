package com.popcon.picks.dataSource.localDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.popcon.picks.utils.Converters

@Database(
    entities = [OfflineEntity::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase(){
    abstract fun getOfflineDataDao() : OfflineDataDao

    companion object{
        @Volatile
        private var instance : AppDataBase? = null
        private const val DATABASE_NAME = "popcornPick.db"
        private const val TAG = "AppDatabase"
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDataBase::class.java, DATABASE_NAME
        ).build()
    }
}