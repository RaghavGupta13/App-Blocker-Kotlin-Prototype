package com.example.blockerkotlinprototype

import android.app.AppOpsManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //call to enableUsageAccessSettings
        enableUsageAccessSettings()

        //Call to startBlockingApps method
        startBlockingApps()
    }

    //Method to start background service
    private fun startBackgroundService(){
        val intent = Intent(this, MyBackgroundService::class.java)
        startService(intent)
    }

    //Method to open usage access settings
    /*
    Usage access settings need to be enabled for the app
    so that our app can track the other apps on the phone
     */
    private fun enableUsageAccessSettings(){
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }

    //This method checks if usage access is anabled or not and return a boolean value
    private fun checkIfUsageAccess(): Boolean{

        try {
            val mPackageManager: PackageManager = applicationContext.packageManager
            val applicationInfo: ApplicationInfo = mPackageManager.getApplicationInfo(applicationContext.packageName, 0)
            val appOpsManager: AppOpsManager = applicationContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName)

            return (mode == AppOpsManager.MODE_ALLOWED)

        }catch (e: PackageManager.NameNotFoundException){
            return false
        }
    }

    //This method handles click event for the 'start blocking apps' button
    private fun startBlockingApps(){
        btn = findViewById(R.id.id_btn)

        btn.setOnClickListener {
            if(!checkIfUsageAccess()){
                val toast = Toast.makeText(applicationContext,
                    "Usage Access should be enabled",
                    Toast.LENGTH_SHORT)
                toast.show()
                enableUsageAccessSettings()
            }else{
                startBackgroundService()
                finish()
            }
        }
    }

}