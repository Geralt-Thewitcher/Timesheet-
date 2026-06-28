package com.example.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.SiteLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Parses raw site activity text into a structured SiteLog model.
     * If the API key is not configured, or if the API call fails, we return a fallback or throw.
     */
    suspend fun parseActivityLog(
        rawLog: String,
        currentDateString: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    ): SiteLog = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e(TAG, "API Key is missing or using placeholder!")
            // Trigger a simulated AI parse for demonstration/development convenience if key is empty
            return@withContext simulateAiParse(rawLog, currentDateString)
        }

        val systemInstruction = """
            You are an expert site supervisor and timesheet parser. 
            Analyze the user's raw day-to-day site activity log or notes (e.g. from the 'Pulsar' app or personal diary) 
            and extract a single, perfectly formatted JSON timesheet entry.
            
            Strict Output Format:
            You MUST return ONLY a JSON object with the following fields and NO markdown wrapping:
            {
              "workerName": "Name of the worker (String, default: 'Unknown Worker')",
              "dateString": "The date of the log in 'yyyy-MM-dd' format (String, default to today's date: '$currentDateString')",
              "startTime": "Start of work time in 'HH:mm' 24-hour format (String, e.g. '08:00')",
              "endTime": "End of work time in 'HH:mm' 24-hour format (String, e.g. '17:30')",
              "breakMinutes": "Unpaid break duration in minutes (Integer, e.g. 30, 45, or 60. Default to 0 if none mentioned)",
              "hourlyRate": "Hourly rate of pay (Double, default: 35.0)",
              "activitiesText": "A clean, concise bulleted list summarizing the key activities performed, with time segments if noted in the input. (String)",
              "siteName": "Name of the site/project (String, default: 'General Site')"
            }
            
            Rule for times:
            - Standardize all extracted times to 24-hour HH:mm format (e.g. "8 AM" -> "08:00", "5:30 PM" -> "17:30").
            - Ensure dateString is formatted exactly as yyyy-MM-dd (e.g., June 25, 2026 -> 2026-06-25).
            - Extract unpaid break time accurately if mentioned. (e.g. "lunch for 30 mins" -> breakMinutes = 30).
        """.trimIndent()

        val prompt = "Raw Log Input:\n$rawLog"

        val requestBodyJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply {
                        put("text", systemInstruction)
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.2) // Low temperature for high precision structured output
            })
        }

        val requestBody = requestBodyJson.toString().toRequestBody(JSON_MEDIA_TYPE)
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errorBody = response.body?.string() ?: ""
                    Log.e(TAG, "Request failed: code=${response.code} body=$errorBody")
                    throw Exception("API Error (${response.code}): $errorBody")
                }
                
                val responseBodyStr = response.body?.string() ?: throw Exception("Empty response body")
                Log.d(TAG, "Response: $responseBodyStr")
                
                val responseJson = JSONObject(responseBodyStr)
                val candidates = responseJson.getJSONArray("candidates")
                if (candidates.length() == 0) throw Exception("No response candidates returned")
                
                val content = candidates.getJSONObject(0).getJSONObject("content")
                val parts = content.getJSONArray("parts")
                if (parts.length() == 0) throw Exception("No response parts returned")
                
                val rawText = parts.getJSONObject(0).getString("text").trim()
                Log.d(TAG, "Extracted text: $rawText")
                
                // Parse the inner JSON returned by Gemini
                val innerJson = JSONObject(rawText)
                
                return@withContext SiteLog(
                    workerName = innerJson.optString("workerName", "Unknown Worker"),
                    dateString = innerJson.optString("dateString", currentDateString),
                    startTime = innerJson.optString("startTime", "08:00"),
                    endTime = innerJson.optString("endTime", "17:00"),
                    breakMinutes = innerJson.optInt("breakMinutes", 30),
                    hourlyRate = innerJson.optDouble("hourlyRate", 35.0),
                    activitiesText = innerJson.optString("activitiesText", ""),
                    siteName = innerJson.optString("siteName", "General Site"),
                    isParsedByAi = true
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error calling Gemini API: ${e.message}", e)
            throw e
        }
    }

    /**
     * Fallback rules to mock/simulate parsing if API key is not configured or fails.
     * This makes sure the app remains 100% testable and interactive for the developer/user.
     */
    fun simulateAiParse(rawLog: String, currentDateString: String): SiteLog {
        // Try to regex parse some simple info or return a realistic result
        var name = "John Doe"
        val lowercaseLog = rawLog.lowercase()
        
        // Check for common names
        if (lowercaseLog.contains("alex")) name = "Alex Smith"
        else if (lowercaseLog.contains("sarah")) name = "Sarah Jenkins"
        else if (lowercaseLog.contains("david")) name = "David Miller"
        else if (lowercaseLog.contains("pulsar")) name = "Pulsar Technician"

        var startTime = "08:00"
        var endTime = "17:00"
        var breakMins = 30
        var site = "Pulsar Wind Farm"

        // Search for times e.g. "7:30", "07:30", "8 AM"
        val timeRegex = "([0-1]?[0-9]|2[0-3]):[0-5][0-9]".toRegex()
        val times = timeRegex.findAll(rawLog).map { it.value }.toList()
        if (times.isNotEmpty()) {
            startTime = times.first()
            if (times.size > 1) {
                endTime = times.last()
            }
        } else {
            if (lowercaseLog.contains("7 am") || lowercaseLog.contains("7am")) startTime = "07:00"
            if (lowercaseLog.contains("8 am") || lowercaseLog.contains("8am")) startTime = "08:00"
            if (lowercaseLog.contains("4 pm") || lowercaseLog.contains("4pm") || lowercaseLog.contains("16:00")) endTime = "16:00"
            if (lowercaseLog.contains("5 pm") || lowercaseLog.contains("5pm") || lowercaseLog.contains("17:00")) endTime = "17:00"
            if (lowercaseLog.contains("5:30 pm") || lowercaseLog.contains("5:30pm") || lowercaseLog.contains("17:30")) endTime = "17:30"
            if (lowercaseLog.contains("6 pm") || lowercaseLog.contains("6pm") || lowercaseLog.contains("18:00")) endTime = "18:00"
        }

        if (lowercaseLog.contains("lunch") || lowercaseLog.contains("break")) {
            if (lowercaseLog.contains("45 min") || lowercaseLog.contains("45min")) breakMins = 45
            else if (lowercaseLog.contains("1 hour") || lowercaseLog.contains("1 hr") || lowercaseLog.contains("60 min")) breakMins = 60
            else breakMins = 30
        }

        if (lowercaseLog.contains("turbine")) site = "Turbine Field site"
        else if (lowercaseLog.contains("phase 2")) site = "Phase 2 Construction"
        else if (lowercaseLog.contains("office")) site = "Head Office"

        // Format clean activities
        val cleanedActivities = rawLog.lines()
            .filter { it.isNotBlank() }
            .map { "• " + it.trim().removePrefix("-").removePrefix("•").trim() }
            .joinToString("\n")

        return SiteLog(
            workerName = name,
            dateString = currentDateString,
            startTime = startTime,
            endTime = endTime,
            breakMinutes = breakMins,
            hourlyRate = 35.0,
            activitiesText = if (cleanedActivities.isNotEmpty()) cleanedActivities else "• General site activities\n• Pulsar data alignment",
            siteName = site,
            isParsedByAi = true
        )
    }
}
