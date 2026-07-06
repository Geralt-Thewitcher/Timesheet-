package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.Project

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PowerLogApp(viewModel: PowerLogViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToProject = { projectId ->
                    viewModel.setCurrentProject(projectId)
                    navController.navigate("project_details")
                },
                onAddProject = {
                    navController.navigate("add_project")
                }
            )
        }
        composable("add_project") {
            AddProjectScreen(
                onBack = { navController.popBackStack() },
                onSave = { name, client, site ->
                    viewModel.addProject(name, client, site)
                    navController.popBackStack()
                }
            )
        }
        composable("project_details") {
            ProjectDetailsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onAddActivity = { navController.navigate("add_activity") }
            )
        }
        composable("add_activity") {
            AddActivityScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSave = { cat, desc, equip ->
                    viewModel.currentProjectId.value?.let {
                        viewModel.addActivity(it, cat, desc, equip)
                    }
                    navController.popBackStack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: PowerLogViewModel,
    onNavigateToProject: (Int) -> Unit,
    onAddProject: () -> Unit
) {
    val projects by viewModel.allProjects.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("PowerLog Pro", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProject) {
                Icon(Icons.Default.Add, contentDescription = "Add Project")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            // Stats Row
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatCard("Projects", projects.size.toString())
                StatCard("Hours", "42.5")
                StatCard("Pending", "3")
            }
            Text("Recent Projects", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                items(projects) { project ->
                    ProjectCard(project) { onNavigateToProject(project.id) }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        modifier = Modifier.size(100.dp).padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(project.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(project.client, fontSize = 14.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(project.site, fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectScreen(onBack: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var client by remember { mutableStateOf("") }
    var site by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Project") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Project Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = client,
                onValueChange = { client = it },
                label = { Text("Client") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = site,
                onValueChange = { site = it },
                label = { Text("Site Location") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onSave(name, client, site) },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() && client.isNotBlank()
            ) {
                Text("Create Project")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailsScreen(
    viewModel: PowerLogViewModel,
    onBack: () -> Unit,
    onAddActivity: () -> Unit
) {
    val activities by viewModel.activities.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddActivity) {
                Icon(Icons.Default.Add, contentDescription = "Add Activity")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                val context = LocalContext.current
                val currentProject = viewModel.allProjects.collectAsState().value.find { it.id == viewModel.currentProjectId.value }
                Column { 
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { currentProject?.let { ExportManager.exportToExcel(context, it, activities) } }) {
                            Icon(Icons.Default.FileDownload, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export Excel")
                        }
                        Button(onClick = { currentProject?.let { ExportManager.exportToPdf(context, it, activities) } }) {
                            Icon(Icons.Default.FileDownload, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Export PDF")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    currentProject?.let { Iec60034ReportButton(context, it, activities) }
                }
            }
            Text("Activities", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
                items(activities) { activity ->
                    ActivityCard(activity)
                }
            }
        }
    }
}

@Composable
fun ActivityCard(activity: com.example.data.ActivityLog) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(activity.category, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(activity.status, color = if(activity.status == "Completed") Color.Green else Color.Gray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(activity.description)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Time: ${activity.startTime} - ${activity.endTime}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    viewModel: PowerLogViewModel,
    onBack: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var equipment by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Activity") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category (e.g. Electrical, Testing)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = equipment,
                onValueChange = { equipment = it },
                label = { Text("Equipment (e.g. Transformer, Motor)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onSave(category, description, equipment) },
                modifier = Modifier.fillMaxWidth(),
                enabled = category.isNotBlank() && description.isNotBlank()
            ) {
                Text("Save Activity")
            }
        }
    }
}

@Composable
fun Iec60034ReportButton(context: android.content.Context, project: com.example.data.Project, activities: List<com.example.data.ActivityLog>) {
    Button(
        onClick = { 
            com.example.ui.ExportManager.exportIec60034Pdf(context, project, activities)
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Icon(androidx.compose.material.icons.Icons.Default.FileDownload, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text("IEC 60034-1 Report")
    }
}
