package org.openlake.sampoorna.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.databinding.ActivityMainBinding
import org.openlake.sampoorna.presentation.features.profile.ProfileFragment
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

        // Implementing bottom navigation view
        val navController = findNavController(R.id.fragmentContainerView)

        //using AppBarConfiguration because sibling screens are not hierarchically related
        val bottomNavDestinations = setOf(
            R.id.alertFragment,
            R.id.trackingFragment,
            R.id.blogsFragment,
            R.id.selfCareFragment
        )

        val appBarConfiguration = AppBarConfiguration(
            bottomNavDestinations,
            binding.drawerLayout
        )
        binding.toolbar.setupWithNavController(navController,appBarConfiguration)

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id in bottomNavDestinations) {
                binding.bottomNavigationView.visibility = View.VISIBLE
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                binding.bottomNavigationView.visibility = View.GONE
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        binding.navigationDrawer.setNavigationItemSelectedListener { item->
            navController.navigate(when(item.itemId){
                else -> R.id.profileFragment
            })
            return@setNavigationItemSelectedListener true
        }
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