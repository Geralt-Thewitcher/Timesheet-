package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

@Entity(tableName = "site_logs")
data class SiteLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workerName: String,
    val dateString: String, // format: "yyyy-MM-dd"
    val startTime: String,  // format: "HH:mm"
    val endTime: String,    // format: "HH:mm"
    val breakMinutes: Int,  // e.g., 30, 45, 60
    val hourlyRate: Double,
    val activitiesText: String,
    val siteName: String = "General Site",
    val isParsedByAi: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) {
    // Utility to calculate total minutes between start and end time
    fun getGrossMinutes(): Int {
        try {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val start = format.parse(startTime) ?: return 0
            val end = format.parse(endTime) ?: return 0
            
            var diffMs = end.time - start.time
            if (diffMs < 0) {
                // Handle overnight shift: add 24 hours
                diffMs += 24 * 60 * 60 * 1000
            }
            return (diffMs / (1000 * 60)).toInt()
        } catch (e: Exception) {
            return 0
        }
    }

    fun getGrossHours(): Double {
        return getGrossMinutes() / 60.0
    }

    fun getNetHours(): Double {
        val netMinutes = max(0, getGrossMinutes() - breakMinutes)
        return netMinutes / 60.0
    }

    fun getRegularHours(): Double {
        return min(8.0, getNetHours())
    }

    fun getOvertimeHours(): Double {
        return max(0.0, getNetHours() - 8.0)
    }

    fun getRegularPay(): Double {
        return getRegularHours() * hourlyRate
    }

    fun getOvertimePay(): Double {
        return getOvertimeHours() * hourlyRate * 1.5
    }

    fun getTotalPay(): Double {
        return getRegularPay() + getOvertimePay()
    }

    fun getFormattedDate(): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val date = parser.parse(dateString) ?: return dateString
            formatter.format(date)
        } catch (e: Exception) {
            dateString
        }
    }
}
