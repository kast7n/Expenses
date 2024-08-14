package com.example.expenses

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.example.expenses.helpers.DatabaseHelper
import com.example.expenses.models.Budget
import com.example.expenses.models.BudgetType
import com.example.expenses.models.Category
import com.example.expenses.ui.home.HomeFragment

class AddBudgetFragment : Fragment() {
    private var categories = ArrayList<String>()
    private var db: DatabaseHelper? = null
    private lateinit var txtCategory: EditText
    private lateinit var listView: ListView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_budget, container, false)

        val imgClose = view.findViewById<ImageView>(R.id.imgMinimiseAddBudget)
        var minimised = false
        db = DatabaseHelper(requireContext())
        categories = db!!.getAllCategoriesNames()
        listView = view.findViewById(R.id.lvCategories)
        var selectedType: String? = null
        val etAmount = view.findViewById<EditText>(R.id.etAddBudgetAmount)
        txtCategory = view.findViewById(R.id.etBudgetCategoriesAdd)

        txtCategory.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                listView.visibility = View.VISIBLE
                handleAddCategoryClick()
            } else {
                listView.visibility = View.GONE
            }
        }

        val radioGroupType = view.findViewById<RadioGroup>(R.id.rgrpBudgetType)
        radioGroupType.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            selectedType = selectedRadioButton.tag as String?
        }

        val btnAddBudget = view.findViewById<Button>(R.id.btnAddBudget)
        btnAddBudget.setOnClickListener {
            val type = when (selectedType) {
                "Expense" -> BudgetType.EXPENSE
                else -> BudgetType.INCOME
            }

            if (txtCategory.text.isNotEmpty() && !categories.contains(txtCategory.text.toString())) {
                categories.add(txtCategory.text.toString())
                db?.insertCategory(Category(id = "", name = txtCategory.text.toString()))
            }
            if (txtCategory.text.isEmpty()) {
                txtCategory.error = "Category name is required"
            }
            if (etAmount.text.isEmpty()) {
                etAmount.error = "Amount is required"
            }

            val category = txtCategory.text.toString()

            if (txtCategory.text.isNotEmpty() && etAmount.text.isNotEmpty()) {
                val amount = etAmount.text.toString().toDouble()
                val newBudget = Budget(amount = amount, category = category, type = type)
                db!!.insertBudget(newBudget)
                val parentHomeFragment = parentFragment as? HomeFragment
                parentHomeFragment?.refreshBudgets()
                etAmount.text.clear()
                txtCategory.text.clear()

            }
        }

        imgClose.setOnClickListener{
            val layoutBudgetAddInfo = view.findViewById<View>(R.id.layoutBudgetAddInfo)
            minimised = !minimised
            if(!minimised){
                layoutBudgetAddInfo.visibility = View.VISIBLE
                imgClose.rotationX = 180F
            }else{
                layoutBudgetAddInfo.visibility = View.GONE
                imgClose.rotationX = 0F
            }
        }

        return view
    }

    private fun handleAddCategoryClick() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        listView.adapter = adapter

        val layoutParams = listView.layoutParams

        fun setListHeight() {
            if (adapter.count < 3) {
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                layoutParams.height = 350 // Adjust as needed
            }
            listView.layoutParams = layoutParams
        }

        setListHeight()

        txtCategory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s){
                    setListHeight()
                }


            }

            override fun afterTextChanged(s: Editable?) {
                setListHeight()
            }
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            txtCategory.setText(adapter.getItem(position))
            txtCategory.clearFocus()
        }
    }
}