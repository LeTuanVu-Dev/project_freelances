package com.tuanvu.quanlichitieu.future.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tuanvu.quanlichitieu.future.database.dao.TableUserDao
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import com.tuanvu.quanlichitieu.future.ultis.Constants

@Database(entities = [TableUser::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tableUserDao(): TableUserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.NAME_DATABASE_USER
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
