// your app's source code, resource files, and app-level settings, such as the module-level
// build file and Android Manifest file

plugins {
    // Android application plugin, Kotlin Android plugin, Realm, Dagger Hilt, and Google Services
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("io.realm.kotlin") version "1.14.1"// Realm is a mobile database that runs
    // directly
    id("dagger.hilt.android.plugin") //version "2.38.1'" apply false
    // version "2.38.1" // Hilt is a dependency injection library
    // for Android
    id("com.google.gms.google-services") version "4.3.14" apply false
    // services to your project
    id("kotlin-kapt")
}

android {
    // configuration specific to the Android application
    namespace = "com.github.peaquyen.xJournal"
    compileSdk = 34

    // sets the default configuration for all build types. This block is optional
    defaultConfig {
        applicationId = "com.github.peaquyen.xJournal"
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

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ROOM components
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))

    // Runtime Compose
    implementation("androidx.compose.runtime:runtime:1.3.3")
    implementation("androidx.compose.runtime:runtime-livedata:1.3.3")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.3.3")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // MongoDB Realm
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0") {
        version {
            strictly("1.7.1")
        }
    }
    implementation("io.realm.kotlin:library-sync:1.14.1")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Google Auth
    // implementation("com.google.android.gms:play-services-auth:19.2.0")

    // Coil: An image loading library for Android backed by Kotlin Coroutines.
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Pager - Accompanist: -> This library is deprecated, with official pager
    // support in androidx.compose.foundation.pager.
    implementation("com.google.accompanist:accompanist-pager:0.35.0-alpha")

    // Date-Time Picker -> should change to official library
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    // Message Bar Compose: -> ok
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.8")

    // One-Tap Compose: easily integrate One-Tap Sign in with Google
    // -> oh no
//    implementation("com.github.stevdza-san.OneTapCompose:1.0.11")

    // Desugar JDK: help certain code in the app without api required
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

}

// The kotlin block configures the Kotlin plugin for the project.
kapt {
    correctErrorTypes = true
}
