package com.ra.enterprise.admoblibrary

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


/**
 * Developer Name : Rafaqat Mehmood
 * Whatsapp       : 0310-1025532
 * Designation    : Sr.Android Developer
 */
object NewAdManager {
    var mainInterstitialAd: InterstitialAd? = null
    var mainCount = 0L
    var mInterstitialAd: InterstitialAd? = null
    private var dialog: Dialog? = null


//    lateinit var adView: AdView
//    private lateinit var adSize: AdSize
//    private var nativeAd: NativeAd? = null

    var adCloseOrNot = false
    var isVisibile = false
    var mainAdIsLoaded = false

//    private var mNativeAd: NativeAd? = null



//    private var callBack: ProgressCallBack? = null

    var limitCounter = 0

//    fun setCall(callBack: ProgressCallBack) {
//        NewAdManager.callBack = callBack
//    }


    //Step 1: Initialize Ads
    fun initializeAds(context: Context?) {
        MobileAds.initialize(context!!) {
            Log.i("TAG", "initializeAds: ")
        }
    }


    //Step 2: Main Interstitial Ad Load And Show on the Spot with Ad Loading Dialog
    fun loadAndShow(
        context: Activity,
        remoteXValue: Long,
        sessionAdCount: Int, // Keep as val (immutable)
        perSessionAd: Int,
        adId: String,
        onAdDismissed: () -> Unit
    ) {
        mainCount++

        if (mainCount % remoteXValue != 0L) {
            onAdDismissed()
            return
        }

        var currentSessionAdCount = sessionAdCount // Create a mutable copy

        if (currentSessionAdCount >= perSessionAd) {
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
                    currentSessionAdCount++ // Modify the mutable copy

                    Log.e("TAG", "onAdLoaded: Ad is ready." + mainCount + "---" + currentSessionAdCount + "---" + remoteXValue)

                    loadingDialog.dismiss()
                    mainInterstitialAd!!.show(context)
                    mainInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
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
    fun interLoad(context: Activity, id: String,onAdDismissed: () -> Unit) {
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
                            showInter(context,onAdDismissed)
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

        } catch (e:Exception) {
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


//    fun loadSmallBanner(activity: Activity, linearLayout: LinearLayout,id: String) {
//        adView = AdView(activity)
//        (adView).let { validAdView ->
//            validAdView.adUnitId = id
//            linearLayout.removeAllViews()
//            linearLayout.addView(validAdView)
//            val adSize = getAdSize(activity, linearLayout)
//            validAdView.setAdSize(adSize)
//            val adRequest = AdRequest.Builder().build()
//            validAdView.loadAd(adRequest)
//            validAdView.adListener = object : AdListener() {
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    super.onAdFailedToLoad(loadAdError)
//                    Log.i("BannerAd", "Failed Banner: ${loadAdError.message}")
//                    linearLayout.visibility = View.GONE
//                }
//
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
//                    linearLayout.visibility = View.VISIBLE
//                    Log.i("BannerAd", "AdLoad")
//
//                }
//
//            }
//        }
//
//
//    }
//    fun loadOnboardSmallBanner(activity: Activity, linearLayout: LinearLayout,id: String,callBack:(Boolean)->Unit) {
//        adView = AdView(activity)
//        (adView).let { validAdView ->
//            validAdView.adUnitId = id
//            linearLayout.removeAllViews()
//            linearLayout.addView(validAdView)
//            val adSize = getAdSize(activity, linearLayout)
//            validAdView.setAdSize(adSize)
//            val adRequest = AdRequest.Builder().build()
//            validAdView.loadAd(adRequest)
//            validAdView.adListener = object : AdListener() {
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    super.onAdFailedToLoad(loadAdError)
//                    Log.i("BannerAd", "Failed Banner: ${loadAdError.message}")
////                    linearLayout.visibility = View.GONE
//                    callBack(false)
//                }
//
//                override fun onAdLoaded() {
//                    super.onAdLoaded()
////                    linearLayout.visibility = View.VISIBLE
//                    Log.i("BannerAd", "AdLoad")
//                    callBack(true)
//
//
//                }
//
//            }
//        }
//
//
//    }
//




//    private fun getAdSize(activity: Activity, linearLayout: LinearLayout): AdSize {
//        return try {
//            // Determine the ad container's width in pixels.
//            val adWidthPixels: Int = if (linearLayout.width > 0) {
//                // Use the measured width of the layout if it's available.
//                linearLayout.width
//            } else {
//                // Fallback: Retrieve the screen width using the best available API.
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    // For API 30+ use the WindowMetrics API.
//                    val windowMetrics = activity.windowManager.currentWindowMetrics
//                    val insets = windowMetrics.windowInsets
//                        .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
//                    // Subtract the insets (e.g., status and navigation bars) from the total width.
//                    windowMetrics.bounds.width() - insets.left - insets.right
//                } else {
//                    // For older APIs, use the legacy DisplayMetrics approach.
//                    @Suppress("DEPRECATION")
//                    val displayMetrics = DisplayMetrics()
//                    @Suppress("DEPRECATION")
//                    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
//                    displayMetrics.widthPixels
//                }
//            }
//
//            // Convert the width from pixels to dp (density-independent pixels) using the resources' metrics.
//            val density = activity.resources.displayMetrics.density
//            val adWidth = (adWidthPixels / density).toInt()
//
//            // Return an adaptive banner ad size based on the current orientation.
//            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
//        } catch (e: Exception) {
//            Log.e("TAG", "Error in getAdSize: ${e.message}", e)
//            // Return a default ad size as a fallback.
//            AdSize.BANNER
//        }
//    }
//
//
//
//
//    fun showBannerMediumAd(context: Context, bannerAd: LinearLayout, check: String) {
//        bannerAd.removeAllViews()
//        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
//        val displayMetrics = context.resources.displayMetrics
//        val adWidthPixels =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                val windowMetrics: WindowMetrics = windowManager!!.currentWindowMetrics
//                windowMetrics.bounds.width()
//            } else {
//                displayMetrics.widthPixels
//            }
//        val density = displayMetrics.density
//        val width = (adWidthPixels / density).toInt()
//        adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, width)
//        adView = AdView(context)
//        bannerAd.addView(adView)
//        adView.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                bannerAd.visibility = View.VISIBLE
//
//            }
//
//            override fun onAdFailedToLoad(p0: LoadAdError) {
//                super.onAdFailedToLoad(p0)
//                Log.d("bannerAd", "$p0")
//                bannerAd.visibility = View.INVISIBLE
//            }
//
//            override fun onAdOpened() {
//            }
//
//
//            override fun onAdClosed() {
//            }
//        }
//
//        loadBannerMedium(check)
//
//    }
//
//    private fun loadBannerMedium(check: String) {
//        if (App.remoteModel != null) {
//            adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
//            if (check == "2_4" && App.remoteModel!!.multiplePhoto24RectangleBanner.showAd) {
//                adView.adUnitId = App.remoteModel!!.multiplePhoto24RectangleBanner.adID
//
//            }
////            else if (check == "3_4" && App.remoteModel!!.pairedScreenBannerRectangle.showAd) {
////                adView.adUnitId = App.remoteModel!!.pairedScreenBannerRectangle.adID
////            }
////            else if (check == "ScanSubScreen" && App.remoteModel!!.scanSubScreenBannerRectangle.showAd) {
////                adView.adUnitId = App.remoteModel!!.scanSubScreenBannerRectangle.adID
////            }
//
//
//
//
//            val adRequest = AdRequest.Builder().build()
//            adView.loadAd(adRequest)
//        }
//
//    }
//
//    fun showCollapsibleAd(context: Context, bannerAd: LinearLayout, check: String) {
//        bannerAd.removeAllViews()
//        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
//        val displayMetrics = context.resources.displayMetrics
//        val adWidthPixels =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                val windowMetrics: WindowMetrics = windowManager!!.currentWindowMetrics
//                windowMetrics.bounds.width()
//            } else {
//                displayMetrics.widthPixels
//            }
//        val density = displayMetrics.density
//        val width = (adWidthPixels / density).toInt()
//        adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, width)
//        adView = AdView(context)
//        bannerAd.addView(adView)
//        adView.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                // Code to be executed when an ad finishes loading
//                bannerAd.visibility = View.VISIBLE
//
//            }
//
//            override fun onAdFailedToLoad(p0: LoadAdError) {
//                super.onAdFailedToLoad(p0)
//                Log.d("bannerAd", "$p0")
//                bannerAd.visibility = View.INVISIBLE
//            }
//
//            override fun onAdOpened() {
//                Log.i("TAG", "open: ")
//                adCloseOrNot = true
//
//            }
//
//
//            override fun onAdClosed() {
//                Log.i("TAG", "onAdClosed: ")
//                adCloseOrNot = false
//
//            }
//        }
//
//        loadCollapsible(check)
//
//    }
//
//    private fun loadCollapsible(check: String) {
//        if (App.remoteModel != null) {
//
//            adView.setAdSize(adSize)
//            if (check == "PrintLabel" && App.remoteModel!!.printLabelCollapsibleBanner.showAd) {
//                adView.adUnitId = App.remoteModel!!.printLabelCollapsibleBanner.adID
//            } else if (check == "MultiplePhoto" && App.remoteModel!!.multiplePhotoDashboardCollapsibleBanner.showAd) {
//                adView.adUnitId = App.remoteModel!!.multiplePhotoDashboardCollapsibleBanner.adID
//            } else if (check == "PrintCalendar" && App.remoteModel!!.printCalendarCollapsibleBanner.showAd) {
//                adView.adUnitId = App.remoteModel!!.printCalendarCollapsibleBanner.adID
//            }
//            else if (check == "MultiplePhoto68" && App.remoteModel!!.multiplePhoto68CollapsibleBanner.showAd) {
//                adView.adUnitId = App.remoteModel!!.multiplePhoto68CollapsibleBanner.adID
//            }
//
//
//
//            val extras = Bundle()
//            extras.putString("collapsible", "bottom")
//            val adRequest = AdRequest.Builder()
//                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
//                .build()
//            adView.loadAd(adRequest)
//        }
//
//
//    }


//    fun loadMainAdmobInterstitialAds(context2: Context,id: String) {
//        if (App.remoteModel != null && App.remoteModel!!.mainScreenInter.showAd) {
//
//                if (mainInterstitialAd == null && !mainAdIsLoaded) {
//                    val adRequest = AdRequest.Builder().build()
//                    InterstitialAd.load(
//                        context2, id, adRequest,
//                        object : InterstitialAdLoadCallback() {
//                            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                                super.onAdLoaded(interstitialAd)
//                                mainInterstitialAd = interstitialAd
//                                mainAdIsLoaded =true
//                                Log.i("TAG", "Main-onAdLoaded: ")
//
//                            }
//
//                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                                super.onAdFailedToLoad(loadAdError)
//                                mainInterstitialAd = null
//                                mainAdIsLoaded =false
//                                Log.i("TAG", "Main-Fail: " + loadAdError.message)
//
//                            }
//                        })
//                }
//            else
//                {
//                    mainAdIsLoaded =true
//                    Log.i("TAG", "Else-Already-Main-onAdLoaded: ")
//
//                }
//
//        }
//
//
//    }

//    fun loadMainAdmobInterstitialAds(context2: Context, id: String) {
//        val maxAdLoadLimit =3
//
//        // Check if the ad loading limit has been reached
//        if (perSessionAdCount >= maxAdLoadLimit) {
//            Log.i("TAG", "Ad loading limit reached for this session.")
//            return
//        }
//
//        if (App.remoteModel != null && App.remoteModel!!.mainScreenInter.showAd) {
//            if (mainInterstitialAd == null && !mainAdIsLoaded) {
//                val adRequest = AdRequest.Builder().build()
//                InterstitialAd.load(
//                    context2, id, adRequest,
//                    object : InterstitialAdLoadCallback() {
//                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                            super.onAdLoaded(interstitialAd)
//                            mainInterstitialAd = interstitialAd
//                            mainAdIsLoaded = true
//                            Log.i("TAG", "Main-onAdLoaded: ")
//
//                            // Increment and save the ad load count
////                            sharedPreferences.edit()
////                                .putInt("AdLoadCount", currentAdLoadCount + 1)
////                                .apply()
//                            perSessionAdCount++
//                        }
//
//                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                            super.onAdFailedToLoad(loadAdError)
//                            mainInterstitialAd = null
//                            mainAdIsLoaded = false
//                            Log.i("TAG", "Main-Fail: " + loadAdError.message)
//                        }
//                    }
//                )
//            } else {
//                mainAdIsLoaded = true
//                Log.i("TAG", "Else-Already-Main-onAdLoaded: ")
//            }
//        }
//    }


//    fun loadNative(context: Context, adContainer: TemplateView, advertisingArea: View, id: String) {
//
//        if (context.checkForInternet() && !SplashAct.purchaseSuccessfull) {
//            Log.i("TAG", "loadNative: " + id)
//            val adLoader = AdLoader.Builder(context, id)
//                .forNativeAd { nativeAd: com.google.android.gms.ads.nativead.NativeAd ->
//                    // Destroy any previously loaded NativeAd to avoid memory leaks
//                    NewAdManager.nativeAd?.destroy()
//                    NewAdManager.nativeAd = nativeAd
//                    adContainer.setNativeAd(nativeAd)
//                    advertisingArea.visibility = View.VISIBLE
//                    isVisibile = true
//
//
//                }.withAdListener(object : AdListener() {
//                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                        super.onAdFailedToLoad(loadAdError)
//                        advertisingArea.visibility = View.GONE
//
//                        Log.i("TAG", "Fail: " + loadAdError.message)
//
//
//                    }
//                })
//                .withNativeAdOptions(
//                    NativeAdOptions.Builder()
//                        .setRequestCustomMuteThisAd(true)
//                        .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT)
//                        .build()
//                )
//                .build()
//
//            adLoader.loadAd(AdRequest.Builder().build())
//        }
//
//    }



//    fun destroyNativeAd() {
//        if (nativeAd !=null) {
//            nativeAd!!.destroy()
//            nativeAd = null
//        }
//    }

    fun resetMainCount() {
        mainCount = 0L
    }



//    fun showInter(
//        context: Activity,
//        adDismissCall: Boolean,
//        event: String,
//        callbackMethod: Callable<Void>
//    ) {
//        // Check if ad count has reached the session limit
//        if (sessionAdCount >= MAX_ADS_PER_SESSION) {
//            Log.i("TAG", "Ad limit reached for this session.")
//            callbackMethod.call()
//            return
//        }
//
//        if (mainInterstitialAd != null) {
//            mainInterstitialAd!!.show(context)
//            mainInterstitialAd!!.fullScreenContentCallback =
//                object : FullScreenContentCallback() {
//                    override fun onAdDismissedFullScreenContent() {
//                        mainInterstitialAd = null
//                        sessionAdCount++ // Increment ad count
//
//                        if (sessionAdCount >= MAX_ADS_PER_SESSION) {
//                            Log.i("TAG", "Ad limit reached for this session So Request Not Send.")
//                        }
//                        else {
//                            if (adDismissCall) {
////                                if (App.remoteModel != null && App.remoteModel!!.mainScreenInter.showAd) {
////                                    loadAdmobInterstitialAds(context)
////                                }
//                            }
//                            callbackMethod.call()
//                        }
//                    }
//
//                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
//                        super.onAdFailedToShowFullScreenContent(p0)
//                        callbackMethod.call()
//                    }
//
//                    override fun onAdImpression() {
//                        super.onAdImpression()
//                    }
//                }
//        } else {
//            callbackMethod.call()
//            if (adDismissCall) {
////                if (App.remoteModel != null && App.remoteModel!!.mainScreenInter.showAd) {
////                    loadAdmobInterstitialAds(context)
////                }
//                Log.i("TAG", "showInter------: Ad not ready.")
//            }
//        }
//    }


    //PreLoad
//    fun loadAdmobInterstitialAds(context2: Context) {
//        if (App.remoteModel != null && App.remoteModel!!.mainScreenInter.showAd) {
//            if (mainInterstitialAd == null && !isSendRequest) {
//                val adRequest = AdRequest.Builder().build()
//                InterstitialAd.load(
//                    context2, App.remoteModel!!.mainScreenInter.adID, adRequest,
//                    object : InterstitialAdLoadCallback() {
//                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                            super.onAdLoaded(interstitialAd)
//                            mainInterstitialAd = interstitialAd
//                            isSendRequest=false
//                            Log.e("TAG", "onAdLoaded: Ad is ready.")
//                        }
//
//                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                            super.onAdFailedToLoad(loadAdError)
//                            mainInterstitialAd = null
//                            isSendRequest=false
//                            Log.e("TAG", "Failed Interstitial: " + loadAdError.message)
//                        }
//                    })
//                isSendRequest=true
//                Log.i("TAG", "Main-Inter-Loaded: Request sent.")
//            }
//            else
//            {
//                Log.i("TAG", "Main-Inter-Already-Loaded: ")
//            }
//        }
//    }

    //OnTheSpotLoadForPro Screen
//    fun loadAdmobInterstitialAds(context2: Context) {
//        if (App.remoteModel != null && App.remoteModel!!.mainScreenInter.showAd) {
//            if (mainInterstitialAd == null && !isSendRequest) {
//                val adRequest = AdRequest.Builder().build()
//                InterstitialAd.load(
//                    context2, App.remoteModel!!.mainScreenInter.adID, adRequest,
//                    object : InterstitialAdLoadCallback() {
//                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                            super.onAdLoaded(interstitialAd)
//                            mainInterstitialAd = interstitialAd
//                            isSendRequest =false
//                            Log.e("TAG", "onAdLoaded: Ad is ready.")
//                        }
//
//                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                            super.onAdFailedToLoad(loadAdError)
//                            mainInterstitialAd = null
//                            isSendRequest =false
//                            Log.e("TAG", "Failed Interstitial: " + loadAdError.message)
//                        }
//                    })
//                isSendRequest =true
//                Log.i("TAG", "Main-Inter-Loaded: Request sent.")
//            }
//            else
//            {
//                Log.i("TAG", "Main-Inter-Already-Loaded: ")
//            }
//        }
//    }

//
//
//
//    fun loadNative(activity: Activity, frameLayout: FrameLayout, adId: String) {
//        if (activity.checkForInternet() && !SplashAct.purchaseSuccessfull) {
//            val adLoader = AdLoader.Builder(activity, adId)
//                .forNativeAd { nativeAd ->
//                    mNativeAd = nativeAd
//                }
//                .withAdListener(object : AdListener() {
//                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                        super.onAdFailedToLoad(loadAdError)
//                        frameLayout.visibility=View.GONE
//                        Log.e("TAG", "Customize Native onAdFailedToLoad: ${loadAdError.message}")
//                    }
//
//                    override fun onAdLoaded() {
//                        super.onAdLoaded()
//
//                        if (App.remoteModel!=null && App.remoteModel!!.onBoardSmallNative.showAd)
//                        {
//                            showNativeAdsCustomize(activity, frameLayout)
//                            frameLayout.visibility=View.VISIBLE
//                            Log.i("TAG", "onAdLoaded--1st-if Run: ")
//                        }
//
//
//                    }
//                })
//                .build()
//
//            adLoader.loadAd(AdRequest.Builder().build())
//        }
//    }
//    fun showNativeAdsCustomize(activity: Activity, fLayout: FrameLayout) {
//        if (mNativeAd == null) return
//
//        val adVw = LayoutInflater.from(activity).inflate(R.layout.gnt_medium_small_media_template_view, null) as NativeAdView
//        val mediaView = adVw.findViewById<MediaView>(R.id.ad_media)
//        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//        adVw.mediaView = mediaView
//
//        adVw.headlineView = adVw.findViewById(R.id.tv_title)
//        adVw.callToActionView = adVw.findViewById(R.id.btn_click)
//        adVw.iconView = adVw.findViewById(R.id.icon_ad)
//
//        mNativeAd?.let {
//            (adVw.headlineView as TextView).text = it.headline
//
//            if (it.callToAction == null) {
//                adVw.callToActionView?.visibility = View.INVISIBLE
//            } else {
//                adVw.callToActionView?.visibility = View.VISIBLE
//                (adVw.callToActionView as TextView).text = it.callToAction
//            }
//
////            if (it.icon == null) {
////                adVw.iconView?.visibility = View.GONE
////            } else {
////                Glide.with(activity.applicationContext)
////                    .load(it.icon?.drawable)
////                    .circleCrop()
////                    .into(adVw.iconView as ImageView)
////                adVw.iconView?.visibility = View.VISIBLE
////            }
//            if (it.icon == null) {
//                adVw.iconView?.visibility = View.GONE
//            } else {
//                val drawable = it.icon?.drawable
//                if (drawable != null) {
//                    // Set the drawable directly to the ImageView
//                    (adVw.iconView as? ImageView)?.setImageDrawable(drawable)
//                }
//                adVw.iconView?.visibility = View.VISIBLE
//            }
//
//
//            adVw.setNativeAd(it)
//
//            it.mediaContent?.videoController?.let { vc ->
//                if (vc.hasVideoContent()) {
//                    vc.setVideoLifecycleCallbacks(object : VideoController.VideoLifecycleCallbacks() {
//                        override fun onVideoEnd() {
//                            super.onVideoEnd()
//                        }
//                    })
//                }
//            }
//        }
//
//        fLayout.removeAllViews()
//        fLayout.addView(adVw)
//    }
//
//
//    fun loadLastNative(activity: Activity, frameLayout: FrameLayout, adId: String) {
//        if (activity.checkForInternet() && !SplashAct.purchaseSuccessfull) {
//            val adLoader = AdLoader.Builder(activity, adId)
//                .forNativeAd { nativeAd ->
//                    mNativeAd = nativeAd
//                }
//                .withAdListener(object : AdListener() {
//                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                        super.onAdFailedToLoad(loadAdError)
//                        frameLayout.visibility=View.GONE
//                        Log.e("TAG", "Customize Native onAdFailedToLoad: ${loadAdError.message}")
//                    }
//
//                    override fun onAdLoaded() {
//                        super.onAdLoaded()
//                        if (App.remoteModel!=null && App.remoteModel!!.onBoardSmallNativeLastItem.showAd) {
//                            // showNativeAdsCustomize(activity, frameLayout)
//                            Log.i("TAG", "onAdLoaded--2nd-else if Run: ")
//                        }
//
//                    }
//                })
//                .build()
//
//            adLoader.loadAd(AdRequest.Builder().build())
//        }
//    }
//    fun showLastNativeAdsCustomize(activity: Activity, fLayout: FrameLayout) {
//        if (mNativeAd == null) return
//
//        val adVw = LayoutInflater.from(activity).inflate(R.layout.gnt_medium_small_media_template_view, null) as NativeAdView
//        val mediaView = adVw.findViewById<MediaView>(R.id.ad_media)
//        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)
//        adVw.mediaView = mediaView
//
//        adVw.headlineView = adVw.findViewById(R.id.tv_title)
//        adVw.callToActionView = adVw.findViewById(R.id.btn_click)
//        adVw.iconView = adVw.findViewById(R.id.icon_ad)
//
//        mNativeAd?.let {
//            (adVw.headlineView as TextView).text = it.headline
//
//            if (it.callToAction == null) {
//                adVw.callToActionView?.visibility = View.INVISIBLE
//            } else {
//                adVw.callToActionView?.visibility = View.VISIBLE
//                (adVw.callToActionView as TextView).text = it.callToAction
//            }
//
////            if (it.icon == null) {
////                adVw.iconView?.visibility = View.GONE
////            } else {
////                Glide.with(activity.applicationContext)
////                    .load(it.icon?.drawable)
////                    .circleCrop()
////                    .into(adVw.iconView as ImageView)
////                adVw.iconView?.visibility = View.VISIBLE
////            }
//            if (it.icon == null) {
//                adVw.iconView?.visibility = View.GONE
//            } else {
//                val drawable = it.icon?.drawable
//                if (drawable != null) {
//                    // Set the drawable directly to the ImageView
//                    (adVw.iconView as? ImageView)?.setImageDrawable(drawable)
//                }
//                adVw.iconView?.visibility = View.VISIBLE
//            }
//
//
//            adVw.setNativeAd(it)
//
//            it.mediaContent?.videoController?.let { vc ->
//                if (vc.hasVideoContent()) {
//                    vc.setVideoLifecycleCallbacks(object : VideoController.VideoLifecycleCallbacks() {
//                        override fun onVideoEnd() {
//                            super.onVideoEnd()
//                        }
//                    })
//                }
//            }
//        }
//
//        fLayout.removeAllViews()
//        fLayout.addView(adVw)
//    }

}

