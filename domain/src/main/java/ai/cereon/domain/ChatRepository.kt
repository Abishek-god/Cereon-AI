package ai.cereon.domain

import ai.cereon.core.database.ConversationEntity
import ai.cereon.core.database.MessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun conversations(): Flow<List<ConversationEntity>>
    fun messages(conversationId: String): Flow<List<MessageEntity>>
    suspend fun send(conversationId: String, prompt: String, onDelta: suspend (String) -> Unit)
    suspend fun newConversation(model: String): String
    suspend fun deleteConversation(id: String)
}
