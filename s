[1mdiff --git a/settings.gradle.kts b/settings.gradle.kts[m
[1mnew file mode 100644[m
[1mindex 0000000..3027c70[m
[1m--- /dev/null[m
[1m+++ b/settings.gradle.kts[m
[36m@@ -0,0 +1,34 @@[m
[32m+[m[32mpluginManagement {[m
[32m+[m[32m    repositories {[m
[32m+[m[32m        // Your repo from where Gradle will search for Gradle plugins[m
[32m+[m[32m        google {[m
[32m+[m[32m            content {[m
[32m+[m[32m                includeGroupByRegex("com\\.android.*")[m
[32m+[m[32m                includeGroupByRegex("com\\.google.*")[m
[32m+[m[32m                includeGroupByRegex("androidx.*")[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m[32m        mavenCentral()[m
[32m+[m[32m        gradlePluginPortal()[m
[32m+[m[32m    }[m
[32m+[m[32m    resolutionStrategy {[m
[32m+[m[32m        eachPlugin {[m
[32m+[m[32m            if( requested.id.id == "dagger.hilt.android.plugin") {[m
[32m+[m[32m                useModule("com.google.dagger:hilt-android-gradle-plugin:2.44")[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m[32m    }[m
[32m+[m[32m}[m
[32m+[m[32mdependencyResolutionManagement {[m
[32m+[m[32m    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)[m
[32m+[m[32m    repositories {[m
[32m+[m[32m        google()[m
[32m+[m[32m        mavenCentral()[m
[32m+[m[32m    }[m
[32m+[m[32m}[m
[32m+[m
[32m+[m[32mrootProject.name = "xJournal"[m
[32m+[m[32minclude(":app")[m
[32m+[m
[32m+[m[32m//for git testing sake[m
[32m+[m[32m//for checkout revert testing sake[m
\ No newline at end of file[m
