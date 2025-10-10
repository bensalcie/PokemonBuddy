package bensalcie.app.pokmonbuddy.ui.theme

import android.app.Activity
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

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)


// ---------------- Light Theme ----------------
private val LightBackground = Color(0xFFEFF4F4)
private val LightSurface = Color(0xFFFFFFFF)
private val LightCard = Color(0xFFD6F0E6)
private val LightTextPrimary = Color(0xFF1C1C1E)
private val LightTextSecondary = Color(0xFF7A7A7A)
private val LightAccent = Color(0xFF3A7C74)

// ---------------- Dark Theme ----------------
private val DarkBackground = Color(0xFF0F1414)
private val DarkSurface = Color(0xFF1A1E1E)
private val DarkCard = Color(0xFF243131)
private val DarkTextPrimary = Color(0xFFF4F4F4)
private val DarkTextSecondary = Color(0xFFB5B5B5)
private val DarkAccent = Color(0xFF5EEAD4)


private val LightColorScheme = lightColorScheme(
    primary = LightAccent,
    onPrimary = Color.White,
    background = LightBackground,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    onBackground = LightTextPrimary,
    secondary = LightCard,
    onSecondary = LightTextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkAccent,
    onPrimary = Color.Black,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    onBackground = DarkTextPrimary,
    secondary = DarkCard,
    onSecondary = DarkTextPrimary
)

@Composable
fun PokémonBuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}