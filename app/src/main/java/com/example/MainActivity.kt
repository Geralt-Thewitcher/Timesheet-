package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.SiteLogRepository
import com.example.ui.TimesheetAppScreen
import com.example.ui.TimesheetViewModel
import com.example.ui.TimesheetViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Instantiate Room Database, Repository, and ViewModel using constructor injection
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = SiteLogRepository(database.siteLogDao())
        val factory = TimesheetViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[TimesheetViewModel::class.java]

        setContent {
            MyApplicationTheme {
                TimesheetAppScreen(viewModel = viewModel)
            }
        }
    }
}
