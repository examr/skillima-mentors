
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import skillima.build.libs

class AndroidKoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                // use the exact alias keys from your version catalog
                add("implementation", libs.findLibrary("koin-compose").get())
                add("implementation", libs.findLibrary("koin-android").get())
                // if you really need the separate compose-viewmodel artifact:
                add("implementation", libs.findLibrary("koin-compose-viewmodel").get())
                add("implementation", libs.findLibrary("koin-compose-navigation").get())
            }
        }
    }
}