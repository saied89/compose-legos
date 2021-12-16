val kspVersion: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":processor-api"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("com.squareup:kotlinpoet-ksp:1.10.2")
}