package com.ra.enterprise.admoblibrary

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner


open class App : Application(), LifecycleObserver {
    private var currentActivity: Activity? = null
    var isPurchase=false
    var showAd=false
    var blockScreenList= mutableListOf("")
    var adId="ca-app-pub-3940256099942544/9257395921"

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {
                // Here you can decide whether to show ads based on the activity name
//                if (activity.javaClass.simpleName != "SplashActivity") {
//                    SplashAppOpen.fetchAd(activity)
                currentActivity = activity

                //}
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {
                if (currentActivity == activity) {
                    currentActivity = null
                }
            }
        })
    }

    fun getCurrentActivity(): Activity? {
        return currentActivity
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        if (Constant.checkPauseToRestart) {
            var appOpenManager = SplashAppOpen()

            currentActivity?.let {
                if (checkForInternet() && !isPurchase) {
                    val activityName = it.javaClass.simpleName
                    if (activityName != "AdActivity" && activityName !in getBlockedScreens())
                    {
                        if (!isPurchase) {
//                            SplashAppOpen.fetchAd(it)

                            if (SplashAppOpen.appOpenAd != null) {
                                // Show the ad if available
                                SplashAppOpen.showAdIfAvailable(it)
                            } else {
                                // If no ad is available, request a new one
                                SplashAppOpen.fetchAd(it,adId,showAd)
                            }

                        }

                    }

                }
            }
        }
        println("App is in foreground")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        Constant.checkPauseToRestart = true
        println("App is in background")
    }

    fun blockScreenList(activityName: List<String>) {
        blockScreenList.addAll(activityName) // Add activity name to the list
    }

    fun getBlockedScreens(): List<String> {
        return blockScreenList // Return the list when needed
    }
}