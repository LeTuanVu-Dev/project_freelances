package com.tuanvu.quanlichitieu.future.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuanvu.quanlichitieu.future.database.entity.TableUser
import kotlinx.coroutines.flow.Flow

@Dao
interface TableUserDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tableUser : TableUser)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGetId(tableUser : TableUser): Long

    @Insert
    suspend fun insertAll(template : List<TableUser>)

    @Query("SELECT * FROM table_user")
    fun getAllUser(): Flow<List<TableUser>>

    @Update
    suspend fun update(tableUser: TableUser)

    @Delete
    suspend fun delete(tableUser: TableUser)
}