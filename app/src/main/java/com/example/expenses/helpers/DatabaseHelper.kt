package com.example.expenses.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.expenses.models.BudgetType
import com.example.expenses.models.Budget
import com.example.expenses.models.Category

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        companion object {
            private const val DATABASE_NAME = "budgeting.db"
            private const val DATABASE_VERSION = 1


            private const val TABLE_NAME = "budgets"
            private const val COLUMN_ID = "id"
            private const val COLUMN_AMOUNT = "amount"
            private const val COLUMN_DATE = "date"
            private const val COLUMN_CATEGORY = "category"
            private const val COLUMN_TYPE = "type"
            private const val TABLE_CATEGORIES = "BudgetCategory"
            private const val COLUMN_CATEGORY_ID = "id"
            private const val COLUMN_CATEGORY_NAME = "name"

        }

        override fun onCreate(db: SQLiteDatabase) {
            val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AMOUNT REAL,
                $COLUMN_DATE TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_TYPE TEXT   
            )
        """.trimIndent()
            db.execSQL(createTable)

            val createCategoriesTable = """
        CREATE TABLE $TABLE_CATEGORIES (
            $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_CATEGORY_NAME TEXT
        )
    """.trimIndent()
            db.execSQL(createCategoriesTable)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }

        fun insertBudget(expense: Budget): Long {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_AMOUNT, expense.amount)
                put(COLUMN_DATE, expense.date)
                put(COLUMN_CATEGORY, expense.category)
                put(COLUMN_TYPE, expense.type.name)
            }
            return db.insert(TABLE_NAME, null, values)
        }
        fun insertCategory(category: Category): Long {
            val db = this.writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_CATEGORY_NAME, category.name)

            }
            return db.insert(TABLE_CATEGORIES, null, values)
        }

        fun clearDatabase() {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, null, null)
            db.delete(TABLE_CATEGORIES, null, null)
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
                    val amount = it.getDouble(it.getColumnIndex(COLUMN_AMOUNT))
                    val date = it.getString(it.getColumnIndex(COLUMN_DATE))
                    val category = it.getString(it.getColumnIndex(COLUMN_CATEGORY))

                    val typeString = it.getString(it.getColumnIndex(COLUMN_TYPE))
                    val type = BudgetType.valueOf(typeString)
                    budgets.add(Budget(id = id,
                        amount = amount,
                        date = date,
                        category = category,
                        type = type))
                }
            }
            db.close()
            return budgets
        }

    @SuppressLint("Range")
    fun getAllCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndex(COLUMN_CATEGORY_ID))
                val name = it.getString(it.getColumnIndex(COLUMN_CATEGORY_NAME))

                categories.add(Category(id = id,name = name))
            }
        }
        db.close()
        return categories
    }

    @SuppressLint("Range")
    fun getAllCategoriesNames(): ArrayList<String> {
        val categories = ArrayList<String>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, null)
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(COLUMN_CATEGORY_NAME))

                categories.add(name)
            }
        }
        db.close()
        return categories
    }

        fun deleteBudget(id: String) {
            val db = this.writableDatabase
            db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id))
            db.close()
        }
    }


