pluginManagement {
    repositories {
        google()
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

rootProject.name = "manolito-voice"

include(":app")
include(":core:model")
include(":core:designsystem")
include(":core:network")
include(":core:audio")
include(":feature:home")
include(":feature:history")
include(":feature:memory")
include(":feature:settings")
