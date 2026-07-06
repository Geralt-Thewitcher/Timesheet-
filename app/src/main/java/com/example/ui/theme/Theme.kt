package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = AbbRed,
    onPrimary = TextLight,
    primaryContainer = AbbRedDark,
    onPrimaryContainer = TextLight,
    secondary = IndustrialGrey,
    onSecondary = TextLight,
    background = IndustrialGreyDark,
    onBackground = TextLight,
    surface = IndustrialGrey,
    onSurface = TextLight,
    error = ErrorColor,
    onError = TextLight
  )

private val LightColorScheme =
  lightColorScheme(
    primary = AbbRed,
    onPrimary = TextLight,
    primaryContainer = AbbRedLight,
    onPrimaryContainer = TextDark,
    secondary = IndustrialGrey,
    onSecondary = TextLight,
    background = LightGrey,
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    error = ErrorColor,
    onError = TextLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
