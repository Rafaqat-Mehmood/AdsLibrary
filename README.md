# AdsLibrary

[![JitPack](https://jitpack.io/v/Rafaqat-Mehmood/AdsLibrary.svg)](https://jitpack.io/#Rafaqat-Mehmood/AdsLibrary)

AdsLibrary is an Android library designed to simplify the integration of various types of ads into your mobile application. It supports banner ads, native ads, interstitial ads, and open ads. This document explains every step in detail—from setting up your project to customizing layouts—so that developers of all experience levels can follow along.

---

## Table of Contents

1. [Installation](#installation)
   - [Adding the JitPack Repository](#adding-the-jitpack-repository)
   - [Adding the Dependency](#adding-the-dependency)
2. [Banner Ads Implementation](#banner-ads-implementation)
   - [Overview](#banner-ads-overview)
   - [Kotlin Code Examples](#banner-ads-kotlin-code-examples)
   - [XML Layout Details](#banner-ads-xml-layout-details)
3. [Native Ads Implementation](#native-ads-implementation)
   - [Overview](#native-ads-overview)
   - [Kotlin Code Examples](#native-ads-kotlin-code-examples)
   - [XML Layout Details](#native-ads-xml-layout-details)
4. [Interstitial Ads Implementation](#interstitial-ads-implementation)
   - [Main Interstitial Ads](#main-interstitial-ads)
   - [Splash/Onboarding/Pro Screen Interstitial Ads](#splash-onboarding-pro-screen-interstitial-ads)
5. [Open Ads Implementation](#open-ads-implementation)
   - [Splash Screen Open Ads](#splash-screen-open-ads)
   - [App-Wide Open Ads](#app-wide-open-ads)

---

## 1. Installation

Before you begin integrating AdsLibrary, you need to set up your project to use the library.

### Adding the JitPack Repository

AdsLibrary is hosted on JitPack. To fetch the library, add the following repository to your root `settings.gradle` file. This tells Gradle where to look for dependencies not available in the default repositories.

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        // Adding JitPack repository to fetch AdsLibrary
        maven { url = uri("https://jitpack.io") }
    }
}
```
 ### Adding the Dependency
After setting up the repository, add AdsLibrary as a dependency in your module’s build.gradle file:
```gradle
dependencies {
	        implementation("com.github.Rafaqat-Mehmood:AdsLibrary:1.3")
	}
```
Explanation:

Replace "Tag" with the actual version number you intend to use (for example, "1.0.0").
This line instructs Gradle to include AdsLibrary in your project during compilation.

## 2. Banner Ads Implementation
Banner ads are the most commonly used ad format. They come in different sizes and styles. This section explains how to implement various banner ads.

### Banner Ads Overview
AdsLibrary supports the following banner ad types:

Fixed Size Banner: A standard banner with set dimensions.
Large Banner: For larger spaces.
Medium Banner: A balanced size between small and large.
Collapsible Banner: Can be positioned at the top or bottom and collapse when not needed.

 ### Banner Ads Kotlin Code Examples
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
### Banner Ads XML Layout Details
You need a dedicated container in your layout where banner ads will be shown. Create a layout file (for example, banner_container.xml) that defines the size and behavior of your ad container.

Example XML for Banner Ads Container

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
## 3. Native Ads Implementation
Native ads let you tailor the ad’s appearance to match your app’s design. You can choose from several templates depending on whether you want an image-only ad or a layout with media elements.

### Native Ads Overview
AdsLibrary provides three native ad formats:

IMAGE: Displays a native ad focusing solely on an image.
SMALL_MEDIA: Uses a compact layout with a small media element (such as a video thumbnail or small image).
LARGE_MEDIA: Emphasizes larger media content and is suitable for rich visuals.

### Native Ads Kotlin Code Examples
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
### Native Ads XML Layout Details
You need to prepare XML layout files for different native ad templates so that the ad content fits nicely within your app’s design.

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
## 4. Interstitial Ads Implementation
Interstitial ads are full-screen ads that appear at natural transition points in your app (for example, after a level is completed or before navigating to a new screen).

### Main Interstitial Ads
This method allows you to show an interstitial ad every nth click and limits the total number of ads per session.

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

### Splash/Onboarding/Pro Screen Interstitial Ads
For scenarios like splash screens or onboarding, use a dedicated method:

```Kotlin
 // 1st Parameter = Context or Activity
 // 2nd Parameter = Ads ids

   NewAdManager.interLoad(this,"ca-app-pub-3940256099942544/1033173712"){
    // Callback: Called when the ad is dismissed or fails to load.

        }
```

## 5. Open Ads Implementation
Open ads are used to display a full-screen ad when launching the app or in other situations where you want to block user interaction until the ad is dismissed.

### Splash Screen Open Ads
Implement open ads on your splash screen with the following two-step process:

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

## App-Wide Open Ads
Configure your Application class to globally manage open ads. Extend the provided Application class from AdsLibrary.

```Kotlin
 // 1st Parameter = Context or Activity
 // 2nd Parameter = Ads ids
// 3rd Parameter = if show ad then true pass if OFF Ads then false add

 // Your App class extend My App Class
 class App : com.ra.enterprise.admoblibrary.App()
 {

   // if user purchase then purchase variable store in library variable
    this.isPurchase=SplashAct.purchaseSuccessfull

        this.adId = "ca-app-pub-3940256099942544/9257395921"
// block the screen which or not show the open Ads that Screen
  // Pass Your Activity Name which You are Block
        this.blockScreenList(listOf("AdActivity","SplashAct")) // Call the method on 'this'
        this.showAd=false

}

```
