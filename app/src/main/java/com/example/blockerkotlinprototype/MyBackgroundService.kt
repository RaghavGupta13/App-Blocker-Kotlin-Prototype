package com.example.blockerkotlinprototype

import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

/*
 Background service will keep on running until the user terminates the app.
 In most case scenarios, user will only press back button to close the app.
 The background service will keep checking usage stats for the system every 2 secs and
 as soon as the top package name (the last opened app by the user) matches the hardcoded package
 name 'com.google.android.gm' (package for the GMail app), it will send an intent to start a new
 activity which is our Lock Activity.
 Therefore, every time a user opens the Gmail app, the lock activity screen will get displayed blocking the user's
 access to GMail.
  */

class MyBackgroundService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Thread(
            object : Runnable{
                override fun run() {
                    while (true){
                        Log.d(TAG, "onStartCommand: " + "Service is running")

                        var topPackageName: String? = null
                        val usageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                        val time: Long = System.currentTimeMillis()
                        //Getting usage stats for last 10 secs
                        val stats: List<UsageStats> = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,
                            time - 1000 * 10, time)
                        //Sort the stats by last time used

                        if(stats != null){
                            var mySortedMap: SortedMap<Long, UsageStats> = TreeMap()

                            for(usageStats in stats){
                                mySortedMap.put(usageStats.lastTimeUsed, usageStats)
                            }

                            if(!mySortedMap.isEmpty()){
                                topPackageName = mySortedMap.get(mySortedMap.lastKey())?.packageName
                                Log.d(TAG, "onStartCommand: $topPackageName" )

                                if(topPackageName.equals("com.google.android.gm")){
                                    val intent = Intent(applicationContext, LockActivity::class.java)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                }
                            }

                        }
                        try {
                            Thread.sleep(2000)
                        }catch (e: InterruptedException){
                            e.printStackTrace()
                        }
                    }
                }


            }
        ).start()

        return super.onStartCommand(intent, flags, startId)
    }
}