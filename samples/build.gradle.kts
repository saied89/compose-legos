plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
    id ("kotlin-parcelize")
    id("com.google.devtools.ksp")
}
val compose_version: String by project
val material3_version: String by project
android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "home.saied.samples"
    kotlin {
        sourceSets {
            main {
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
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation( "androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation("androidx.compose.runtime:runtime:$compose_version")
    implementation("androidx.activity:activity-compose:1.7.0-beta01")
    implementation("androidx.compose.material:material-icons-extended:$compose_version")
    implementation("androidx.compose.animation:animation-graphics:$compose_version")
    implementation("androidx.compose.runtime:runtime-rxjava3:$compose_version")
    implementation("androidx.compose.runtime:runtime-rxjava2:$compose_version")
    implementation("androidx.compose.runtime:runtime-livedata:$compose_version")
    implementation("androidx.compose.foundation:foundation:1.4.0-beta01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("androidx.paging:paging-compose:1.0.0-alpha18")
    implementation("androidx.compose.material3:material3:$material3_version")
    ksp(project(":processor"))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
