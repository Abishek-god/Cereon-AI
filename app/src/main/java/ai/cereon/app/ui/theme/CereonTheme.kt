package ai.cereon.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(primary = Color(0xFF9B8CFF), onPrimary = Color(0xFF21145F), secondary = Color(0xFF72D5C6), background = Color(0xFF101116), surface = Color(0xFF191A22), surfaceVariant = Color(0xFF252632))
private val LightColors = lightColorScheme(primary = Color(0xFF5946C8), secondary = Color(0xFF087F73), background = Color(0xFFF8F7FC), surface = Color(0xFFFFFFFF))

@Composable
fun CereonTheme(darkTheme: Boolean = true, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, content = content)
}
