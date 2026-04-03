plugins {
    alias(libs.plugins.skillima.android.library.compose)
    alias(libs.plugins.skillima.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.skillima.koin)
}

android {
    namespace = "skillima.screens.guild"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.svg)  // ← add this

    implementation(libs.androidx.compose.material.icons.core)
    implementation(projects.data.guild)
    implementation(projects.core.models)
    implementation(projects.core.supabase)
    implementation(projects.core.ui)
    implementation(projects.data.local)
    implementation(projects.core.utils)
    implementation(projects.core.navigation)
    //nav 3

    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}