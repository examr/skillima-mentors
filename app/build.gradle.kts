import java.util.Properties

plugins {
    alias(libs.plugins.skillima.android.compose)
    alias(libs.plugins.skillima.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.skillima.koin)
}


val localProperties = Properties()
localProperties.load(rootProject.file("local.properties").inputStream())
android {

    namespace = "skillima.mentors"
    compileSdk { version = release(version = 36) }

    buildFeatures { buildConfig = true }

    defaultConfig {
        applicationId = "skillima.mentors"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "SUPABASE_URL", "\"${localProperties["SUPABASE_URL"]}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${localProperties["SUPABASE_ANON_KEY"]}\"")
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
    buildFeatures {
        compose = true
    }
}

dependencies {


    implementation(projects.screens.auth)
    implementation(projects.core.supabase)
    implementation(projects.data.auth)
    implementation(projects.screens.onboarding)
    implementation(projects.core.supabase)
    implementation(projects.core.datastore)
    implementation(projects.core.utils)
    implementation(projects.core.navigation)
    implementation(projects.data.local)
    implementation(projects.screens.guild)
    implementation(projects.data.guild)
    implementation(projects.screens.home)


    implementation("androidx.core:core-splashscreen:1.0.1")


    //nav 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.kotlinx.serialization.core)


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(projects.core.ui)
}