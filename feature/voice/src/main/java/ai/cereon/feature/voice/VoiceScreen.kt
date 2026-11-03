package ai.cereon.feature.voice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Feature entry point. Domain operations are added with their service contracts. */
@Composable
fun VoiceScreen() {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Voice", style = MaterialTheme.typography.headlineMedium)
        Text("Voice interaction requires an explicit microphone permission and a configured speech service.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
