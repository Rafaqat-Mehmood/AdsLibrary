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
 > Banner Ad. Implemenation
```Kotlin
 // 1st Parameter = Context or Activity
 // 2nd Parmter = adContainer is the layout of Ads
 // 3rd Parameter = Test Ad Id
 // 4th Parameter = Ads ON or Off then Shiffer Effect Visibility Gone
 val adContainer = findViewById<ViewGroup>(R.id.adContainer) 
 NewAdManager.loadAndShowSmallBanner(this, adContainer as ViewGroup,"ca-app-pub-3940256099942544/6300978111", false)
```
```xml
<!--    Ads Show Top then Constaints Attached Top-->
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
