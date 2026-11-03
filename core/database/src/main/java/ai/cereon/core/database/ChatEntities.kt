package ai.cereon.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(@PrimaryKey val id: String, val title: String, val model: String, val createdAt: Long, val updatedAt: Long, val pinned: Boolean = false, val archived: Boolean = false)

@Entity(tableName = "messages")
data class MessageEntity(@PrimaryKey val id: String, val conversationId: String, val role: String, val content: String, val createdAt: Long, val status: String = "complete")
