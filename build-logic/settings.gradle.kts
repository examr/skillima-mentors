dependencyResolutionManagement {
    // 1. Setup repositories to download dependencies
    repositories {
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
    }

    // 2. Create version catalog named `libs` from
    // the `libs.versions.toml` file already present in
    // the gradle folder.
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}



// 3. Set project name
rootProject.name = "build-logic"

// 4. Include the `convention` module that we will create next
include(":convention")