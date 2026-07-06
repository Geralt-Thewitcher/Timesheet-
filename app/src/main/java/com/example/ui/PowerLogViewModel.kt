package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.ActivityLog
import com.example.data.Equipment
import com.example.data.PowerLogDao
import com.example.data.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PowerLogViewModel(application: Application, private val dao: PowerLogDao) : AndroidViewModel(application) {

    val allProjects: StateFlow<List<Project>> = dao.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentProjectId = MutableStateFlow<Int?>(null)
    val currentProjectId: StateFlow<Int?> = _currentProjectId.asStateFlow()

    private val _activities = MutableStateFlow<List<ActivityLog>>(emptyList())
    val activities: StateFlow<List<ActivityLog>> = _activities.asStateFlow()

    fun setCurrentProject(projectId: Int) {
        _currentProjectId.value = projectId
        viewModelScope.launch {
            dao.getActivitiesForProject(projectId).collect {
                _activities.value = it
            }
        }
    }

    fun addProject(name: String, client: String, site: String) {
        viewModelScope.launch {
            dao.insertProject(Project(name = name, client = client, site = site, startDate = "2026-07-05", status = "Active"))
        }
    }

    fun addActivity(projectId: Int, category: String, description: String, equipment: String) {
        viewModelScope.launch {
            dao.insertActivity(
                ActivityLog(
                    projectId = projectId,
                    date = "2026-07-05",
                    category = category,
                    description = description,
                    startTime = "09:00",
                    endTime = "17:00",
                    equipment = equipment,
                    status = "Completed"
                )
            )
        }
    }
}

class PowerLogViewModelFactory(
    private val application: Application,
    private val dao: PowerLogDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PowerLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PowerLogViewModel(application, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
