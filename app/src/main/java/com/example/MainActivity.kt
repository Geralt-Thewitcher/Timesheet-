package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.ui.PowerLogApp
import com.example.ui.PowerLogViewModel
import com.example.ui.PowerLogViewModelFactory
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(applicationContext)
        val factory = PowerLogViewModelFactory(application, database.powerLogDao())
        val viewModel = ViewModelProvider(this, factory)[PowerLogViewModel::class.java]

        setContent {
            MyApplicationTheme {
                PowerLogApp(viewModel = viewModel)
            }
        }
    }
}
