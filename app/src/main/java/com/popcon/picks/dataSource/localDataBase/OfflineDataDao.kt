package com.popcon.picks.dataSource.localDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OfflineDataDao {
    @Insert
    suspend fun insert(offlineData: OfflineEntity)

    @Query("SELECT * FROM offline_entity")
    suspend fun getAll(): List<OfflineEntity?>

    @Query("DELETE FROM offline_entity")
    suspend fun deleteAll()

}