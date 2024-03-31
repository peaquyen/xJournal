// your app's source code, resource files, and app-level settings, such as the module-level
// build file and Android Manifest file

plugins {
    // Android application plugin, Kotlin Android plugin, Realm, Dagger Hilt, and Google Services
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("io.realm.kotlin") version "1.14.1"// Realm is a mobile database that runs
    // directly
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin") //version "2.38.1'" apply false
    // version "2.38.1" // Hilt is a dependency injection library
    // for Android
    id ("com.google.gms.google-services") version "4.3.14" apply false
    // services to your project
}

android {
    // configuration specific to the Android application
    namespace = "com.github.peaquyen.diaryapp"
    compileSdk = 34

    // sets the default configuration for all build types. This block is optional
    defaultConfig {
        applicationId = "com.github.peaquyen.diaryapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    // sets the configuration for the release build type. This block is optional
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // The compileOptions and kotlinOptions blocks set the source and target
    // compatibility for Java and Kotlin respectively.
    // e.g: ìf jvmTarget != 1.8, it will cause error when building the project
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // The buildFeatures block enables or disables specific features of the Android
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// The dependencies block declares the dependencies for the project.
// These include libraries for AndroidX, Jetpack Compose, testing,
// and more. For example, implementation(libs.androidx.core.ktx)
// includes the AndroidX Core KTX library in the project.
dependencies {
    // dagger hilt (2)
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// The kotlin block configures the Kotlin plugin for the project.
kapt {
    correctErrorTypes = true
}
