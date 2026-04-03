import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.apply
import skillima.build.libs

class SkillimaAndroidRoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

            // Required for Room annotation processing
            apply(plugin = "org.jetbrains.kotlin.kapt")

            dependencies {
                add("implementation", libs.findLibrary("room.runtime").get())
                add("implementation", libs.findLibrary("room.ktx").get())
                add("kapt", libs.findLibrary("room.compiler").get())
            }
        }
    }
}
