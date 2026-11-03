package ai.cereon.feature.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.cereon.core.database.ChatDao

@Composable
fun HistoryScreen(dao: ChatDao) {
    val conversations by dao.observeConversations().collectAsState(initial = emptyList())
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("History", style = MaterialTheme.typography.headlineMedium)
        if (conversations.isEmpty()) Text("Your conversations will appear here.", modifier = Modifier.padding(top = 24.dp))
        LazyColumn {
            items(conversations, key = { it.id }) { item ->
                ListItem(headlineContent = { Text(item.title) }, supportingContent = { Text(item.model) })
            }
        }
    }
}
