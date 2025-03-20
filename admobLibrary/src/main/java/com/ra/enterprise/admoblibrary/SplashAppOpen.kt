package com.ra.enterprise.admoblibrary

import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.ra.enterprise.admoblibrary.Constant.checkPauseToRestart


class SplashAppOpen {

    companion object {
        private var dialog: Dialog? = null
        private const val TAG = "SplashOpenAd"
        var appOpenAd: AppOpenAd? = null
        private var loadCallback: AppOpenAdLoadCallback? = null
        private var isAdFetching: Boolean = false // Flag to track ad fetching


        fun fetchAd(context: Activity,id:String) {
//            if (!onOffAds) {
//                Log.d(TAG, "Ads are turned off. Skipping ad fetch.")
//                return
//            }

            // Skip fetch if an ad is already loaded or being fetched
            if (appOpenAd != null) {
                Log.d(TAG, "Ad already loaded. No need to fetch again.")
                return
            }

            if (isAdFetching) {
                Log.d(TAG, "Ad fetch request ignored. Already fetching an ad.")
                return
            }

                    isAdFetching = true // Set flag to true
                    dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                    dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog!!.setCancelable(false)
                    dialog!!.setCanceledOnTouchOutside(false)
                    dialog!!.setContentView(R.layout.layout_loading_ad)

                    dialog!!.show()

                    val handler = Handler(Looper.getMainLooper())
                    val timeoutRunnable = Runnable {
                        if (dialog != null && dialog!!.isShowing) {
                            dismissDialogSafely() // Use the safe method for dismissing the dialog
                            isAdFetching = false // Reset flag on timeout
                            Log.d(TAG, "Ad loading timed out after 10 seconds.")
                        }
                    }
                    handler.postDelayed(timeoutRunnable, 10000)

                    loadCallback = object : AppOpenAdLoadCallback() {
                        override fun onAdLoaded(ad: AppOpenAd) {
                            if (dialog != null && dialog!!.isShowing) {
                                dismissDialogSafely() // Use the safe method here too
                            }
                            isAdFetching = false // Reset flag on success
                            appOpenAd = ad // Store the loaded ad
                            handler.removeCallbacks(timeoutRunnable)
                            showAdIfAvailable(context)
                            Log.d(TAG, "Ad loaded successfully and ready to be used.")
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            Log.d(TAG, "Ad failed to load: ${loadAdError.message}")
                            if (dialog != null && dialog!!.isShowing) {
                                dismissDialogSafely() // Use the safe method here
                            }
                            isAdFetching = false // Reset flag on failure
                            handler.removeCallbacks(timeoutRunnable)
                        }
                    }

                    val request = adRequest
                    AppOpenAd.load(
                        context,
                        id,
                        request,
                        loadCallback!!
                    )


        }

        fun showAdIfAvailable(activity: Activity) {
            if (appOpenAd == null) {
                Log.d(TAG, "No ad available to show.")
                return
            }

            // Check if app is in foreground
            if (!isAppInForeground(activity)) {
                Log.d(TAG, "Ad cannot be shown as the app is not in the foreground.")
                return
            }

            val fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null // Clear the ad after it is dismissed
                    isAdFetching = false // Reset fetching flag
                    checkPauseToRestart = false
                    Log.d(TAG, "Ad dismissed.")
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.i(TAG, "Ad failed to show: $adError")
                    appOpenAd = null // Clear the ad on failure
                    isAdFetching = false // Reset fetching flag
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad is showing.")
                }
            }

            appOpenAd!!.fullScreenContentCallback = fullScreenContentCallback
            appOpenAd!!.show(activity)
        }


        private val adRequest: AdRequest
            get() = AdRequest.Builder().build()

        private fun isAppInForeground(context: Context): Boolean {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appProcesses = activityManager.runningAppProcesses ?: return false

            for (appProcess in appProcesses) {
                if (appProcess.processName == context.packageName) {
                    return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                }
            }
            return false
        }

        private fun dismissDialogSafely() {
            if (dialog != null && dialog!!.isShowing) {
                val window = dialog!!.window
                if (window != null && window.decorView.isAttachedToWindow) {
                    dialog!!.dismiss()
                }
            }
        }

    }


}
