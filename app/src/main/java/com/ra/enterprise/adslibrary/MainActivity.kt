package com.ra.enterprise.adslibrary

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ra.enterprise.ConsentCallback
import com.ra.enterprise.admoblibrary.NewAdManager
import com.ra.enterprise.admoblibrary.controller.ConsentController
import com.ra.enterprise.admoblibrary.enums.CMPStatus
import com.ra.enterprise.admoblibrary.enums.NativeAdType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)
    }

}