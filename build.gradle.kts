buildscript {
    repositories {
        google()  // Google's Maven repository
        mavenCentral()
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.

allprojects {
    ext.apply {
        set("compose_version", "1.5.0-beta03")
        set("material3_version", "1.2.0-alpha02")
        set("kspVersion", "1.8.0-1.0.8")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    id("org.jetbrains.kotlin.android") version "1.8.22" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.4" apply false
}