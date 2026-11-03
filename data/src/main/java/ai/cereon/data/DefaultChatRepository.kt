package ai.cereon.data

import ai.cereon.core.database.ChatDao
import ai.cereon.core.database.ConversationEntity
import ai.cereon.core.database.MessageEntity
import ai.cereon.core.network.ChatTurn
import ai.cereon.core.network.OpenAiCompatibleClient
import ai.cereon.core.security.SecretStore
import ai.cereon.domain.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.collect
import java.util.UUID

data class ProviderConfig(val id: String, val name: String, val baseUrl: String, val model: String, val enabled: Boolean = true)

class DefaultChatRepository(private val dao: ChatDao, private val client: OpenAiCompatibleClient, private val secrets: SecretStore, private val config: () -> ProviderConfig?) : ChatRepository {
    override fun conversations(): Flow<List<ConversationEntity>> = dao.observeConversations()
    override fun messages(conversationId: String): Flow<List<MessageEntity>> = dao.observeMessages(conversationId)
    override suspend fun newConversation(model: String): String { val id = UUID.randomUUID().toString(); val now = System.currentTimeMillis(); dao.putConversation(ConversationEntity(id, "New conversation", model, now, now)); return id }
    override suspend fun deleteConversation(id: String) { dao.deleteMessages(id); dao.deleteConversation(id) }
    override suspend fun send(conversationId: String, prompt: String, onDelta: suspend (String) -> Unit) {
        val provider = config()?.takeIf { it.enabled } ?: error("Add and enable a provider in Models first")
        val key = secrets.get("provider:${provider.id}")?.takeIf { it.isNotBlank() } ?: error("API key is missing for ${provider.name}")
        val existing = dao.observeMessages(conversationId).first(); val now = System.currentTimeMillis()
        dao.putMessage(MessageEntity(UUID.randomUUID().toString(), conversationId, "user", prompt, now))
        val assistantId = UUID.randomUUID().toString(); val response = StringBuilder()
        dao.putMessage(MessageEntity(assistantId, conversationId, "assistant", "", now + 1, "streaming"))
        val history = (existing + MessageEntity("pending", conversationId, "user", prompt, now)).map { ChatTurn(it.role, it.content) }
        client.stream(provider.baseUrl, key, provider.model, history).collect { token -> response.append(token); onDelta(token) }
        dao.putMessage(MessageEntity(assistantId, conversationId, "assistant", response.toString(), now + 1, "complete"))
        val title = existing.firstOrNull { it.role == "user" }?.content?.take(48) ?: prompt.take(48)
        dao.putConversation(ConversationEntity(conversationId, title, provider.model, now, System.currentTimeMillis()))
    }
}
