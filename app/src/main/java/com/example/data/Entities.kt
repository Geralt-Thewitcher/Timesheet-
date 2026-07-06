package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val client: String,
    val site: String,
    val startDate: String,
    val status: String
)

@Entity(tableName = "activities")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: Int,
    val date: String,
    val category: String,
    val description: String,
    val startTime: String,
    val endTime: String,
    val equipment: String,
    val status: String
)

@Entity(tableName = "equipments")
data class Equipment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: Int,
    val name: String,
    val type: String,
    val serialNumber: String,
    val status: String
)
