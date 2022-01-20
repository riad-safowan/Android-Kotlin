package com.riadsafowan.to_do.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.riadsafowan.to_do.R
import com.riadsafowan.to_do.data.local.pref.UserData
import com.riadsafowan.to_do.data.local.pref.UserDataStore
import com.riadsafowan.to_do.databinding.ActivityMainBinding
import com.riadsafowan.to_do.databinding.NavHeaderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawer: DrawerLayout
    private lateinit var navView: NavigationView

    private lateinit var userDataStore: UserDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawer = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(binding.toolbar)
//        setupActionBarWithNavController(navController)

        val toggle = ActionBarDrawerToggle(
            this, drawer, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        navView.setCheckedItem(R.id.tasks)
        navDrawer()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.tasks -> {
                    navController.navigate(R.id.tasksFragment)
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        viewModel.title.observe(this) {
            binding.toolbar.title = it
        }

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.tasksFragment -> binding.toolbar.title = destination.label
//                R.id.addEditTaskFragment -> binding.toolbar.title = destination.label
//            }
//        }
    }

    @DelicateCoroutinesApi
    private fun navDrawer() {
        userDataStore = UserDataStore(this)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val header = navigationView.getHeaderView(0)
        val hBinding: NavHeaderBinding = NavHeaderBinding.bind(header)

        this.lifecycleScope.launchWhenStarted {
            viewModel.userDataFlow.collect {
                if (it.isLoggedIn) {
                    launch(Dispatchers.Main) {
                        hBinding.name.text = it.name
                        hBinding.textView.text = it.email
                        hBinding.name.visibility = View.VISIBLE
                        hBinding.textView.visibility = View.VISIBLE
                    }
                } else {
                    launch(Dispatchers.Main) {
                        hBinding.name.visibility = View.GONE
                        hBinding.textView.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}