package com.tuanvu.quanlichitieu.future.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "table_expense",
)
data class TableExpense(
    @PrimaryKey(autoGenerate = true)
    var expense_id: Long = 0,
    var user_id: Long = 0,
    var category_id: Long = 0,
    var amount:Float,
    var date: String,
    var description: String,
    var status: String,
    ):Serializable

/*
-- Bảng Khoản Chi (Expenses)
CREATE TABLE Expenses (
    expense_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    category_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    description TEXT,
    status ENUM('Paid', 'Unpaid') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id),
    FOREIGN KEY (category_id) REFERENCES ExpenseCategories(category_id)
);*/
