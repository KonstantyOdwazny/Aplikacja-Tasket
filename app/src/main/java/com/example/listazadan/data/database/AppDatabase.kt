package com.example.listazadan.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.listazadan.data.models.Group
import com.example.listazadan.data.models.Task

//@Database(entities = [Task::class], version = 2) // Migracja z version 1 na 2
@Database(entities = [Task::class, Group::class], version = 3) // Zaktualizowana wersja bazy danych
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun groupDao(): GroupDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE tasks ADD COLUMN date TEXT")
            }
        }
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Tworzenie nowej tabeli 'groups'
                db.execSQL("CREATE TABLE IF NOT EXISTS `groups` (`groupId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL)")
                // Dodanie kolumny 'groupId' do istniejącej tabeli 'tasks'
                db.execSQL("ALTER TABLE tasks ADD COLUMN groupId INTEGER NOT NULL DEFAULT 0 REFERENCES groups(groupId) ON DELETE CASCADE")
            }
        }
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_database"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
