package com.example.ui

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import org.json.JSONArray
import org.json.JSONObject
import androidx.compose.material.icons.filled.Download
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SiteLog
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.SafetyAmber
import com.example.ui.theme.SafetyAmberLight
import com.example.ui.theme.SteelDarkBg
import com.example.ui.theme.SteelGray
import com.example.ui.theme.SteelSurface
import com.example.ui.theme.SuccessTeal
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextDark
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimesheetAppScreen(viewModel: TimesheetViewModel) {
    val activeTab by viewModel.activeTab.collectAsState()
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = SteelSurface,
                modifier = Modifier
                    .border(1.dp, CardBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { viewModel.setActiveTab(0) },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Entry") },
                    label = { Text("Add & AI Parse", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF0C0E12),
                        selectedTextColor = SafetyAmber,
                        indicatorColor = SafetyAmber,
                        unselectedIconColor = TextLight.copy(alpha = 0.6f),
                        unselectedTextColor = TextLight.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_add_tab")
                )
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { viewModel.setActiveTab(1) },
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text("Saved Logs", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF0C0E12),
                        selectedTextColor = SafetyAmber,
                        indicatorColor = SafetyAmber,
                        unselectedIconColor = TextLight.copy(alpha = 0.6f),
                        unselectedTextColor = TextLight.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_history_tab")
                )
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { viewModel.setActiveTab(2) },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Reports") },
                    label = { Text("Payroll Report", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF0C0E12),
                        selectedTextColor = SafetyAmber,
                        indicatorColor = SafetyAmber,
                        unselectedIconColor = TextLight.copy(alpha = 0.6f),
                        unselectedTextColor = TextLight.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.testTag("nav_reports_tab")
                )
            }
        },
        containerColor = SteelDarkBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // App Header Banner
            AppHeader()

            // Main Content depending on Tab
            when (activeTab) {
                0 -> ParseAndEntryScreen(viewModel)
                1 -> SavedLogsScreen(viewModel)
                2 -> PayrollReportScreen(viewModel)
            }
        }
    }
}

@Composable
fun AppHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SteelSurface)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .border(0.dp, Color.Transparent)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "PULSAR SITE TIMESHEET",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = SafetyAmber,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Field Activity Log & Payroll Calculator",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextLight.copy(alpha = 0.7f)
                )
            }
            
            Box(
                modifier = Modifier
                    .background(SafetyAmber.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .border(1.dp, SafetyAmber.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Powered",
                        tint = SafetyAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "GEMINI AI",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafetyAmber
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = CardBorder, thickness = 1.dp)
    }
}

