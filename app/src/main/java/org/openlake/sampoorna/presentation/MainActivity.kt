package org.openlake.sampoorna.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import org.openlake.sampoorna.R
import org.openlake.sampoorna.data.constants.Constants
import org.openlake.sampoorna.databinding.ActivityMainBinding
import org.openlake.sampoorna.util.services.ReactivateService

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    companion object{
        var SOSSwitch = MutableLiveData<Boolean>()
    }
    init {
        SOSSwitch.postValue(false)
    }

    private lateinit var binding: ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Implementing bottom navigation view
        navController = findNavController(R.id.fragmentContainerView)

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
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

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
            when(item.itemId){
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment, bundleOf("uid" to auth.uid!!))
                }
            }
            true
        }

        val notificationChannel = NotificationChannel(Constants.NotificationChannel, Constants.Sampoorna, NotificationManager.IMPORTANCE_HIGH).apply {
            description = Constants.Sampoorna
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        val fragmentId = intent.getIntExtra("fragment", -1)
        if(fragmentId != -1) {
            navController.navigate(fragmentId)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                navController.popBackStack()
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