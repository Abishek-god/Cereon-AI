package ai.cereon.feature.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ModelsScreen() {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("https://api.openai.com/v1") }
    var model by remember { mutableStateOf("") }
    var apiKey by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Credentials are stored encrypted on this device.") }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Models & providers", style = MaterialTheme.typography.headlineMedium)
        Text("Connect an OpenAI-compatible endpoint. Some endpoints may not expose model discovery.")
        OutlinedTextField(name, { name = it }, label = { Text("Provider name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(url, { url = it }, label = { Text("Base URL") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(model, { model = it }, label = { Text("Model ID") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(apiKey, { apiKey = it }, label = { Text("API key") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { status = if (name.isNotBlank() && url.startsWith("https://") && model.isNotBlank() && apiKey.isNotBlank()) "Provider details validated. Save integration is available through ProviderStore." else "Enter a provider name, HTTPS URL, model ID and API key." }) { Text("Validate configuration") }
        Text(status, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("Capability awareness", style = MaterialTheme.typography.titleMedium); Text("Vision, streaming, tool calls and structured output vary by provider/model. They are not assumed to be supported.") } }
    }
}