@Composable
fun ParseAndEntryScreen(viewModel: TimesheetViewModel) {
    val inputText by viewModel.inputText.collectAsState()
    val isParsing by viewModel.isParsing.collectAsState()
    val parseError by viewModel.parseError.collectAsState()
    val previewLog by viewModel.parsedPreviewLog.collectAsState()
    val editingLogId by viewModel.editingLogId.collectAsState()

    var manualMode by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Mode Toggler Card
            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TabRow(
                        selectedTabIndex = if (manualMode) 1 else 0,
                        containerColor = Color.Transparent,
                        contentColor = SafetyAmber,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[if (manualMode) 1 else 0]),
                                color = SafetyAmber
                            )
                        },
                        divider = {}
                    ) {
                        Tab(
                            selected = !manualMode,
                            onClick = { manualMode = false },
                            text = { Text("AI Pulsar Log Parser", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = manualMode,
                            onClick = { manualMode = true },
                            text = { Text("Manual Log Form", fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }
        }

        if (!manualMode) {
            // AI Log Parser Content
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SteelSurface),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Paste Site Activity Log",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyAmber
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Paste text copied from your Pulsar tracker, field logs, or notes. Gemini will extract date, times, worker, and site details.",
                            fontSize = 12.sp,
                            color = TextLight.copy(alpha = 0.7f),
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { viewModel.setInputText(it) },
                            placeholder = {
                                Text(
                                    "e.g., Alex Jenkins - 2026-06-25\n07:30 - Arrived at Pulsar wind farm site.\n12:00 to 12:30 - lunch break.\n17:00 - finished and secured gate.\nWelding and cable layout completed.",
                                    color = TextLight.copy(alpha = 0.4f),
                                    fontSize = 13.sp
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .testTag("pulsar_log_input"),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SafetyAmber,
                                unfocusedBorderColor = CardBorder,
                                focusedTextColor = TextLight,
                                unfocusedTextColor = TextLight
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.parsePulsarLog() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("parse_log_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = SafetyAmber),
                            enabled = !isParsing && inputText.isNotBlank()
                        ) {
                            if (isParsing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = TextDark,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("AI Processing Logs...", color = TextDark, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TextDark)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Format & Calculate with AI", color = TextDark, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Error message if any
        if (parseError != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ErrorRed.copy(alpha = 0.1f)),
                    border = BorderStroke(1.dp, ErrorRed),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Error", tint = ErrorRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = parseError ?: "",
                            fontSize = 12.sp,
                            color = ErrorRed,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Extracted Preview OR Manual Entry Form
        item {
            val showPreview = previewLog != null && !manualMode
            val titleText = if (showPreview) "AI Extracted Timesheet Preview" else if (editingLogId != null) "Edit Timesheet Entry" else "New Timesheet Entry"
            
            AnimatedVisibility(
                visible = showPreview || manualMode,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SteelSurface),
                    border = BorderStroke(1.dp, if (showPreview) SuccessTeal else CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = titleText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (showPreview) SuccessTeal else SafetyAmber
                            )
                            if (showPreview) {
                                Box(
                                    modifier = Modifier
                                        .background(SuccessTeal.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("AI PARSED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SuccessTeal)
                                }
                            }
                        }
                        
                        if (showPreview) {
                            Text(
                                text = "Please review extracted information. You can edit any details in the fields below before finalizing.",
                                fontSize = 11.sp,
                                color = TextLight.copy(alpha = 0.6f),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // Form Fields
                        val formWorker by viewModel.formWorkerName.collectAsState()
                        val formDate by viewModel.formDateString.collectAsState()
                        val formStart by viewModel.formStartTime.collectAsState()
                        val formEnd by viewModel.formEndTime.collectAsState()
                        val formBreak by viewModel.formBreakMinutes.collectAsState()
                        val formRate by viewModel.formHourlyRate.collectAsState()
                        val formSite by viewModel.formSiteName.collectAsState()
                        val formActivities by viewModel.formActivitiesText.collectAsState()

                        OutlinedTextField(
                            value = formWorker,
                            onValueChange = { viewModel.formWorkerName.value = it },
                            label = { Text("Worker Name") },
                            modifier = Modifier.fillMaxWidth().testTag("form_worker_name"),
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = SafetyAmber) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SafetyAmber,
                                unfocusedBorderColor = CardBorder,
                                focusedTextColor = TextLight,
                                unfocusedTextColor = TextLight,
                                focusedLabelColor = SafetyAmber,
                                unfocusedLabelColor = TextLight.copy(alpha = 0.6f)
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = formDate,
                                onValueChange = { viewModel.formDateString.value = it },
                                label = { Text("Date (yyyy-mm-dd)") },
                                modifier = Modifier.weight(1f).testTag("form_date"),
                                leadingIcon = { Icon(Icons.Default.DateRange, contentDescription = null, tint = SafetyAmber) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SafetyAmber,
                                    unfocusedBorderColor = CardBorder,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight,
                                    focusedLabelColor = SafetyAmber,
                                    unfocusedLabelColor = TextLight.copy(alpha = 0.6f)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = formSite,
                                onValueChange = { viewModel.formSiteName.value = it },
                                label = { Text("Site Location") },
                                modifier = Modifier.weight(1f).testTag("form_site_name"),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SafetyAmber,
                                    unfocusedBorderColor = CardBorder,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight,
                                    focusedLabelColor = SafetyAmber,
                                    unfocusedLabelColor = TextLight.copy(alpha = 0.6f)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = formStart,
                                onValueChange = { viewModel.formStartTime.value = it },
                                label = { Text("Start Time (24h)") },
                                placeholder = { Text("08:00") },
                                modifier = Modifier.weight(1f).testTag("form_start_time"),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SafetyAmber,
                                    unfocusedBorderColor = CardBorder,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight,
                                    focusedLabelColor = SafetyAmber,
                                    unfocusedLabelColor = TextLight.copy(alpha = 0.6f)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = formEnd,
                                onValueChange = { viewModel.formEndTime.value = it },
                                label = { Text("End Time (24h)") },
                                placeholder = { Text("17:00") },
                                modifier = Modifier.weight(1f).testTag("form_end_time"),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SafetyAmber,
                                    unfocusedBorderColor = CardBorder,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight,
                                    focusedLabelColor = SafetyAmber,
                                    unfocusedLabelColor = TextLight.copy(alpha = 0.6f)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = formBreak,
                                onValueChange = { viewModel.formBreakMinutes.value = it },
                                label = { Text("Break (Mins)") },
                                modifier = Modifier.weight(1f).testTag("form_break_mins"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SafetyAmber,
                                    unfocusedBorderColor = CardBorder,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight,
                                    focusedLabelColor = SafetyAmber,
                                    unfocusedLabelColor = TextLight.copy(alpha = 0.6f)
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = formRate,
                                onValueChange = { viewModel.formHourlyRate.value = it },
                                label = { Text("Hourly Rate ($)") },
                                modifier = Modifier.weight(1f).testTag("form_hourly_rate"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SafetyAmber,
                                    unfocusedBorderColor = CardBorder,
                                    focusedTextColor = TextLight,
                                    unfocusedTextColor = TextLight,
                                    focusedLabelColor = SafetyAmber,
                                    unfocusedLabelColor = TextLight.copy(alpha = 0.6f)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = formActivities,
                            onValueChange = { viewModel.formActivitiesText.value = it },
                            label = { Text("Activity Details / Logs") },
                            modifier = Modifier.fillMaxWidth().height(90.dp).testTag("form_activities"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SafetyAmber,
                                unfocusedBorderColor = CardBorder,
                                focusedTextColor = TextLight,
                                unfocusedTextColor = TextLight,
                                focusedLabelColor = SafetyAmber,
                                unfocusedLabelColor = TextLight.copy(alpha = 0.6f)
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Live Calculations Panel inside Form
                        val simulatedLog = remember(formWorker, formDate, formStart, formEnd, formBreak, formRate, formActivities, formSite) {
                            SiteLog(
                                workerName = formWorker,
                                dateString = formDate,
                                startTime = formStart,
                                endTime = formEnd,
                                breakMinutes = formBreak.toIntOrNull() ?: 0,
                                hourlyRate = formRate.toDoubleOrNull() ?: 35.0,
                                activitiesText = formActivities,
                                siteName = formSite
                            )
                        }

                        LiveCalculationStats(simulatedLog)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { viewModel.resetForm(); if (showPreview) viewModel.setInputText("") },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextLight),
                                modifier = Modifier.weight(1f).height(48.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, CardBorder)
                            ) {
                                Text("Clear", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.saveTimesheetEntry() },
                                colors = ButtonDefaults.buttonColors(containerColor = if (showPreview) SuccessTeal else SafetyAmber),
                                modifier = Modifier.weight(1.5f).height(48.dp).testTag("save_timesheet_button"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = TextDark)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (editingLogId != null) "Update Timesheet" else "Save Timesheet",
                                        color = TextDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiveCalculationStats(log: SiteLog) {
    val grossHrs = log.getGrossHours()
    val netHrs = log.getNetHours()
    val regHrs = log.getRegularHours()
    val otHrs = log.getOvertimeHours()
    val totalPay = log.getTotalPay()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF161B24), RoundedCornerShape(8.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                "LIVE TIMESHEET CALCULATIONS",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SafetyAmber,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Gross Shift:", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                    Text(String.format(Locale.getDefault(), "%.2f hrs", grossHrs), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextLight)
                }
                Column {
                    Text("Unpaid Break:", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                    Text("${log.breakMinutes} mins", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextLight)
                }
                Column {
                    Text("Net Work Hours:", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                    Text(String.format(Locale.getDefault(), "%.2f hrs", netHrs), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SafetyAmber)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = CardBorder.copy(alpha = 0.5f), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Regular Hours: ", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                        Text(String.format(Locale.getDefault(), "%.2f hrs ($%.2f/hr)", regHrs, log.hourlyRate), fontSize = 11.sp, color = TextLight)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Overtime Hours (x1.5):", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                        Text(String.format(Locale.getDefault(), "%.2f hrs ($%.2f/hr)", otHrs, log.hourlyRate * 1.5), fontSize = 11.sp, color = SafetyAmberLight)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SafetyAmber.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ESTIMATED GROSS EARNINGS:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SafetyAmber)
                Text(
                    text = String.format(Locale.getDefault(), "$%.2f", totalPay),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = SafetyAmber
                )
            }
        }
    }
}

@Composable
fun SavedLogsScreen(viewModel: TimesheetViewModel) {
    val allLogs by viewModel.allLogs.collectAsState()
    val workerFilter by viewModel.workerFilter.collectAsState()
    val siteFilter by viewModel.siteFilter.collectAsState()

    val filteredLogs = remember(allLogs, workerFilter, siteFilter) {
        allLogs.filter {
            it.workerName.contains(workerFilter, ignoreCase = true) &&
            it.siteName.contains(siteFilter, ignoreCase = true)
        }
    }

    // Overall summary statistics
    val totalHours = filteredLogs.sumOf { it.getNetHours() }
    val totalOT = filteredLogs.sumOf { it.getOvertimeHours() }
    val totalEarnings = filteredLogs.sumOf { it.getTotalPay() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Stats Card
        Card(
            colors = CardDefaults.cardColors(containerColor = SteelSurface),
            border = BorderStroke(1.dp, CardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "LATEST LOG SUMMARY",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SafetyAmber,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Entries", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                        Text("${filteredLogs.size}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextLight)
                    }
                    Column {
                        Text("Total Hours", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                        Text(String.format(Locale.getDefault(), "%.2f h", totalHours), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextLight)
                    }
                    Column {
                        Text("Total OT", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                        Text(String.format(Locale.getDefault(), "%.2f h", totalOT), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SafetyAmberLight)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Est. Gross Pay", fontSize = 11.sp, color = TextLight.copy(alpha = 0.6f))
                        Text(String.format(Locale.getDefault(), "$%.2f", totalEarnings), fontSize = 16.sp, fontWeight = FontWeight.Black, color = SafetyAmber)
                    }
                }
            }
        }

        // Search Filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = workerFilter,
                onValueChange = { viewModel.setWorkerFilter(it) },
                placeholder = { Text("Filter by worker...", fontSize = 12.sp, color = TextLight.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SafetyAmber, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.weight(1f).height(50.dp).testTag("filter_worker_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SafetyAmber,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextLight,
                    unfocusedTextColor = TextLight
                )
            )
            OutlinedTextField(
                value = siteFilter,
                onValueChange = { viewModel.setSiteFilter(it) },
                placeholder = { Text("Filter by site...", fontSize = 12.sp, color = TextLight.copy(alpha = 0.5f)) },
                modifier = Modifier.weight(1f).height(50.dp).testTag("filter_site_input"),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SafetyAmber,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextLight,
                    unfocusedTextColor = TextLight
                )
            )
        }

        // Logs list
        if (filteredLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = TextLight.copy(alpha = 0.2f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No site logs recorded yet.",
                        color = TextLight.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Use the AI Parser to format and save a site log!",
                        color = TextLight.copy(alpha = 0.3f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredLogs) { log ->
                    SiteLogCard(
                        log = log,
                        onEdit = { viewModel.startEditingLog(log) },
                        onDelete = { viewModel.deleteLog(log) }
                    )
                }
            }
        }
    }
}

@Composable
fun SiteLogCard(
    log: SiteLog,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = SteelSurface),
        border = BorderStroke(1.dp, CardBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("log_card_${log.id}")
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = log.workerName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyAmber,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (log.isParsedByAi) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(SuccessTeal.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SuccessTeal, modifier = Modifier.size(10.dp))
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text("AI", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = SuccessTeal)
                                }
                            }
                        }
                    }
                    Text(
                        text = "${log.getFormattedDate()} • ${log.siteName}",
                        fontSize = 12.sp,
                        color = TextLight.copy(alpha = 0.6f)
                    )
                }
                
                Text(
                    text = String.format(Locale.getDefault(), "$%.2f", log.getTotalPay()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = SafetyAmber
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column {
                        Text("Shift", fontSize = 9.sp, color = TextLight.copy(alpha = 0.5f))
                        Text("${log.startTime} - ${log.endTime}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextLight)
                    }
                    Column {
                        Text("Break", fontSize = 9.sp, color = TextLight.copy(alpha = 0.5f))
                        Text("${log.breakMinutes}m", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextLight)
                    }
                    Column {
                        Text("Net Hrs", fontSize = 9.sp, color = TextLight.copy(alpha = 0.5f))
                        Text(String.format(Locale.getDefault(), "%.2f h", log.getNetHours()), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SafetyAmber)
                    }
                    if (log.getOvertimeHours() > 0) {
                        Column {
                            Text("OT Hrs", fontSize = 9.sp, color = TextLight.copy(alpha = 0.5f))
                            Text(String.format(Locale.getDefault(), "%.2f h", log.getOvertimeHours()), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SafetyAmberLight)
                        }
                    }
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(36.dp).testTag("edit_button_${log.id}")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextLight.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp).testTag("delete_button_${log.id}")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ErrorRed.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                    }
                }
            }

            // Expanded Activities List
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = CardBorder.copy(alpha = 0.4f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Extracted Site Activities:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SafetyAmberLight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (log.activitiesText.isNotBlank()) log.activitiesText else "No activity description added.",
                        fontSize = 12.sp,
                        color = TextLight.copy(alpha = 0.8f),
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Click to collapse ", fontSize = 10.sp, color = TextLight.copy(alpha = 0.4f))
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, tint = TextLight.copy(alpha = 0.4f), modifier = Modifier.size(14.dp))
                    }
                }
            }
            if (!expanded) {
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Click to view log details ", fontSize = 10.sp, color = TextLight.copy(alpha = 0.4f))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = TextLight.copy(alpha = 0.4f), modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}

fun generateJsonData(workerName: String, logs: List<SiteLog>): String {
    val workerLogs = logs.filter { it.workerName.equals(workerName, ignoreCase = true) }
        .sortedBy { it.dateString }
    val array = JSONArray()
    for (log in workerLogs) {
        val obj = JSONObject().apply {
            put("id", log.id)
            put("workerName", log.workerName)
            put("dateString", log.dateString)
            put("startTime", log.startTime)
            put("endTime", log.endTime)
            put("breakMinutes", log.breakMinutes)
            put("hourlyRate", log.hourlyRate)
            put("activitiesText", log.activitiesText)
            put("siteName", log.siteName)
            put("grossHours", log.getGrossHours())
            put("netHours", log.getNetHours())
            put("regularHours", log.getRegularHours())
            put("overtimeHours", log.getOvertimeHours())
            put("regularPay", log.getRegularPay())
            put("overtimePay", log.getOvertimePay())
            put("totalPay", log.getTotalPay())
        }
        array.put(obj)
    }
    return array.toString(2)
}

fun generateCsvData(workerName: String, logs: List<SiteLog>): String {
    val workerLogs = logs.filter { it.workerName.equals(workerName, ignoreCase = true) }
        .sortedBy { it.dateString }
    val sb = StringBuilder()
    // Headers
    sb.append("Date,Site Location,Worker Name,Start Time,End Time,Break Minutes,Hourly Rate,Gross Hours,Net Hours,Regular Hours,Overtime Hours,Regular Pay,Overtime Pay,Total Pay,Activities\n")
    for (log in workerLogs) {
        // Escape quotes in activities and site names
        val escapedSite = log.siteName.replace("\"", "\"\"")
        val escapedActivities = log.activitiesText.replace("\"", "\"\"")
        sb.append("\"${log.dateString}\",")
        sb.append("\"$escapedSite\",")
        sb.append("\"${log.workerName}\",")
        sb.append("\"${log.startTime}\",")
        sb.append("\"${log.endTime}\",")
        sb.append("${log.breakMinutes},")
        sb.append("${log.hourlyRate},")
        sb.append(String.format(Locale.US, "%.2f,", log.getGrossHours()))
        sb.append(String.format(Locale.US, "%.2f,", log.getNetHours()))
        sb.append(String.format(Locale.US, "%.2f,", log.getRegularHours()))
        sb.append(String.format(Locale.US, "%.2f,", log.getOvertimeHours()))
        sb.append(String.format(Locale.US, "%.2f,", log.getRegularPay()))
        sb.append(String.format(Locale.US, "%.2f,", log.getOvertimePay()))
        sb.append(String.format(Locale.US, "%.2f,", log.getTotalPay()))
        sb.append("\"$escapedActivities\"\n")
    }
    return sb.toString()
}

@Composable
fun PayrollReportScreen(viewModel: TimesheetViewModel) {
    val allLogs by viewModel.allLogs.collectAsState()
    val selectedWorker by viewModel.selectedWorkerForReport.collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val createJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            try {
                val data = generateJsonData(selectedWorker, allLogs)
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(data.toByteArray())
                    Toast.makeText(context, "JSON exported successfully!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    val createCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            try {
                val data = generateCsvData(selectedWorker, allLogs)
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(data.toByteArray())
                    Toast.makeText(context, "CSV exported successfully!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Extract unique workers
    val uniqueWorkers = remember(allLogs) {
        allLogs.map { it.workerName }.distinct().sortedBy { it }
    }

    // Set default selected worker if empty
    remember(uniqueWorkers) {
        if (selectedWorker.isEmpty() && uniqueWorkers.isNotEmpty()) {
            viewModel.setSelectedWorkerForReport(uniqueWorkers.first())
        }
        null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SteelSurface),
            border = BorderStroke(1.dp, CardBorder),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "SELECT WORKER FOR TIMESHEET",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SafetyAmber,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                var dropdownExpanded by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SteelDarkBg, RoundedCornerShape(8.dp))
                        .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                        .clickable { dropdownExpanded = true }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedWorker.isNotEmpty()) selectedWorker else "No Workers Recorded Yet",
                            color = if (selectedWorker.isNotEmpty()) TextLight else TextLight.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = SafetyAmber
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .background(SteelSurface)
                            .border(1.dp, CardBorder)
                    ) {
                        if (uniqueWorkers.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("No logs found. Save a log first.", color = TextLight) },
                                onClick = { dropdownExpanded = false }
                            )
                        } else {
                            uniqueWorkers.forEach { workerName ->
                                DropdownMenuItem(
                                    text = { Text(workerName, color = TextLight, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        viewModel.setSelectedWorkerForReport(workerName)
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (selectedWorker.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = TextLight.copy(alpha = 0.2f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No timesheet entries available.",
                        color = TextLight.copy(alpha = 0.5f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Add and save daily logs to generate structured timesheets.",
                        color = TextLight.copy(alpha = 0.3f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        } else {
            val reportText = remember(selectedWorker, allLogs) {
                viewModel.generateTimesheetReportText(selectedWorker, allLogs)
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = SteelSurface),
                border = BorderStroke(1.dp, CardBorder),
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "FORMATTED TIMESHEET REPORT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SafetyAmber
                        )
                        Box(
                            modifier = Modifier
                                .background(SafetyAmber.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("CALCULATED OK", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = SafetyAmber)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    // Text report panel with monospace formatting
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(Color(0xFF0D1117), RoundedCornerShape(6.dp))
                            .border(1.dp, CardBorder, RoundedCornerShape(6.dp))
                            .padding(8.dp)
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                Text(
                                    text = reportText,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 11.sp,
                                    color = Color(0xFF8FF0A4), // gorgeous retro terminals/billing style
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                     Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(reportText))
                                Toast.makeText(context, "Timesheet copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextLight),
                            modifier = Modifier.weight(1f).height(46.dp).testTag("copy_timesheet_button"),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, CardBorder)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Copy Text", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        var exportDropdownExpanded by remember { mutableStateOf(false) }

                        Box(modifier = Modifier.weight(1.1f)) {
                            OutlinedButton(
                                onClick = { exportDropdownExpanded = true },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SafetyAmber),
                                modifier = Modifier.fillMaxWidth().height(46.dp).testTag("export_timesheet_button"),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, SafetyAmber.copy(alpha = 0.5f))
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Export", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(14.dp))
                                }
                            }

                            DropdownMenu(
                                expanded = exportDropdownExpanded,
                                onDismissRequest = { exportDropdownExpanded = false },
                                modifier = Modifier
                                    .background(SteelSurface)
                                    .border(1.dp, CardBorder)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Export as CSV (Excel)", color = TextLight) },
                                    onClick = {
                                        exportDropdownExpanded = false
                                        val formattedName = selectedWorker.replace(" ", "_").lowercase()
                                        try {
                                            createCsvLauncher.launch("timesheet_${formattedName}.csv")
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Export error: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Export as JSON", color = TextLight) },
                                    onClick = {
                                        exportDropdownExpanded = false
                                        val formattedName = selectedWorker.replace(" ", "_").lowercase()
                                        try {
                                            createJsonLauncher.launch("timesheet_${formattedName}.json")
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Export error: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                            }
                        }

                        Button(
                            onClick = {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, reportText)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Timesheet")
                                context.startActivity(shareIntent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SafetyAmber),
                            modifier = Modifier.weight(1.1f).height(46.dp).testTag("share_timesheet_button"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Share, contentDescription = null, tint = TextDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Share", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Simple wrapper class to avoid needing XML-based canvas resource definitions for borders
@Composable
fun BorderStroke(width: androidx.compose.ui.unit.Dp, color: Color) = remember(width, color) {
    androidx.compose.foundation.BorderStroke(width, color)
}
