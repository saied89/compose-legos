val kspVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:javapoet:1.13.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
}