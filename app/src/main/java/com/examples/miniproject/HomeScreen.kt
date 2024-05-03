package com.examples.miniproject


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.examples.miniproject.Fragments.FeedbackFragment
import com.examples.miniproject.Fragments.HomeFragment
import com.examples.miniproject.Fragments.NotesFragment
import com.examples.miniproject.Fragments.ProfileFragment
import com.examples.miniproject.Fragments.SettingsFragment
import com.examples.miniproject.Fragments.TasksFragment
import com.examples.miniproject.Fragments.TodoFragment
import com.examples.miniproject.HelperActivities.Constants
import com.examples.miniproject.HelperActivities.MainViewModel
import com.examples.miniproject.HelperActivities.helperFragamentsActivities.AddNotes
import com.examples.miniproject.HelperActivities.helperFragamentsActivities.AddTransactionsFragment
import com.examples.miniproject.databinding.ActivityHomeScreenBinding
import com.google.android.material.navigation.NavigationView
import java.util.Calendar

class HomeScreen : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var searchEditText: EditText
    private lateinit var originalMenuList: MutableList<MenuItem>
    private lateinit var calendar: Calendar
    lateinit var viewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        Constants.setCategories()

        calendar = Calendar.getInstance()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, HomeFragment())
        onResume()
        transaction.commit()

        setSupportActionBar(binding.toolbarhome)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbarhome,
            R.string.nav_open,
            R.string.nav_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> openFragment(HomeFragment())
                R.id.bottom_notes -> openFragment(NotesFragment())
                R.id.bottom_todo -> openFragment(TodoFragment())
                R.id.bottom_tasks -> openFragment(TasksFragment())
            }
            true
        }

        fragmentManager = supportFragmentManager
        openFragment(HomeFragment())

        binding.fab.setOnClickListener {
            // Handle FAB click for different fragments
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            when (currentFragment) {
                is HomeFragment -> {
                    // Handle FAB click for HomeFragment
                    //Toast.makeText(this, "FAB clicked in HomeFragment", Toast.LENGTH_SHORT).show()
                    val addTransactionFragment = AddTransactionsFragment()
                    addTransactionFragment.show(supportFragmentManager, "AddTransactionFragment")
                }
                is NotesFragment -> {
                    // Handle FAB click for NotesFragment
                    //Toast.makeText(this, "FAB clicked in NotesFragment", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AddNotes::class.java)
                    startActivity(intent)
                }
                is TodoFragment -> {
                    // Handle FAB click for ToDoFragment
                    Toast.makeText(this, "FAB clicked in ToDoFragment", Toast.LENGTH_SHORT).show()

                }
                is TasksFragment -> {
                    // Handle FAB click for TasksFragment
                    Toast.makeText(this, "FAB clicked in TasksFragment", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Initialize the original menu list
        originalMenuList = mutableListOf()
        binding.navigationDrawer.menu.forEach { originalMenuList.add(it) }

        // Setup the search EditText
        searchEditText = binding.navigationDrawer.getHeaderView(0).findViewById(R.id.search_view)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterMenuItems(s.toString())
                // Ensure the EditText remains focused after a short delay
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    searchEditText.requestFocus()
                }, 100)
            }
        })

        // Retrieve username from SharedPreferences
        sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")
        if (!username.isNullOrEmpty()) {
            // Set username to TextView
            binding.navigationDrawer.getHeaderView(0).findViewById<TextView>(R.id.title).text = username
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_profile -> openFragment(ProfileFragment())
            R.id.nav_tasks -> openFragment(TasksFragment())
            R.id.nav_todo -> openFragment(TodoFragment())
            R.id.nav_notes -> openFragment(NotesFragment())
            R.id.nav_feedback -> openFragment(FeedbackFragment())
            R.id.nav_settings -> openFragment(SettingsFragment())
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun filterMenuItems(query: String) {
        val filteredMenuList = mutableListOf<MenuItem>()
        originalMenuList.forEach { menuItem ->
            if (menuItem.title.toString().contains(query, ignoreCase = true)) {
                filteredMenuList.add(menuItem)
            }
        }
        binding.navigationDrawer.menu.clear()
        if (query.isNotEmpty()) {
            filteredMenuList.forEach { menuItem ->
                binding.navigationDrawer.menu.add(menuItem.groupId, menuItem.itemId, menuItem.order, menuItem.title).apply {
                    icon = menuItem.icon // Set the icon for the menu item
                }
            }
        } else {
            originalMenuList.forEach { menuItem ->
                binding.navigationDrawer.menu.add(menuItem.groupId, menuItem.itemId, menuItem.order, menuItem.title).apply {
                    icon = menuItem.icon // Set the icon for the menu item
                }
            }
        }
    }
}
