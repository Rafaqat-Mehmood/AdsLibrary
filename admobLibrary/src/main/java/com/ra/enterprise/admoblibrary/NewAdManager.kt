package com.ra.enterprise.admoblibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.ra.enterprise.ConsentCallback
import com.ra.enterprise.admoblibrary.controller.ConsentController
import com.ra.enterprise.admoblibrary.enums.CMPStatus
import com.ra.enterprise.admoblibrary.enums.NativeAdType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * Developer Name : Rafaqat Mehmood
 * Whatsapp       : 0310-1025532
 * Designation    : Sr.Android Developer
 */
@SuppressLint("StaticFieldLeak")
object NewAdManager {
    private var mainInterstitialAd: InterstitialAd? = null
    private var mainCount = 0L
    private var mInterstitialAd: InterstitialAd? = null
    private var dialog: Dialog? = null
    private lateinit var shimmerContainer: ShimmerFrameLayout

    private lateinit var adaptiveBannerFrame: FrameLayout
    private lateinit var adView: AdView
    private lateinit var adSize: AdSize
    var sessionAdCount=0
    var perSessionAd=25
    var remoteXValue=3

    //Step 1: Initialize Ads
    fun initializeAds(context: Context) {
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(context) {
                Log.i("TAG", "initializeAds: ")
            }
        }
    }



    //Step 2: Main Interstitial Ad Load And Show on the Spot with Ad Loading Dialog
    fun loadAndShow(
        context: Activity,
        adId: String,
        onAdDismissed: () -> Unit
    ) {
        mainCount++

        if (mainCount % remoteXValue != 0L) {
            onAdDismissed()
            return
        }


        if (sessionAdCount >= perSessionAd) {
            Log.i("TAG", "Ad limit reached for this session.")
            onAdDismissed()
            return
        }

        if (mainInterstitialAd == null) {
            val loadingDialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            loadingDialog.setCancelable(false)
            loadingDialog.setCanceledOnTouchOutside(false)
            loadingDialog.setContentView(R.layout.layout_loading_ad)
            loadingDialog.show()

            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(context, adId, adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    mainInterstitialAd = interstitialAd
                    sessionAdCount++ // Modify the mutable copy

                    Log.e(
                        "TAG",
                        "onAdLoaded: Ad is ready." + mainCount + "---" + sessionAdCount + "---" + remoteXValue
                    )

                    loadingDialog.dismiss()
                    mainInterstitialAd!!.show(context)
                    mainInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.i("TAG", "Ad dismissed. Refreshing ad.")
                                mainInterstitialAd = null
                                onAdDismissed()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e("TAG", "Ad failed to show: ${adError.message}")
                                mainInterstitialAd = null
                                onAdDismissed()
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    mainInterstitialAd = null
                    Log.e("TAG", "Failed Interstitial: ${loadAdError.message}")

                    loadingDialog.dismiss()
                    onAdDismissed()
                }
            })
            Log.i("TAG", "Main-Inter-Loaded: Request sent.")
        } else {
            Log.i("TAG", "Main-Inter-Already-Loaded: ")
            mainInterstitialAd?.show(context)
        }
    }


    //Step 3: Just Interstitial Ad Load Method if loaded then show
    fun interLoad(context: Activity, id: String, onAdDismissed: () -> Unit) {
        try {
            dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setContentView(R.layout.layout_loading_ad)
            dialog!!.show()
            if (mInterstitialAd == null) {
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    context, id, adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            super.onAdLoaded(interstitialAd)
                            mInterstitialAd = interstitialAd
                            dialog!!.dismiss()
                            showInter(context, onAdDismissed)
                            Log.e("TAG", "Interstitial Ad Loaded: ")

                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            super.onAdFailedToLoad(loadAdError)
                            mInterstitialAd = null
                            dialog!!.dismiss()
                            onAdDismissed()

                            Log.e("TAG", "Interstitial Failed: " + loadAdError.message)

                        }
                    })
            } else {
                Log.i("TAG", "Already-Splash-Inter-Loaded: ")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showInter(
        context: Activity,
        onAdDismissed: () -> Unit
    ) {

        try {
            if (mInterstitialAd != null) {
                mInterstitialAd!!.show(context)
                mInterstitialAd!!.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            mInterstitialAd = null
                            dialog!!.dismiss()
                            onAdDismissed()

                        }

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            super.onAdFailedToShowFullScreenContent(p0)
                            mInterstitialAd = null
                            dialog!!.dismiss()
                            onAdDismissed()

                        }
                    }


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Just Interstitial Ad Load Method
    fun interJustLoadCall(context: Activity, id: String, onAdDismissed: () -> Unit) {
        try {
            dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.setContentView(R.layout.layout_loading_ad)
            dialog!!.show()
            if (mInterstitialAd == null) {
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(
                    context, id, adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            super.onAdLoaded(interstitialAd)
                            mInterstitialAd = interstitialAd
                            dialog!!.dismiss()
                            Log.e("TAG", "Interstitial Ad Loaded: ")
                            onAdDismissed()
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            super.onAdFailedToLoad(loadAdError)
                            mInterstitialAd = null
                            dialog!!.dismiss()
                            onAdDismissed()

                            Log.e("TAG", "Interstitial Failed: " + loadAdError.message)

                        }
                    })
            } else {
                Log.i("TAG", "Already-Splash-Inter-Loaded: ")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //Step 4: Load Small Banner
    @SuppressLint("StaticFieldLeak")
    fun loadAndShowSmallBanner(
        activity: Activity,
        adViewLayout: ViewGroup,
        id: String
    ) {

        // Find views from the inflated layout
        shimmerContainer = adViewLayout.findViewById(R.id.shimmer_view_container)
        adaptiveBannerFrame = adViewLayout.findViewById(R.id.adaptive_banner_frame)


        adView = AdView(activity).apply {
            adUnitId = id
        }


        adaptiveBannerFrame.addView(adView)

        val adRequest: AdRequest = AdRequest.Builder().build()
        adSize = getAdSize(activity, adaptiveBannerFrame)

        adView.setAdSize(adSize)
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
            }
        }


    }

    @SuppressLint("StaticFieldLeak")
    fun loadAndShowMediumBanner(activity: Activity, adViewLayout: ViewGroup, id: String) {

        // Find views
        val shimmerContainer =
            adViewLayout.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        val bannerAdFrame = adViewLayout.findViewById<FrameLayout>(R.id.adaptive_banner_frame)


        // Create and configure AdView
        adView = AdView(activity).apply {
            adUnitId = id
            setAdSize(AdSize.MEDIUM_RECTANGLE) // Adjust size if needed
        }

        // Add AdView to container
        bannerAdFrame.addView(adView)

        // Load Ad
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
                bannerAdFrame.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
                Log.d("bannerAd", "Failed to load banner ad: $p0")
            }
        }


    }

    @SuppressLint("StaticFieldLeak")
    fun loadAndShowLargeBanner(activity: Activity, adViewLayout: ViewGroup, id: String) {

        // Find views
        val shimmerContainer =
            adViewLayout.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        val bannerAdFrame = adViewLayout.findViewById<FrameLayout>(R.id.adaptive_banner_frame)


        // Create and configure AdView
        adView = AdView(activity).apply {
            adUnitId = id
            setAdSize(AdSize.LARGE_BANNER) // Adjust size if needed
        }

        // Add AdView to container
        bannerAdFrame.addView(adView)

        // Load Ad
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
                bannerAdFrame.visibility = View.VISIBLE
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
                Log.d("bannerAd", "Failed to load banner ad: $p0")
            }
        }


    }


    @SuppressLint("StaticFieldLeak")
    fun loadAndShowCollapsibleAd(
        activity: Activity,
        adViewLayout: ViewGroup,
        openAd: String,
        id: String
    ) {


        // Find views
        val shimmerContainer =
            adViewLayout.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        val bannerAdFrame = adViewLayout.findViewById<FrameLayout>(R.id.adaptive_banner_frame)


        // Get screen width in dp
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val displayMetrics = activity.resources.displayMetrics
        val adWidthPixels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager!!.currentWindowMetrics.bounds.width()
        } else {
            displayMetrics.widthPixels
        }
        val density = displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        // Set AdSize dynamically
        val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)

        // Create and configure AdView
        val adView = AdView(activity).apply {
            setAdSize(adSize)
            adUnitId = id
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.visibility = View.GONE
                    bannerAdFrame.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.visibility = View.GONE
                    Log.e("bannerAd", "Failed to load collapsible ad: ${error.message}")
                }
            }
        }

        // Add AdView to container
        bannerAdFrame.addView(adView)

        // Load Ad with extra parameters
        val extras = Bundle().apply {
            putString("collapsible", openAd)
        }

        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()

        adView.loadAd(adRequest)


    }

    @SuppressLint("StaticFieldLeak")
    private fun getAdSize(activity: Activity, bannerAd: FrameLayout): AdSize {
        return try {
            // Determine the ad container's width in pixels.
            val adWidthPixels: Int = if (bannerAd.width > 0) {
                // Use the measured width of the layout if it's available.
                bannerAd.width
            } else {
                // Fallback: Retrieve the screen width using the best available API.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    // For API 30+ use the WindowMetrics API.
                    val windowMetrics = activity.windowManager.currentWindowMetrics
                    val insets = windowMetrics.windowInsets
                        .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                    // Subtract the insets (e.g., status and navigation bars) from the total width.
                    windowMetrics.bounds.width() - insets.left - insets.right
                } else {
                    // For older APIs, use the legacy DisplayMetrics approach.
                    @Suppress("DEPRECATION")
                    val displayMetrics = DisplayMetrics()
                    @Suppress("DEPRECATION")
                    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                    displayMetrics.widthPixels
                }
            }

            // Convert the width from pixels to dp (density-independent pixels) using the resources' metrics.
            val density = activity.resources.displayMetrics.density
            val adWidth = (adWidthPixels / density).toInt()

            // Return an adaptive banner ad size based on the current orientation.
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        } catch (e: Exception) {
            Log.e("TAG", "Error in getAdSize: ${e.message}", e)
            // Return a default ad size as a fallback.
            AdSize.BANNER
        }
    }


    @SuppressLint("StaticFieldLeak")
    fun loadAndShowNativeAd(
        activity: Activity,
        adContainer: ViewGroup,
        adType: NativeAdType,
        id: String
    ) {


        // Find views
        val shimmerContainer =
            adContainer.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)
        val nativeAdFrame = adContainer.findViewById<FrameLayout>(R.id.nativeAd)

        // Start shimmer animation
        shimmerContainer.startShimmer()



        // Create AdLoader
        val adLoader = AdLoader.Builder(activity, id)
            .forNativeAd { nativeAd ->
                // Inflate native ad layout
                var nativeAdView: NativeAdView? = null
                when (adType) {
                    NativeAdType.SMALL_MEDIA -> {
                        nativeAdView = activity.layoutInflater.inflate(
                            R.layout.native_ad_small_media_layout,
                            null
                        ) as NativeAdView
                    }

                    NativeAdType.LARGE_MEDIA -> {
                        nativeAdView = activity.layoutInflater.inflate(
                            R.layout.native_ad_large_media_layout,
                            null
                        ) as NativeAdView

                    }
                    NativeAdType.LARGE_MEDIA_RATING_TEXT -> {
                        nativeAdView = activity.layoutInflater.inflate(
                            R.layout.native_ad_media_text_rating_layout,
                            null
                        ) as NativeAdView

                    }

                    NativeAdType.IMAGE -> {
                        nativeAdView = activity.layoutInflater.inflate(
                            R.layout.native_ad_without_media_layout,
                            null
                        ) as NativeAdView

                    }
                }


                // Populate native ad
                populateNativeAdView(nativeAd, nativeAdView!!)

                // Remove previous views and add new native ad
                nativeAdFrame.removeAllViews()
                nativeAdFrame.addView(nativeAdView)

                // Handle shimmer
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
                nativeAdFrame.visibility = View.VISIBLE
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    // Handle error
                    shimmerContainer.stopShimmer()
                    shimmerContainer.visibility = View.GONE
                    nativeAdFrame.visibility = View.GONE
                    Log.d("NativeAd", "Failed to load native ad: $error")
                }
            })
            .build()

        // Load the ad
        adLoader.loadAd(AdRequest.Builder().build())

        // Add container to activity root view
