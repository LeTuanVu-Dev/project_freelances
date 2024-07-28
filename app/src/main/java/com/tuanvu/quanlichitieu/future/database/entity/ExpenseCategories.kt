package com.tuanvu.quanlichitieu.future.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "table_expense_categories",
)
data class ExpenseCategories(
    @PrimaryKey(autoGenerate = true)
    var category_id: Long = 0,
    var name: String,
    ):Serializable

/*
--- Bảng Loại Chi (ExpenseCategories)
CREATE TABLE ExpenseCategories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);*/
