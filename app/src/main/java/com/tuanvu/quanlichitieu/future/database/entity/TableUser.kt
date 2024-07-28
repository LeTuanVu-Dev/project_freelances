package com.tuanvu.quanlichitieu.future.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "table_user",
)
data class TableUser(
    @PrimaryKey(autoGenerate = true)
    var user_id: Long = 0,
    var full_name:String,
    var email: String,
    var gender: String,
    var username: String,
    var password: String,
    )

/*
*  user_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    gender ENUM('Male', 'Female') NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL*/