//        (activity.findViewById<View>(android.R.id.content) as ViewGroup).addView(adContainer)
    }

    private fun populateNativeAdView(nativeAd: NativeAd, nativeAdView: NativeAdView) {
        // Set media view
        val mediaView = nativeAdView.findViewById<MediaView>(R.id.nativeMedia)
        nativeAdView.mediaView = mediaView

        // Set other views
        nativeAdView.headlineView = nativeAdView.findViewById(R.id.nativeHeadline)
        nativeAdView.bodyView = nativeAdView.findViewById(R.id.nativeBody)
        nativeAdView.callToActionView = nativeAdView.findViewById(R.id.nativeAction)
        nativeAdView.iconView = nativeAdView.findViewById(R.id.nativeIcon)
        nativeAdView.priceView = nativeAdView.findViewById(R.id.nativePrice)
        nativeAdView.advertiserView = nativeAdView.findViewById(R.id.nativeAdvertiser)
        nativeAdView.storeView = nativeAdView.findViewById(R.id.nativeStore)

        // Populate text
        (nativeAdView.headlineView as TextView).text = nativeAd.headline
        (nativeAdView.bodyView as TextView).text = nativeAd.body ?: ""
        (nativeAdView.callToActionView as Button).text = nativeAd.callToAction ?: ""

        // Handle optional fields
        if (nativeAd.icon != null) {
            (nativeAdView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
        } else {
            nativeAdView.iconView?.visibility = View.GONE
        }

        // Set other optional fields similarly...

        // Register native ad
        nativeAdView.setNativeAd(nativeAd)
    }


}

