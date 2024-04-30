// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript{
//    ext{
//        //compose_version = "1.6.5"
//    }
//}

// plugin in this is used to apply the plugins to the project
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

}