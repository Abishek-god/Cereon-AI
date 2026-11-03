package ai.cereon.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.responsive.*
import ai.cereon.app.CereonApplication
import ai.cereon.core.network.OpenAiCompatibleClient
import ai.cereon.core.security.SecretStore
import ai.cereon.data.DefaultChatRepository
import ai.cereon.data.ProviderConfig
import ai.cereon.domain.ChatRepository
import ai.cereon.feature.chat.ChatScreen
import ai.cereon.feature.chat.ChatViewModel
import ai.cereon.feature.history.HistoryScreen
import ai.cereon.feature.models.ModelsScreen
import ai.cereon.feature.agents.AgentsScreen
import ai.cereon.feature.skills.SkillsScreen
import ai.cereon.feature.knowledge.KnowledgeScreen
import ai.cereon.feature.voice.VoiceScreen
import ai.cereon.feature.create.CreateScreen
import ai.cereon.feature.markets.MarketsScreen
import ai.cereon.feature.profile.ProfileScreen
import ai.cereon.feature.settings.SettingsScreen

private enum class Destination(val label: String) {
    Chat("Chat"), History("History"), Models("Models"), Agents("Agents"),
    Skills("Skills"), Knowledge("Knowledge"), Voice("Voice"), Create("Create"),
    Markets("Markets"), Profile("Profile"), Settings("Settings")
}

@Composable
fun CereonRoot() {
    val context = LocalContext.current
    val app = context.applicationContext as CereonApplication
    val secrets = remember { SecretStore(context) }
    val repository = remember {
        DefaultChatRepository(app.database.chatDao(), OpenAiCompatibleClient(), secrets) {
            val prefs = context.getSharedPreferences("cereon_providers", android.content.Context.MODE_PRIVATE)
            val array = org.json.JSONArray(prefs.getString("items", "[]"))
            if (array.length() == 0) null else {
                val p = array.getJSONObject(0)
                ProviderConfig(p.getString("id"), p.getString("name"), p.getString("baseUrl"), p.getString("model"), p.optBoolean("enabled", true))
            }
        }
    }
    val chatViewModel = remember { ChatViewModel(repository) }
    var selected by remember { mutableStateOf(Destination.Chat) }
    val destinations = Destination.entries
    val wide = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp >= 700

    if (wide) {
        RowNavigationLayout(selected, { selected = it }, destinations) { content ->
            Surface(color = MaterialTheme.colorScheme.background) { content() }
        } {
            DestinationContent(selected, chatViewModel, app)
        }
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    listOf(Destination.Chat, Destination.History, Destination.Models, Destination.Settings).forEach { destination ->
                        NavigationBarItem(
                            selected = selected == destination,
                            onClick = { selected = destination },
                            icon = { Icon(destination.icon(), contentDescription = destination.label) },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        ) { padding ->
            Surface(Modifier.padding(padding), color = MaterialTheme.colorScheme.background) {
                DestinationContent(selected, chatViewModel, app)
            }
        }
    }
}

@Composable
private fun RowNavigationLayout(
    selected: Destination,
    onSelect: (Destination) -> Unit,
    destinations: List<Destination>,
    wrapper: @Composable (@Composable () -> Unit) -> Unit,
    content: @Composable () -> Unit
) {
    Row {
        NavigationRail {
            destinations.forEach { destination ->
                NavigationRailItem(
                    selected = selected == destination,
                    onClick = { onSelect(destination) },
                    icon = { Icon(destination.icon(), contentDescription = destination.label) },
                    label = { Text(destination.label) }
                )
            }
        }
        androidx.compose.foundation.layout.Box(Modifier.weight(1f)) { content() }
    }
}

@Composable
private fun DestinationContent(destination: Destination, chat: ChatViewModel, app: CereonApplication) {
    when (destination) {
        Destination.Chat -> ChatScreen(chat)
        Destination.History -> HistoryScreen(app.database.chatDao())
        Destination.Models -> ModelsScreen()
        Destination.Agents -> AgentsScreen()
        Destination.Skills -> SkillsScreen()
        Destination.Knowledge -> KnowledgeScreen()
        Destination.Voice -> VoiceScreen()
        Destination.Create -> CreateScreen()
        Destination.Markets -> MarketsScreen()
        Destination.Profile -> ProfileScreen()
        Destination.Settings -> SettingsScreen()
    }
}

private fun Destination.icon() = when (this) {
    Destination.Chat -> Icons.Default.Chat
    Destination.History -> Icons.Default.History
    Destination.Models -> Icons.Default.Tune
    else -> Icons.Default.Settings
}
