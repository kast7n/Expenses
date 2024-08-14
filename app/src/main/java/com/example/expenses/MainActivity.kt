package com.example.expenses

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expenses.databinding.ActivityMainBinding
import com.example.expenses.helpers.DatabaseHelper
import com.example.expenses.models.Budget
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var budgetList = ArrayList<Budget>()
    private var db : DatabaseHelper? = null
    private lateinit var budgetAdapter: BudgetAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        db = DatabaseHelper(this)
//        budgetList = db!!.getAllBudgets() as ArrayList<Budget>
//        val recyclerView: RecyclerView = findViewById(R.id.recyclerExpenses)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        budgetAdapter = BudgetAdapter(this, budgetList)
//        recyclerView.adapter = budgetAdapter
//
//        val btnAddBudget = findViewById<FloatingActionButton>(R.id.fabAddExpense)
//        btnAddBudget.setOnClickListener{
//            val intent = Intent(this, AddBudgetActivity::class.java)
//            startActivity(intent)
//        }
//
//
//
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        loadBudgets()
//    }
//
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun loadBudgets() {
//        budgetList.clear()
//        //db.clearDatabase();
//        db?.let { budgetList.addAll(it.getAllBudgets()) }
//        budgetAdapter.notifyDataSetChanged()
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}