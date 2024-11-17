import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
}

android {
    val javaVersion: JavaVersion by rootProject.extra

    namespace = "com.tmdb.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":domain"))

    implementation(libs.room.core)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation (libs.hilt.core)
    ksp (libs.hilt.compiler)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.gson.gson)
    implementation(libs.androidx.core.ktx)
}