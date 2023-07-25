plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization.plugin)
    id ("kotlin-parcelize")
    alias(libs.plugins.ksp)
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
//    packagingOptions {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
    namespace = "home.saied.samples"
    kotlin {
        this@android.sourceSets {
            getByName("main") {
                kotlin.srcDir("build/generated/ksp/debug/kotlin")
                kotlin.srcDir("../support/compose/ui/ui/samples/src/main/java/androidx/compose/ui/samples")
                kotlin.srcDir("../support/compose/animation/animation/samples/src/main/java/androidx/compose/animation/samples")
                kotlin.srcDir("../support/compose/material3/material3/samples/src/main/java/androidx/compose/material3/samples")
                kotlin.srcDir("../support/compose/foundation/foundation-layout/samples/src/main/java/androidx/compose/foundation/layout/samples")
                kotlin.srcDir("../support/compose/foundation/foundation/samples/src/main/java/androidx/compose/foundation/samples")
                kotlin.srcDir("../support/compose/ui/ui-unit/samples/src/main/java/androidx/compose/ui/unit/samples")
                kotlin.srcDir("../support/compose/ui/ui-graphics/samples/src/main/java/androidx/compose/ui/graphics/samples")
    //                kotlin.srcDir("../support/compose/ui/ui-viewbinding/samples/src/main/java/androidx/compose/ui/samples")
                kotlin.srcDir("../support/compose/ui/ui-text/samples/src/main/java/androidx/compose/ui/text/samples")
                kotlin.srcDir("../support/compose/material/material-icons-core/samples/src/main/java/androidx/compose/material/icons/samples")
                kotlin.srcDir("../support/compose/material/material/samples/src/main/java/androidx/compose/material/samples")
                kotlin.srcDir("../support/compose/animation/animation-graphics/samples/src/main/java/androidx/compose/animation/graphics/samples")
                kotlin.srcDir("../support/compose/animation/animation-core/samples/src/main/java/androidx/compose/animation/core/samples")
                kotlin.srcDir("../support/compose/runtime/runtime-rxjava3/samples/src/main/java/androidx/compose/runtime/rxjava3/samples")
                kotlin.srcDir("../support/compose/runtime/runtime-rxjava2/samples/src/main/java/androidx/compose/runtime/rxjava2/samples")
                kotlin.srcDir("../support/compose/runtime/runtime-saveable/samples/src/main/java/androidx/compose/runtime/saveable/samples")
                kotlin.srcDir("../support/compose/runtime/runtime-livedata/samples/src/main/java/androidx/compose/runtime/livedata/samples")
                kotlin.srcDir("../support/compose/runtime/runtime/samples/src/main/java/androidx/compose/runtime/samples")
                kotlin.srcDir("../support/activity/activity-compose/samples/src/main/java/androidx/activity/compose/samples")
                kotlin.srcDir("../support/lifecycle/lifecycle-viewmodel-compose/samples/src/main/java/androidx/lifecycle/viewmodel/compose/samples")
                kotlin.srcDir("../support/navigation/navigation-compose/samples/src/main/java/androidx/navigation/compose/samples")
                kotlin.srcDir("../support/paging/paging-compose/samples/src/main/java/androidx/paging/compose/samples/")
            }
        }
    }
}

dependencies {
    implementation(libs.android.core)
    implementation(libs.android.appCompat)
    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.runtime)
    implementation(libs.activity.compose)
    implementation(libs.compose.material.iconsExtended)
    implementation(libs.compose.animation.graphics)
    implementation(libs.compose.runtime.rxjava3)
    implementation(libs.compose.runtime.rxjava2)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.compose.foundation)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.paging.compose)
    implementation(libs.compose.material3)
    ksp(project(":processor"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
