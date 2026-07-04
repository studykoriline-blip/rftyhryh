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
    primary = Gold500,
    secondary = Gold300,
    tertiary = Gold700,
    background = Navy900,
    surface = Navy800,
    onPrimary = Navy950,
    onSecondary = Navy950,
    onTertiary = Ink50,
    onBackground = Ink50,
    onSurface = Ink50,
    onSurfaceVariant = Ink300,
    surfaceVariant = Navy700,
    error = DangerRed
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Gold500,
    secondary = Gold700,
    tertiary = Gold300,
    background = Ink50,
    surface = Color(0xFFF1EEE4),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Navy950,
    onBackground = Navy950,
    onSurface = Navy950,
    onSurfaceVariant = Ink500,
    surfaceVariant = Color(0xFFE3DFD1),
    error = DangerRed
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
