package com.popcon.picks.dataSource.localDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OfflineDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(offlineData: List<OfflineEntity>)

    @Query("SELECT * FROM offline_entity LIMIT 20 OFFSET :offset")
    suspend fun getMoviesByPage(offset: Int): List<OfflineEntity>

    @Query("SELECT * FROM offline_entity WHERE movie_id = :movieId")
    suspend fun getMovieById(movieId: Int): OfflineEntity?

    @Query("DELETE FROM offline_entity")
    suspend fun deleteAll()

}