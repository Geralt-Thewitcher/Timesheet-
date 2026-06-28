package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SiteLogDao {
    @Query("SELECT * FROM site_logs ORDER BY dateString DESC, timestamp DESC")
    fun getAllLogs(): Flow<List<SiteLog>>

    @Query("SELECT * FROM site_logs WHERE workerName LIKE '%' || :workerName || '%' ORDER BY dateString DESC")
    fun getLogsByWorker(workerName: String): Flow<List<SiteLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: SiteLog): Long

    @Update
    suspend fun updateLog(log: SiteLog)

    @Delete
    suspend fun deleteLog(log: SiteLog)

    @Query("DELETE FROM site_logs WHERE id = :id")
    suspend fun deleteLogById(id: Int)

    @Query("DELETE FROM site_logs")
    suspend fun clearAllLogs()
}
