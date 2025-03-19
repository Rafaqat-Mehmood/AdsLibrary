[![](https://jitpack.io/v/Rafaqat-Mehmood/AdsLibrary.svg)](https://jitpack.io/#Rafaqat-Mehmood/AdsLibrary)

> Step 1. Add the JitPack repository to your build file
Add it in your root settings.gradle at the end of repositories:

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
 // 1st Parameter = Context or Activity
 // 2nd Parmter = adContainer is the layout of Ads
 // 3rd Parameter = Test Ad Id
 // 4th Parameter = Ads ON or Off then Shiffer Effect Visibility Gone

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
<!--    Ads Show Top then Constaints Attached Top-->
<!--     1-> small banner show then  this pass -->
<!--      layout="@layout/banner_container"-->
    
<!--     2-> Rectangle banner show then  this pass -->
<!--      layout="@layout/banner_rectangle_container"-->
    
<!--     3-> Rectangle banner show then  this pass -->
<!--      layout="@layout/large_banner_container"-->

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
<!--    Ads Show Top then Constaints Attached Top-->
<!--     1-> Native Ads without Media show then  this pass -->
<!--      layout="@layout/native_container_without_media"-->
    
<!--     2-> Native Ads Large Media or Custom Meida show then  this pass -->
<!--      layout="@layout/native_container_large_media"-->
    
<!--     3-> Native Ads Small Media show then  this pass -->
<!--      layout="@layout/native_container_small_media"-->

<include
        layout="@layout/native_container_without_media"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopf="parent"/>

<!--    Ads Show Bottom then Constaints Attached Bottom-->
<include
        layout="@layout/native_container_without_media"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"/>
```
