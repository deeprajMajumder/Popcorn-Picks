package com.popcon.picks.dataSource.localDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OfflineDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(offlineData: List<OfflineEntity>)

    @Query("SELECT * FROM offline_entity")
    suspend fun getAll(): List<OfflineEntity?>

    @Query("DELETE FROM offline_entity")
    suspend fun deleteAll()

}