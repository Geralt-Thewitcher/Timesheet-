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
    primary = SafetyAmber,
    onPrimary = TextDark,
    primaryContainer = SteelGray,
    onPrimaryContainer = TextLight,
    secondary = SafetyAmberLight,
    onSecondary = TextDark,
    background = SteelDarkBg,
    onBackground = TextLight,
    surface = SteelSurface,
    onSurface = TextLight,
    error = ErrorRed,
    onError = TextLight
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SafetyAmber,
    onPrimary = TextDark,
    primaryContainer = SteelGrayLight,
    onPrimaryContainer = TextLight,
    secondary = SafetyAmberLight,
    onSecondary = TextDark,
    background = Color(0xFFF4F6F9),
    onBackground = TextDark,
    surface = Color.White,
    onSurface = TextDark,
    error = ErrorRed,
    onError = TextLight
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // We disable dynamicColor by default to preserve our gorgeous custom brand identity
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
