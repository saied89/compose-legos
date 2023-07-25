val kspVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.kotlinPoet.ksp)
}