package ai.cereon.feature.create

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
fun CreateScreen() {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Create", style = MaterialTheme.typography.headlineMedium)
        Text("Generate and organize writing, code, summaries and reusable prompt templates.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
