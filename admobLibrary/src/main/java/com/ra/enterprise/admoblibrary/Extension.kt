package com.ra.enterprise.admoblibrary

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.os.Looper
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintAttributes.COLOR_MODE_COLOR
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.print.pdf.PrintedPdfDocument

import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast


fun Context.checkForInternet(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        // if the android version is below M
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }

}


//
// fun Context.loadBottomSmallNative(activity:Activity,id: String, bottomNative: FrameLayout) {
//        if (!SplashAct.purchaseSuccessfull && checkForInternet()) {
//            NewAdManager.loadNative(
//                activity,
//                bottomNative,
//                id
//            )
//        }
//}
//fun Context.loadBottomLastSmallNative(activity:Activity,id: String, bottomNative: FrameLayout) {
//    if (!SplashAct.purchaseSuccessfull && checkForInternet()) {
//        NewAdManager.loadLastNative(
//            activity,
//            bottomNative,
//            id
//        )
//    }
//}
//fun Context.loadSmallBanner(id: String, view: LinearLayout,activity:Activity) {
//    if (!SplashAct.purchaseSuccessfull && checkForInternet()) {
//        NewAdManager.loadSmallBanner(
//            activity,
//            view,
//            id
//        )
//    }
//}
//
//fun Context.loadCollapsibleAd(check: String, view: LinearLayout) {
//    if (!SplashAct.purchaseSuccessfull && checkForInternet()) {
//        NewAdManager.showCollapsibleAd(this, view, check)
//
//    }
//}
//
//fun Context.loadMediumAd(check: String, view: LinearLayout) {
//    if (!SplashAct.purchaseSuccessfull && checkForInternet()) {
//        NewAdManager.showBannerMediumAd(
//            this,
//            view,
//            check
//        )
//    }
//}

fun Context.moveAct(cl:Class<*>,value:String){
    val intent=Intent(this, cl).apply {
        flags=Intent.FLAG_ACTIVITY_SINGLE_TOP
        putExtra("key",value)
    }
    startActivity(intent)

}
fun Context.moveAct(activity: Activity,cl:Class<*>,value: String){
    val intent=Intent(this, cl).apply {
        flags=Intent.FLAG_ACTIVITY_SINGLE_TOP
        putExtra("back",value)
    }
    startActivity(intent)
    activity.finish()
}

fun Context.moveAct(activity: Activity,cl: Class<*>) {
    val intent = Intent(this, cl).apply {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }
    startActivity(intent)
    activity.finish()
}







