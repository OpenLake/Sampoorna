package org.openlake.sampoorna.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.ActivityMainBinding
import org.openlake.sampoorna.util.services.ReactivateService

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object{
        var SOSSwitch = MutableLiveData<Boolean>()
    }
    init {
        SOSSwitch.postValue(false)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
      //  Implementing bottom navigation view
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView)
        //using AppBarConfiguration because sibling screens are not hierarchically related
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.alertFragment,R.id.trackingFragment,R.id.blogsFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onDestroy() {
        if (SOSSwitch.value==true) {
            val broadcastIntent = Intent()
            broadcastIntent.action = "restart Service"
            broadcastIntent.setClass(this, ReactivateService::class.java)
            this.sendBroadcast(broadcastIntent)
        }
        super.onDestroy()
    }
}