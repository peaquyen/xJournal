pluginManagement {
    repositories {
        // Your repo from where Gradle will search for Gradle plugins
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven(url = "https://jitpack.io")
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "dagger.hilt.android.plugin") {
                useModule("com.google.dagger:hilt-android-gradle-plugin:2.44")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "xJournal"
include(":app")

//buildscript {
//    ext {
//        compose_version = "1.5.1"
//    }
//    dependencies {
//        classpath ("com.google.gms:google-services:4.3.14")
//    }
//}

plugins {
    id("com.android.application") version "8.3.1" apply false
    id("com.android.library") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("dagger.hilt.android.plugin") version "2.44" apply false
    id("io.realm.kotlin") version "1.14.1" apply false
}