package ai.cereon.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM conversations WHERE archived = 0 ORDER BY pinned DESC, updatedAt DESC") fun observeConversations(): Flow<List<ConversationEntity>>
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY createdAt ASC") fun observeMessages(conversationId: String): Flow<List<MessageEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun putConversation(conversation: ConversationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun putMessage(message: MessageEntity)
    @Query("UPDATE conversations SET title = :title, updatedAt = :updatedAt WHERE id = :id") suspend fun renameConversation(id: String, title: String, updatedAt: Long)
    @Query("UPDATE conversations SET pinned = :pinned WHERE id = :id") suspend fun setPinned(id: String, pinned: Boolean)
    @Query("UPDATE conversations SET archived = :archived WHERE id = :id") suspend fun setArchived(id: String, archived: Boolean)
    @Query("DELETE FROM messages WHERE conversationId = :id") suspend fun deleteMessages(id: String)
    @Query("DELETE FROM conversations WHERE id = :id") suspend fun deleteConversation(id: String)
}
