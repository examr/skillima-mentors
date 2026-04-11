pluginManagement {
    repositories {
        includeBuild("build-logic")
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Skillima-Mentors"
include(":app")
include(":core:ui")
include(":core:utils")
includeBuild("build-logic")

include(":screens:auth")
include(":screens:onboarding")
include(":core:models")
include(":core:supabase")
include(":data:auth")
include(":data:local")
include(":core:datastore")
include(":core:datastore")
include(":core:navigation")
include(":data:user")
include(":data:guild")
include(":screens:guild")
include(":screens:home")
