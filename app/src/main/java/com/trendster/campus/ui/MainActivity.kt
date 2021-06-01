package com.trendster.campus.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivityMainBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainViewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

//    private lateinit var listener : NavController.OnDestinationChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        setContentView(binding.root)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayUseLogoEnabled(true)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        val drawerMenu = binding.appBar.btnDrawer
        drawerMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.appBar.btnProfile.setOnClickListener {
            val currentFrag = navController.currentDestination?.label as String
            Log.d("MYFeg", navController.currentDestination!!.label as String)
            when (currentFrag) {
                "Schedule" -> { navController.navigate(R.id.action_scheduleFragment_to_profileFragment) }
                "Subjects" -> { navController.navigate(R.id.action_subjectsFragment_to_profileFragment) }
                "Notifications" -> { navController.navigate(R.id.action_notificationsFragment_to_profileFragment) }
            }
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.scheduleFragment,
                R.id.subjectsFragment,
                R.id.notificationsFragment,
            ),
            drawerLayout
        )

//        setupActionBarWithNavController(navController)
        binding.appBar.myLayout.bottomNavigationView.setupWithNavController(navController)
        navView.setupWithNavController(navController)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.navView.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.logout -> {
                    auth.signOut()
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                    true
                }
                R.id.noticeActivity -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, NoticeActivity::class.java))
                    true
                }
                R.id.aboutApp -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }

                else -> { true }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) ||
            super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
