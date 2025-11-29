import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "skillima.build"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "skillima.android"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "skillima.android.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("firebase") {
            id = "skillima.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }

        register("androidFeature") {
            id = "skillima.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "skillima.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "skillima.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}