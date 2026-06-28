package com.example.data

import kotlinx.coroutines.flow.Flow

class SiteLogRepository(private val siteLogDao: SiteLogDao) {
    val allLogs: Flow<List<SiteLog>> = siteLogDao.getAllLogs()

    fun getLogsByWorker(workerName: String): Flow<List<SiteLog>> {
        return siteLogDao.getLogsByWorker(workerName)
    }

    suspend fun insertLog(log: SiteLog): Long {
        return siteLogDao.insertLog(log)
    }

    suspend fun updateLog(log: SiteLog) {
        siteLogDao.updateLog(log)
    }

    suspend fun deleteLog(log: SiteLog) {
        siteLogDao.deleteLog(log)
    }

    suspend fun deleteLogById(id: Int) {
        siteLogDao.deleteLogById(id)
    }

    suspend fun clearAllLogs() {
        siteLogDao.clearAllLogs()
    }
}
