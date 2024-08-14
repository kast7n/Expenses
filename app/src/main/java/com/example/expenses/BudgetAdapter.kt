package com.example.expenses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.helpers.DatabaseHelper
import com.example.expenses.models.Budget

class BudgetAdapter(private val context: Context, private val budgetList: List<Budget>) :
    RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {


    class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAmount: TextView = itemView.findViewById(R.id.txtBudgetAmount)
        val tvCategory: TextView = itemView.findViewById(R.id.txtBudgetCategory)
        val tvDate: TextView = itemView.findViewById(R.id.txtBudgetTime)
        val tvType: TextView = itemView.findViewById(R.id.txtBudgetType)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.budget_item, parent, false) // Adjust layout name
        return BudgetViewHolder(view)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder,position: Int) {
        val budget = budgetList[position]
        holder.tvAmount.text = budget.amount.toString()
        holder.tvCategory.text = budget.category
        holder.tvDate.text = budget.date
        holder.tvType.text = budget.type.name


    }
    

    override fun getItemCount(): Int = budgetList.size
}