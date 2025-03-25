package com.ra.enterprise.admoblibrary.controller

import android.app.Activity
import android.content.Context
import android.util.Log
import com.ra.enterprise.admoblibrary.enums.CMPStatus
import com.facebook.shimmer.BuildConfig
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.ConsentStatus
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.ra.enterprise.ConsentCallback
import java.util.EnumMap


class ConsentController(private val activity: Activity) {

    val TAG = "consentControllerTAG"

    private var consentInformation: ConsentInformation? = null
    private var consentCallback: ConsentCallback? = null

    val canRequestAds: Boolean get() = consentInformation?.canRequestAds() ?: false

    fun initConsent(
        @Debug("Device Id is only use for DEBUG") deviceId: String,
        callback: ConsentCallback
    ) {
        this.consentCallback = callback

        val isDebug = BuildConfig.DEBUG

        val debugSettings = ConsentDebugSettings.Builder(activity)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId(deviceId)
            .build()

        val params = if (isDebug) {
            Log.d(TAG, "Debug parameters setConsentDebugSettings")
            ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build()
        } else {
            Log.d(TAG, "Release parameters setTagForUnderAgeOfConsent")
            ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build()
        }

        consentInformation = UserMessagingPlatform.getConsentInformation(activity).also {
            if (isDebug) {
                Log.d(TAG, "Consent Form reset() in Debug")
                it.reset()
            }else{
                Log.d(TAG, "All is OK not Reset in release")
            }

            consentCallback?.onReadyForInitialization()
            Log.d(TAG, "onReadyForInitialization")
            it.requestConsentInfoUpdate(activity, params, {
                consentCallback?.onInitializationSuccess()
                consentCallback?.onConsentFormAvailability(it.isConsentFormAvailable)
                Log.d(TAG, "onInitializationSuccess")
                Log.d(TAG, "onConsentFormAvailability, available: ${it.isConsentFormAvailable}")
                if (it.isConsentFormAvailable) {
                    when(consentInformation?.consentStatus){
                        ConsentStatus.REQUIRED -> {
                            consentCallback?.onConsentStatus(CMPStatus.REQUIRED)
                            Log.d(TAG, "consentStatus: REQUIRED")
                            loadConsentForm()
                        }
                        ConsentStatus.NOT_REQUIRED -> {
                            consentCallback?.onConsentStatus(CMPStatus.NOT_REQUIRED)
                            Log.d(TAG, "consentStatus: NOT_REQUIRED")
                        }
                        ConsentStatus.OBTAINED -> {
                            consentCallback?.onConsentStatus(CMPStatus.OBTAINED)
                            Log.d(TAG, "consentStatus: OBTAINED")
                        }
                        ConsentStatus.UNKNOWN -> {
                            consentCallback?.onConsentStatus(CMPStatus.UNKNOWN)
                            Log.d(TAG, "consentStatus: UNKNOWN")
                        }
                        else -> {
                            Log.d(TAG, "consentInformation is null")
                        }

                    }
                    when(consentInformation?.privacyOptionsRequirementStatus){
                        ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED -> {
                            consentCallback?.onPolicyStatus(CMPStatus.REQUIRED)
                            Log.d(TAG, "privacyOptionsRequirementStatus: REQUIRED")
                        }
                        ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED -> {
                           // consentCallback?.onPolicyStatus(CMPStatus.NOT_REQUIRED)
                            Log.d(TAG, "privacyOptionsRequirementStatus: NOT_REQUIRED")
                        }
                        ConsentInformation.PrivacyOptionsRequirementStatus.UNKNOWN -> {
                           // consentCallback?.onPolicyStatus(CMPStatus.UNKNOWN)
                            Log.d(TAG, "privacyOptionsRequirementStatus: UNKNOWN")
                        }
                        else -> {
                            Log.d(TAG, "consentInformation is null")
                        }
                    }

                }
            }, { error ->
                consentCallback?.onInitializationError(error.message)
                Log.e(TAG, "onInitializationError: ${error.message}")
            })
        }
    }

    private fun loadConsentForm() {
        UserMessagingPlatform.loadConsentForm(activity, { consentForm ->
            consentCallback?.onConsentFormLoadSuccess()
            Log.d(TAG, "onConsentFormLoadSuccess")
            showConsentForm(consentForm)
        }) { formError ->
            consentCallback?.onConsentFormLoadFailure(formError.message)
            Log.e(TAG, "onConsentFormLoadFailure: ${formError.message}")
        }
    }

    private fun showConsentForm(consentForm: ConsentForm) {
        consentCallback?.onRequestShowConsentForm()
        Log.d(TAG, "onRequestShowConsentForm")
        consentForm.show(activity) { formError ->
            consentCallback?.onConsentFormDismissed()
            Log.d(TAG, "onConsentFormDismissed")
            formError?.let {
                consentCallback?.onConsentFormShowFailure(it.message)
                Log.e(TAG, "onConsentFormShowFailure: ${it.message}")
            } ?: run {
                checkConsentAndPrivacyStatus()
            }
        }
    }

    private fun checkConsentAndPrivacyStatus() {
        Log.d(TAG, "check Consent And Privacy Status After Form Dismissed")
        when(consentInformation?.consentStatus){
            ConsentStatus.REQUIRED -> {
                Log.d(TAG, "consentStatus: REQUIRED")
            }
            ConsentStatus.NOT_REQUIRED -> {
                Log.d(TAG, "consentStatus: NOT_REQUIRED")
            }
            ConsentStatus.OBTAINED -> {
                Log.d(TAG, "consentStatus: OBTAINED")


            }
            ConsentStatus.UNKNOWN -> {
                Log.d(TAG, "consentStatus: UNKNOWN")
            }
            else -> {
                Log.d(TAG, "consentInformation is null")
            }
        }
        when(consentInformation?.privacyOptionsRequirementStatus){
            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED -> {
                Log.d(TAG, "privacyOptionsRequirementStatus: REQUIRED")
            }
            ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED -> {
                Log.d(TAG, "privacyOptionsRequirementStatus: NOT_REQUIRED")
            }
            ConsentInformation.PrivacyOptionsRequirementStatus.UNKNOWN -> {
                Log.d(TAG, "privacyOptionsRequirementStatus: UNKNOWN")
            }
            else -> {
                Log.d(TAG, "consentInformation is null")
            }
        }
    }

    annotation class Debug(val message: String = "For Debug Feature")
}