package org.openlake.sampoorna.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.ActivityMainBinding
import org.openlake.sampoorna.presentation.features.contacts.ContactsViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Implementing bottom navigation view
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView)
        //using AppBarConfiguration because sibling screens are not hierarchically related
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.alertFragment,R.id.trackingFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId){
            android.R.id.home->{
                onBackPressed()
                true
            }
            else ->{
                super.onOptionsItemSelected(item)
            }
        }
    }
}