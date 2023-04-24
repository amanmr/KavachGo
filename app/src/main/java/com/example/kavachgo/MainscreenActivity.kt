package com.example.kavachgo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kavachgo.databinding.ActivityMainscreenBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class MainscreenActivity : AppCompatActivity() {

    val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val MY_PERMISSIONS_REQUEST = 100
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainscreenBinding

    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMainscreen.toolbar)

        binding.appBarMainscreen.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_content_mainscreen)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        SocketHandler.setSocket()
//        SocketHandler.establishConnection()
//        val mSocket=SocketHandler.getSocket()
//        mSocket.emit("trackMe","New Delhi")
//        mSocket.on("trackMe"){ args->
//            Toast.makeText(this,"${args[0]}",Toast.LENGTH_SHORT).show()
//        }
            findViewById<Button>(R.id.cautious).setOnClickListener { myfun() }


    }

    private fun myfun() {
        count=1
        setUpLocationListener()
    }


    override fun onStart() {
        requestAccessFineLocation()
        super.onStart()

        when{
            isFineLocationGranted() ->{
                when{
                    isLocationEnabled() -> setUpLocationListener()
                    else -> showGPSNotEnableDialogue()
                }
            }
            else -> requestAccessFineLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            999->
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                when{
                    isLocationEnabled() -> setUpLocationListener()
                    else -> showGPSNotEnableDialogue()
                }
            }else{
                Toast.makeText(this,"Permission Not Granted",Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {
        val providers= locationManager.getProviders(true)
        var l:Location? =null

        for(i in providers.indices.reversed()){
            l=locationManager.getLastKnownLocation(providers[i])
            if(l!=null) break
        }

        if(count!=0) {
            l?.let {
                sendSMS("9129792703", "${it.latitude},${it.longitude}")
                Toast.makeText(this, "${it.latitude} ${it.longitude}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun isFineLocationGranted() : Boolean{
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
    }


    private fun requestAccessFineLocation() {
        this.requestPermissions(
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            999
        )
    }

    fun isLocationEnabled(): Boolean{
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showGPSNotEnableDialogue(){
        AlertDialog.Builder(this)
            .setTitle("Enable GPS")
            .setMessage("GPS is required for SHEILD")
            .setCancelable(false)
            .setPositiveButton("Enable Now") { dialogInterface: DialogInterface, i: Int ->
                startActivity((Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                dialogInterface.dismiss()
            }.show()
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val sentPI: PendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent("SMS_SENT"),
            0 or PendingIntent.FLAG_IMMUTABLE
        )
            SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, sentPI, null)
            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.mainscreen, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_mainscreen)
        val mobile=intent.getStringExtra("mobile")
        val name=intent.getStringExtra("name").toString().capitalize()
        findViewById<TextView>(R.id.tvNameSlider).text=name
        findViewById<TextView>(R.id.tvMobileSlider).text=mobile
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}