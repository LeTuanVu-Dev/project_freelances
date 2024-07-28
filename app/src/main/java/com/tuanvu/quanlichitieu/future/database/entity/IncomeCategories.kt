package com.tuanvu.quanlichitieu.future.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "table_income_categories",
)
data class IncomeCategories(
    @PrimaryKey(autoGenerate = true)
    var category_id: Long = 0,
    var name: String,
    ):Serializable

/*
-- Bảng Loại Thu (IncomeCategories)
CREATE TABLE IncomeCategories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);*/
