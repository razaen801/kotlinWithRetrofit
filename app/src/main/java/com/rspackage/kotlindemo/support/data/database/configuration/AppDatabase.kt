package com.rspackage.kotlindemo.support.data.database.configuration

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rspackage.kotlindemo.support.data.database.entity.DBUser

@Database(
    entities = [DBUser::class],
    version = DatabaseConfigs.databaseVersion
)

abstract class AppDatabase : RoomDatabase() {


    //abstract fun getDBUserDao(): DBUserDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DatabaseConfigs.databaseName
            ).build()

        fun clearDatabase(): Unit? {
            return instance?.clearAllTables()
        }
    }
}