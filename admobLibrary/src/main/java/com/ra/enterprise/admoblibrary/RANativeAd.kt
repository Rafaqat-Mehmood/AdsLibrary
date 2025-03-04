package com.ra.enterprise.admoblibrary

import android.content.Context
import android.util.Log
import android.view.View


//object RANativeAd {
//
//    var nativeAd: com.google.android.gms.ads.nativead.NativeAd? = null
//    var isAdLoaded: Boolean = false
//    private val adDisplayStateMap = mutableMapOf<String, Boolean>() // Map to track display state per adId
//
//    // Load Ad method
//    fun loadAd(context: Context, adId: String, onAdLoaded: (() -> Unit)? = null) {
//        // Only load if no ad is currently loaded and displayed for this adId
//        if (isAdLoaded && adDisplayStateMap[adId] == false) {
//            Log.i("NativeAdLoader", "Ad is loaded but not displayed yet for adId: $adId. Waiting before loading a new ad.")
//            return
//        }
//
//        val adLoader = AdLoader.Builder(context, adId)
//            .forNativeAd { nativeAd ->
//                RANativeAd.nativeAd = nativeAd
//                isAdLoaded = true
//                adDisplayStateMap[adId] = false // Set display state to false when a new ad is loaded
//                Log.i("NativeAdLoader", "Ad successfully loaded for adId: $adId")
//
//                // Notify that the ad is loaded
//                onAdLoaded?.invoke()
//            }
//            .withAdListener(object : AdListener() {
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    super.onAdFailedToLoad(loadAdError)
//                    isAdLoaded = false
//                    Log.i("NativeAdLoader", "Failed to load ad for adId: $adId, error: ${loadAdError.message}")
//                }
//            })
//            .withNativeAdOptions(
//                NativeAdOptions.Builder()
//                    .setRequestCustomMuteThisAd(true)
//                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT)
//                    .build()
//            )
//            .build()
//
//        adLoader.loadAd(AdRequest.Builder().build())
//    }
//
//    // Show Ad method
//    fun showAd(
//        adContainer: TemplateView, advertisingArea: View, adId: String
//    ) {
//        if (isAdLoaded && nativeAd != null) {
//            adContainer.setNativeAd(nativeAd)
//            advertisingArea.visibility = View.VISIBLE
//            adDisplayStateMap[adId] = true  // Mark the ad as displayed for this adId
//            Log.i("NativeAdLoader", "Ad displayed for adId: $adId")
//
//            // Optionally, load the next ad after the first is shown
//            // loadAd(context, adId) // Uncomment to preload the next ad
//        } else {
//            advertisingArea.visibility = View.GONE
//            Log.i("NativeAdLoader", "Ad is not ready yet for adId: $adId")
//        }
//    }
//
//    // Check if ad was displayed for a given adId
//    fun isAdDisplayedForId(adId: String): Boolean {
//        return adDisplayStateMap[adId] == true
//    }
//}
