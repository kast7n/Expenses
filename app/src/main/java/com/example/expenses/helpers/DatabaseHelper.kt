package com.example.expenses.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.expenses.models.Expenses

class DatabaseHelper {


    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_NAME = "budgeting.db"
            private const val DATABASE_VERSION = 1


            private const val TABLE_NAME = "expenses"
            private const val COLUMN_ID = "id"
            private const val COLUMN_TITLE = "title"
            private const val COLUMN_AMOUNT = "amount"
            private const val COLUMN_DATE = "date"
            private const val COLUMN_DESCRIPTION = "description"
            private const val COLUMN_CATEGORY = "category"

        }

        override fun onCreate(db: SQLiteDatabase) {
            val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_AMOUNT REAL,
                $COLUMN_DATE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_CATEGORY TEXT
            )
        """.trimIndent()
            db.execSQL(createTable)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        fun insertExpense(expense: Expenses): Long {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, expense.title)
                put(COLUMN_AMOUNT, expense.amount)
                put(COLUMN_DATE, expense.date)
                put(COLUMN_DESCRIPTION, expense.description)
                put(COLUMN_CATEGORY, expense.category)
            }
            return db.insert(TABLE_NAME, null, values)
        }

        fun clearDatabase() {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, null, null)
            db.close()
        }

        @SuppressLint("Range")
        fun getAllExpenses(): List<Expenses> {
            val expenses = mutableListOf<Expenses>()
            val db = this.readableDatabase
            val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getString(it.getColumnIndex(COLUMN_ID))
                    val title = it.getString(it.getColumnIndex(COLUMN_TITLE))
                    val amount = it.getString(it.getColumnIndex(COLUMN_AMOUNT))
                    val date = it.getString(it.getColumnIndex(COLUMN_DATE))
                    val description = it.getString(it.getColumnIndex(COLUMN_DESCRIPTION))
                    val category = it.getString(it.getColumnIndex(COLUMN_CATEGORY))
                    expenses.add(Expenses(id = id, title = title, amount = amount.toDouble(), date = date,description = description,category = category))
                }
            }
            db.close()
            return expenses
        }

        fun deleteExpense(id: String) {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id))
            db.close()
        }
    }


}