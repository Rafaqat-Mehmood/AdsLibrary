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
	        implementation("com.github.Rafaqat-Mehmood:AdsLibrary:1.0")
	}
```
