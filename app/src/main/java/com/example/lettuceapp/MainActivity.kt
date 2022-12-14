package com.example.lettuceapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lettuceapp.databinding.ActivityMainBinding
import com.example.lettuceapp.ui.registerlogin.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.articleFragment,
                R.id.learningFragment,
                R.id.userFragment,
                R.id.statisticsFragment,
                R.id.surveyFragment2,
                R.id.assessmentTabFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        runOnUiThread {
            var database = FirebaseDatabase.getInstance().getReference("users/$userId")
            database.get().addOnSuccessListener {
                drawerLayout.textViewSidebarEmail.text = it.child("email").getValue(String:: class.java)
                drawerLayout.textViewSidebarUsername.text = it.child("username").getValue(String:: class.java)
            }
        }

    }

    //Solve change night mode in apps causing app crash
    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        if(firebaseAuth.currentUser!!.isAnonymous){
            menu.findItem(R.id.action_signout).isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_signout -> {
                firebaseAuth.signOut().apply {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}