package com.example.expenses.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Budget(
    val id: String,
    val title: String,
    val amount: Double,
    val type: BudgetType,
    val category: String,
    val description: String,
    val date: String = getCurrDate(),
    val time: String = getCurrTime()
)

fun getCurrDate() : String  = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
fun getCurrTime() : String  = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

