package com.example.expenses.models

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Expenses(val id: String, val title: String, val amount: Double, val date: String = getCurrDate(), val description: String, val category: String)


fun getCurrDate() : String  = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()) + "-" + SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

