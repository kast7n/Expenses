package com.example.expenses

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.example.expenses.helpers.DatabaseHelper

class AddBudgetActivity : AppCompatActivity() {
    private var categories = ArrayList<String>()
    private var db: DatabaseHelper? = null
    private lateinit var dialog: Dialog
    private lateinit var selectedCategory: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_budget) // Set your layout file

        db = DatabaseHelper(this)
        categories = db!!.getAllCategoriesNames()

        val txtAddCategory = findViewById<TextView>((R.id.txtAddBudgetNewCategory))
        val txtSelectCategory = findViewById<TextView>((R.id.spinAddBudgetCategory))
        txtAddCategory.setOnClickListener {
            handleAddCategoryClick("Add")
        }
        txtSelectCategory.setOnClickListener {
            handleAddCategoryClick("Select")
        }


    }

    private fun handleAddCategoryClick(action : String) {
        dialog = Dialog(this@AddBudgetActivity)
        dialog.setContentView(R.layout.dialog_searchable_add_category)
        dialog.window?.setLayout(650, 800)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val btnAddCategory = dialog.findViewById<TextView>(R.id.btnAddCategory)
        if(action == "Add"){
            btnAddCategory.visibility = View.VISIBLE
        }
        val editText: EditText = dialog.findViewById(R.id.etCategoriesAdd)
        val listView: ListView = dialog.findViewById(R.id.lvCategories)
        val adapter =
            ArrayAdapter(this@AddBudgetActivity, android.R.layout.simple_list_item_1, categories)
        listView.adapter = adapter
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        btnAddCategory.setOnClickListener {
            if (editText.text.isNotEmpty() && !categories.contains(editText.text.toString())) {
                categories.add(editText.text.toString())
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }
        if(action == "Select") {
            listView.setOnItemClickListener { _, _, position, _ ->
                selectedCategory.text = adapter.getItem(position)
                dialog.dismiss()
            }
        }
    }
}