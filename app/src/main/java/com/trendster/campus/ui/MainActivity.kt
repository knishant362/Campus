package com.trendster.campus.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivityMainBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainViewModel : MainViewModel
    private lateinit var navController : NavController
    private lateinit var toggle: ActionBarDrawerToggle

//    private lateinit var listener : NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        
        setContentView(binding.root)
        val toolbar: Toolbar = binding.appBar.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)


        binding.appBar.imgWeekic.setOnClickListener {
            navController.navigate(R.id.action_scheduleFragment_to_weekScheduleFragment)
        }

        binding.drawerLayout.setOnClickListener {

        }


        toolbar.collapseActionView()

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.scheduleFragment,
            R.id.subjectsFragment,
            R.id.notificationsFragment,
            R.id.profileFragment,
            R.id.noticeFragment,
            R.id.aboutFragment
        ),drawerLayout)

//        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.close)

//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.appBar.myLayout.bottomNavigationView.setupWithNavController(navController)
        navView.setupWithNavController(navController)


        val mainViewModelFactory = MainViewModelFactory(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("MYVALUE", navController.currentDestination?.label.toString())
        if (navController.currentDestination?.label == "Schedule"){
            menuInflater.inflate(R.menu.main, menu)
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}