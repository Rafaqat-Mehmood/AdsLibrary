# AdsLibrary

[![JitPack](https://jitpack.io/v/Rafaqat-Mehmood/AdsLibrary.svg)](https://jitpack.io/#Rafaqat-Mehmood/AdsLibrary)

AdsLibrary is a lightweight Android library to easily integrate banner, native, and interstitial ads into your app. Enjoy simple methods to load and display ads with customizable layouts and behavior.

---

## Table of Contents
- [Configure](#implementation)
- [Banner Ads Implementation](#banner-ads-implementation)
- [Native Ads Implementation](#native-ads-implementation)
- [Interstitial Ads Implementation](#interstitial-ads-implementation)
- [Open Ads Implementation](#open-ads-implementation)
- [Splash Open Ads Implementation](#splash-open-ads-implementation)
---

## Configure

### Step 1: Add the JitPack Repository

Add the repository in your root `settings.gradle` file:
```gradle
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}
```
 > Step 2. Add the dependency
```gradle
dependencies {
	        implementation("com.github.Rafaqat-Mehmood:AdsLibrary:Tag")
	}
```
 > Banner Ads. Implemenation
```Kotlin
 // 1st: Context or Activity
// 2nd: adContainer (layout container for ads)
// 3rd: Test Ad ID
// 4th: Ads ON/OFF (if false, shimmer effect is hidden)

 // A-> This Method is Fixed Size Banner Ad
 val adContainer = findViewById<ViewGroup>(R.id.adContainer) 
 NewAdManager.loadAndShowSmallBanner(this, adContainer as ViewGroup,"ca-app-pub-3940256099942544/6300978111", false)

// B-> This Method is Large Size Banner Ad
NewAdManager.loadAndShowLargeBanner(this, adContainer as ViewGroup,"ca-app-pub-3940256099942544/2247696110", false)

// C-> This Method is Medium Size Banner Ad
 NewAdManager.loadAndShowMediumBanner(this, adContainer as ViewGroup,"ca-app-pub-3940256099942544/9214589741", false)

// D-> This Method is Collapsible Size Banner Ad
   // 1-> if Collapsible show top and close top then Pass the Parameter "top"
   // 2->  if Collapsible show bottom and close bottom then Pass the Parameter "bottom"
NewAdManager.loadAndShowCollapsibleAd(this, adContainer as ViewGroup,"top","ca-app-pub-3940256099942544/2247696110", false)


```
```xml
<!--     1-> small banner show then  this pass -->
<!--      layout="@layout/banner_container"-->
    
<!--     2-> Rectangle banner show then  this pass -->
<!--      layout="@layout/banner_rectangle_container"-->
    
<!--     3-> Rectangle banner show then  this pass -->
<!--      layout="@layout/large_banner_container"-->


<!-- Ads Shown at the Top -->
<include
        layout="@layout/banner_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopf="parent"/>

<!--    Ads Show Bottom then Constaints Attached Bottom-->
<include
        layout="@layout/banner_container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"/>
```
> Native Ads. Implemenation
```Kotlin
 // 1st Parameter = Context or Activity
 // 2nd Parmter = adContainer is the layout of Ads

 // 3rd Parmter = Show Different Type of Ad
 // a-> IMAGE
 // b-> SMALL MEDIA
 // c-> LARGE_MEDIA

 // 4th Parameter = Test Ad Id
 // 5th Parameter = Ads ON or Off then Shiffer Effect Visibility Gone

 // A-> This Method is IMAGE Native Ad
 val adContainer = findViewById<ViewGroup>(R.id.adContainer) 
 NewAdManager.loadAndShowNativeAd(this, adContainer as ViewGroup,NativeAdType.IMAGE,"ca-app-pub-3940256099942544/2247696110", false)


// B-> This Method is SMALL MEDIA Native Ad
NewAdManager.loadAndShowNativeAd(this, adContainer as ViewGroup,NativeAdType.SMALL_MEDIA,"ca-app-pub-3940256099942544/1044960115", false)

// C-> This Method is LARGE MEDIA Native Ad
NewAdManager.loadAndShowNativeAd(this, adContainer as ViewGroup,NativeAdType.LARGE_MEDIA,"ca-app-pub-3940256099942544/1044960115", false)

```
```xml
<!--     1-> Native Ads without Media show then  this pass -->
<!--      layout="@layout/native_container_without_media"-->
    
<!--     2-> Native Ads Large Media or Custom Meida show then  this pass -->
<!--      layout="@layout/native_container_large_media"-->
    
<!--     3-> Native Ads Small Media show then  this pass -->
<!--      layout="@layout/native_container_small_media"-->

<!-- Native Ad without Media (Top) -->
<include
        layout="@layout/native_container_without_media"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopf="parent"/>

<!-- Native Ad without Media (Bottom) -->
<include
        layout="@layout/native_container_without_media"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"/>
```
> Main Interstitial Ads. Implemenation (Seperate Ads Id)
```Kotlin
 // Parameters:
// 1. Context or Activity
// 2. Frequency (e.g., show every 3rd click)
// 3. Initial count (starting from 0)
// 4. Maximum ads per session (25 ads per user)
// 5. Ad ID

  NewAdManager.loadAndShow(this,3,0,25,"ca-app-pub-3940256099942544/1033173712"){
    // Callback: Called when the ad is dismissed or fails to load.
        }
```

> Splash Screen OR Onboarding Screen OR Pro Screen Interstitial Ads. Implemenation (Seperate Ads Id)
```Kotlin
 // 1st Parameter = Context or Activity
 // 2nd Parameter = Ads ids

   NewAdManager.interLoad(this,"ca-app-pub-3940256099942544/1033173712"){
    // Callback: Called when the ad is dismissed or fails to load.

        }
```

> Splash Screen Open Ads. Implemenation (Seperate Ads Id)
```Kotlin
 // 1st Parameter = Context or Activity
 // 2nd Parameter = Ads ids
// 3rd Parameter = if show ad then true pass if OFF Ads then false add

  OpenAdUseForSplash.fetchAd(this,
            "ca-app-pub-3940256099942544/9257395921",true)
        {
    // Callback: Called when the ad is dismissed or fails to load.

        }
        OpenAdUseForSplash.showAdIfAvailable(this@SplashAct){
    // Callback: Called when the ad is dismissed or fails to load.
        }
```

> Over All Show Open Ads. Implemenation (Seperate Ads Id)
```Kotlin
 // 1st Parameter = Context or Activity
 // 2nd Parameter = Ads ids
// 3rd Parameter = if show ad then true pass if OFF Ads then false add

 // Your App class extend My App Class
 class App : com.ra.enterprise.admoblibrary.App()
 {

   // if user purchase then purchase variable store in library variable
   App().isPurchase=SplashAct.purchaseSuccessfull

   // if Ads OFF OR ON then pass true or false
   App().showAd=true

            // check condition user purchase or not and 2nd check adS ON or OFF
   if (!SplashAct.purchaseSuccessfull && remoteModel!!.openAd.showAd)
   {
       adId= "ca-app-pub-3940256099942544/9257395921"
  // block the screen which or not show the open Ads that Screen
  // Pass Your Activity Name which You are Block
  blockScreenList(listOf("AdActivity","SplashAct","ProAct","HowToUseAct"))
  }

}
```
