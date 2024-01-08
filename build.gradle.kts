buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.android.tools.build:gradle:4.1.3")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}