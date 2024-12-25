plugins {
    kotlin("jvm")
}

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.kotlinPoet.ksp)
}