package com.popcon.picks.dataSource.localDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OfflineMovieDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(offlineData: OfflineMovieEntity)

    @Query("SELECT * FROM movies WHERE movieId = :id")
    suspend fun getMovieById(id : Int): OfflineMovieEntity?

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()


}