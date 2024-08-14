package com.example.expenses.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.AddBudgetFragment
import com.example.expenses.BudgetAdapter
import com.example.expenses.R
import com.example.expenses.databinding.FragmentHomeBinding
import com.example.expenses.helpers.DatabaseHelper
import com.example.expenses.models.Budget
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: DatabaseHelper
    private lateinit var budgetList: ArrayList<Budget>
    private lateinit var budgetAdapter: BudgetAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        db = DatabaseHelper(requireContext())
        budgetList = db.getAllBudgets() as ArrayList<Budget>
        val recyclerView: RecyclerView = binding.recyclerExpenses // Assuming you have this ID in your layout
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        budgetAdapter = BudgetAdapter(requireContext(), budgetList)
        recyclerView.adapter = budgetAdapter
        loadBudgets()

    return root
    }

    fun refreshBudgets() {
        loadBudgets()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun loadBudgets() {
        budgetList.clear()
        db.let { budgetList.addAll(it.getAllBudgets()) }
        budgetAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}