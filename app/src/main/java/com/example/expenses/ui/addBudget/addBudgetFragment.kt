package com.example.expenses

import android.app.AlertDialog
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
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.example.expenses.helpers.DatabaseHelper
import com.example.expenses.models.Budget
import com.example.expenses.models.BudgetType
import com.example.expenses.models.Category
import com.example.expenses.models.getCurrDate
import com.example.expenses.models.getCurrTime
import com.example.expenses.ui.home.HomeFragment
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.Locale

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
        var time = getCurrDate() + " " + getCurrTime()
        val imgClose = view.findViewById<ImageView>(R.id.imgMinimiseAddBudget)
        var minimised = false
        db = DatabaseHelper(requireContext())
        categories = db!!.getAllCategoriesNames()
        listView = view.findViewById(R.id.lvCategories)
        var selectedType: String? = "Expense"
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
                val newBudget = Budget(amount = amount, category = category, type = type, date = time)
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




        val etDateTime = view.findViewById<EditText>(R.id.etAddBudgetDateTime)
        etDateTime.setText(time)

        val dialogView = View.inflate(activity, R.layout.date_time_picker, null)
        val alertDialog = AlertDialog.Builder(activity).create()

        dialogView.findViewById<View>(R.id.date_time_set).setOnClickListener {
            val datePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)
            val timePicker = dialogView.findViewById<TimePicker>(R.id.time_picker)

            val calendar = GregorianCalendar(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth,
                timePicker.hour,
                timePicker.minute
            )

            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            time = formatter.format(calendar.time)
            etDateTime.setText(time)
            alertDialog.dismiss()
        }

        etDateTime.setOnClickListener {alertDialog.setView(dialogView)
            alertDialog.show()}





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