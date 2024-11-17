buildscript {
    val javaVersion by extra(JavaVersion.VERSION_17)
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.kotlinx.plugin.serialization) apply false
    alias(libs.plugins.ksp) apply false
}