package ai.cereon.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ConversationEntity::class, MessageEntity::class], version = 1, exportSchema = true)
abstract class CereonDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    companion object { fun create(context: Context): CereonDatabase = Room.databaseBuilder(context, CereonDatabase::class.java, "cereon.db").build() }
}
