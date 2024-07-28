package com.tuanvu.quanlichitieu.future.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "table_income",
)
data class Income(
    @PrimaryKey(autoGenerate = true)
    var income_id: Long = 0,
    var user_id: Long = 0,
    var category_id: Long = 0,
    var amount:Float,
    var date: String,
    var description: String,
    var status: String,
    ):Serializable

/*
CREATE TABLE Incomes (
    income_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    category_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    status ENUM('Paid', 'Unpaid') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (category_id) REFERENCES IncomeCategories(category_id)
);*/
