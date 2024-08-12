package com.example.expenses.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.expenses.models.BudgetType
import com.example.expenses.models.Budget

class DatabaseHelper {


    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_NAME = "budgeting.db"
            private const val DATABASE_VERSION = 1


            private const val TABLE_NAME = "budgets"
            private const val COLUMN_ID = "id"
            private const val COLUMN_TITLE = "title"
            private const val COLUMN_AMOUNT = "amount"
            private const val COLUMN_DATE = "date"
            private const val COLUMN_DESCRIPTION = "description"
            private const val COLUMN_CATEGORY = "category"
            private const val COLUMN_TIME ="time"
            private const val COLUMN_TYPE = "type"

        }

        override fun onCreate(db: SQLiteDatabase) {
            val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_AMOUNT REAL,
                $COLUMN_DATE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_TIME TEXT,  
                $COLUMN_TYPE TEXT   
            )
        """.trimIndent()
            db.execSQL(createTable)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        fun insertBudget(expense: Budget): Long {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, expense.title)
                put(COLUMN_AMOUNT, expense.amount)
                put(COLUMN_DATE, expense.date)
                put(COLUMN_DESCRIPTION, expense.description)
                put(COLUMN_CATEGORY, expense.category)
                put(COLUMN_TIME, expense.time)
                put(COLUMN_TYPE, expense.type.name)
            }
            return db.insert(TABLE_NAME, null, values)
        }

        fun clearDatabase() {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, null, null)
            db.close()
        }

        @SuppressLint("Range")
        fun getAllBudgets(): List<Budget> {
            val budgets = mutableListOf<Budget>()
            val db = this.readableDatabase
            val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
            cursor?.use {
                while (it.moveToNext()) {
                    val id = it.getString(it.getColumnIndex(COLUMN_ID))
                    val title = it.getString(it.getColumnIndex(COLUMN_TITLE))
                    val amount = it.getDouble(it.getColumnIndex(COLUMN_AMOUNT))
                    val date = it.getString(it.getColumnIndex(COLUMN_DATE))
                    val description = it.getString(it.getColumnIndex(COLUMN_DESCRIPTION))
                    val category = it.getString(it.getColumnIndex(COLUMN_CATEGORY))
                    val time = it.getString(it.getColumnIndex(COLUMN_TIME))
                    val typeString = it.getString(it.getColumnIndex(COLUMN_TYPE))
                    val type = BudgetType.valueOf(typeString)
                    budgets.add(Budget(id = id, title = title,
                        amount = amount, date = date,
                        description = description, category = category,
                        time = time, type = type))
                }
            }
            db.close()
            return budgets
        }

        fun deleteBudget(id: String) {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id))
            db.close()
        }
    }


}