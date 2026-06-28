package com.example.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiClient
import com.example.data.AppDatabase
import com.example.data.SiteLog
import com.example.data.SiteLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimesheetViewModel(
    application: Application,
    private val repository: SiteLogRepository
) : AndroidViewModel(application) {

    private val TAG = "TimesheetViewModel"

    // Active Navigation Tab
    private val _activeTab = MutableStateFlow(0)
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    // Logs state from database
    val allLogs: StateFlow<List<SiteLog>> = repository.allLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Log Parsing states
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _isParsing = MutableStateFlow(false)
    val isParsing: StateFlow<Boolean> = _isParsing.asStateFlow()

    private val _parseError = MutableStateFlow<String?>(null)
    val parseError: StateFlow<String?> = _parseError.asStateFlow()

    private val _parsedPreviewLog = MutableStateFlow<SiteLog?>(null)
    val parsedPreviewLog: StateFlow<SiteLog?> = _parsedPreviewLog.asStateFlow()

    // Manual / Edit Form state
    val formWorkerName = MutableStateFlow("")
    val formDateString = MutableStateFlow("")
    val formStartTime = MutableStateFlow("08:00")
    val formEndTime = MutableStateFlow("17:00")
    val formBreakMinutes = MutableStateFlow("30")
    val formHourlyRate = MutableStateFlow("35.0")
    val formActivitiesText = MutableStateFlow("")
    val formSiteName = MutableStateFlow("General Site")

    // For editing existing entries
    private val _editingLogId = MutableStateFlow<Int?>(null)
    val editingLogId: StateFlow<Int?> = _editingLogId.asStateFlow()

    // Filtering states
    private val _workerFilter = MutableStateFlow("")
    val workerFilter: StateFlow<String> = _workerFilter.asStateFlow()

    private val _siteFilter = MutableStateFlow("")
    val siteFilter: StateFlow<String> = _siteFilter.asStateFlow()

    // Selected worker for payroll report
    private val _selectedWorkerForReport = MutableStateFlow<String>("")
    val selectedWorkerForReport: StateFlow<String> = _selectedWorkerForReport.asStateFlow()

    init {
        resetForm()
    }

    fun setActiveTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    fun setInputText(text: String) {
        _inputText.value = text
    }

    fun setWorkerFilter(worker: String) {
        _workerFilter.value = worker
    }

    fun setSiteFilter(site: String) {
        _siteFilter.value = site
    }

    fun setSelectedWorkerForReport(worker: String) {
        _selectedWorkerForReport.value = worker
    }

    fun resetForm() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        formWorkerName.value = ""
        formDateString.value = today
        formStartTime.value = "08:00"
        formEndTime.value = "17:00"
        formBreakMinutes.value = "30"
        formHourlyRate.value = "35.0"
        formActivitiesText.value = ""
        formSiteName.value = "General Site"
        _editingLogId.value = null
    }

    fun parsePulsarLog() {
        val rawText = _inputText.value
        if (rawText.isBlank()) {
            _parseError.value = "Please enter some site activity text to parse."
            return
        }

        viewModelScope.launch {
            _isParsing.value = true
            _parseError.value = null
            _parsedPreviewLog.value = null
            try {
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val parsed = GeminiClient.parseActivityLog(rawText, todayStr)
                _parsedPreviewLog.value = parsed
                
                // Populate the form with parsed values to let the user review and edit
                formWorkerName.value = parsed.workerName
                formDateString.value = parsed.dateString
                formStartTime.value = parsed.startTime
                formEndTime.value = parsed.endTime
                formBreakMinutes.value = parsed.breakMinutes.toString()
                formHourlyRate.value = parsed.hourlyRate.toString()
                formActivitiesText.value = parsed.activitiesText
                formSiteName.value = parsed.siteName
            } catch (e: Exception) {
                Log.e(TAG, "Parsing failed", e)
                _parseError.value = "Failed to parse: ${e.localizedMessage}. Used smart mock parsing."
                
                // Fallback parsing so user experience is not broken
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val parsedFallback = GeminiClient.simulateAiParse(rawText, todayStr)
                _parsedPreviewLog.value = parsedFallback
                
                formWorkerName.value = parsedFallback.workerName
                formDateString.value = parsedFallback.dateString
                formStartTime.value = parsedFallback.startTime
                formEndTime.value = parsedFallback.endTime
                formBreakMinutes.value = parsedFallback.breakMinutes.toString()
                formHourlyRate.value = parsedFallback.hourlyRate.toString()
                formActivitiesText.value = parsedFallback.activitiesText
                formSiteName.value = parsedFallback.siteName
            } finally {
                _isParsing.value = false
            }
        }
    }

    fun saveTimesheetEntry() {
        val worker = formWorkerName.value.trim()
        val date = formDateString.value.trim()
        val start = formStartTime.value.trim()
        val end = formEndTime.value.trim()
        val breakMinsStr = formBreakMinutes.value.trim()
        val rateStr = formHourlyRate.value.trim()
        val activities = formActivitiesText.value.trim()
        val site = formSiteName.value.trim()

        if (worker.isEmpty()) {
            _parseError.value = "Worker name cannot be empty."
            return
        }

        val breakMins = breakMinsStr.toIntOrNull() ?: 0
        val rate = rateStr.toDoubleOrNull() ?: 35.0

        val newLog = SiteLog(
            id = _editingLogId.value ?: 0,
            workerName = worker,
            dateString = date,
            startTime = start,
            endTime = end,
            breakMinutes = breakMins,
            hourlyRate = rate,
            activitiesText = activities,
            siteName = site,
            isParsedByAi = _parsedPreviewLog.value != null,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            try {
                if (_editingLogId.value != null) {
                    repository.updateLog(newLog)
                } else {
                    repository.insertLog(newLog)
                }
                
                // Clear state
                _parsedPreviewLog.value = null
                _inputText.value = ""
                resetForm()
                _parseError.value = null
                
                // Switch to history tab (index 1) to see the saved entry!
                _activeTab.value = 1
            } catch (e: Exception) {
                Log.e(TAG, "Saving failed", e)
                _parseError.value = "Failed to save entry: ${e.localizedMessage}"
            }
        }
    }

    fun deleteLog(log: SiteLog) {
        viewModelScope.launch {
            repository.deleteLog(log)
        }
    }

    fun startEditingLog(log: SiteLog) {
        _editingLogId.value = log.id
        formWorkerName.value = log.workerName
        formDateString.value = log.dateString
        formStartTime.value = log.startTime
        formEndTime.value = log.endTime
        formBreakMinutes.value = log.breakMinutes.toString()
        formHourlyRate.value = log.hourlyRate.toString()
        formActivitiesText.value = log.activitiesText
        formSiteName.value = log.siteName
        
        // Show manual form view by resetting preview and shifting to tab 0
        _parsedPreviewLog.value = null
        _activeTab.value = 0
    }

    fun clearAllLogs() {
        viewModelScope.launch {
            repository.clearAllLogs()
        }
    }

    // Generate timesheet report text
    fun generateTimesheetReportText(worker: String, logs: List<SiteLog>): String {
        val workerLogs = logs.filter { it.workerName.equals(worker, ignoreCase = true) }
            .sortedBy { it.dateString }
        if (workerLogs.isEmpty()) return "No records found for worker: $worker"

        val sb = StringBuilder()
        sb.append("=============================================\n")
        sb.append("PULSAR SITE TIMESHEET REPORT\n")
        sb.append("=============================================\n")
        sb.append("Worker: $worker\n")
        val rate = workerLogs.firstOrNull()?.hourlyRate ?: 35.0
        sb.append("Hourly Rate: $${String.format(Locale.getDefault(), "%.2f", rate)}\n")
        sb.append("---------------------------------------------\n")
        sb.append(String.format(Locale.getDefault(), "%-12s %-12s %-5s %-5s %-4s %-5s %-5s %-5s\n", 
            "Date", "Site", "Start", "End", "Brk", "NetHr", "RegHr", "OTHr"))
        sb.append("---------------------------------------------\n")

        var totalNet = 0.0
        var totalReg = 0.0
        var totalOT = 0.0

        for (log in workerLogs) {
            val net = log.getNetHours()
            val reg = log.getRegularHours()
            val ot = log.getOvertimeHours()
            
            totalNet += net
            totalReg += reg
            totalOT += ot

            sb.append(String.format(Locale.getDefault(), "%-12s %-12s %-5s %-5s %-4d %-5.2f %-5.2f %-5.2f\n",
                log.dateString,
                if (log.siteName.length > 12) log.siteName.take(10) + ".." else log.siteName,
                log.startTime,
                log.endTime,
                log.breakMinutes,
                net,
                reg,
                ot
            ))
        }

        val totalRegPay = totalReg * rate
        val totalOTPay = totalOT * rate * 1.5
        val totalPay = totalRegPay + totalOTPay

        sb.append("---------------------------------------------\n")
        sb.append(String.format(Locale.getDefault(), "TOTAL REGULAR HOURS:  %-5.2f hrs  ($%-5.2f)\n", totalReg, totalRegPay))
        sb.append(String.format(Locale.getDefault(), "TOTAL OVERTIME HOURS: %-5.2f hrs  ($%-5.2f)\n", totalOT, totalOTPay))
        sb.append(String.format(Locale.getDefault(), "TOTAL NET WORK HOURS: %-5.2f hrs\n", totalNet))
        sb.append("---------------------------------------------\n")
        sb.append(String.format(Locale.getDefault(), "ESTIMATED TOTAL PAY:  $%-7.2f\n", totalPay))
        sb.append("=============================================\n")
        sb.append("Generated via Pulsar Site Timesheet App\n")

        return sb.toString()
    }
}

class TimesheetViewModelFactory(
    private val application: Application,
    private val repository: SiteLogRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimesheetViewModel::class.java)) {
            return TimesheetViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
