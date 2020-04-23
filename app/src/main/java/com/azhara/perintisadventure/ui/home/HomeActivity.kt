package com.azhara.perintisadventure.ui.home

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.azhara.perintisadventure.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_booked, R.id.navigation_profile
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        toolbar_home.setupWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            if (destination.id == R.id.navigation_home){
                tv_title_toolbar.visibility = View.GONE
                img_toolbar_home.visibility = View.VISIBLE
            }
            if (destination.id == R.id.navigation_booked){
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Pesanan"
                img_toolbar_home.visibility = View.GONE
            }
            if (destination.id == R.id.navigation_profile){
                tv_title_toolbar.visibility = View.VISIBLE
                tv_title_toolbar.text = "Profil"
                img_toolbar_home.visibility = View.GONE
            }
        }

    }
}
