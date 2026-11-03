pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories { google(); mavenCentral() }
}
rootProject.name = "CereonAI"
include(":app", ":core:common", ":core:database", ":core:datastore", ":core:network", ":core:security", ":domain", ":data", ":navigation", ":feature:chat", ":feature:history", ":feature:models", ":feature:agents", ":feature:skills", ":feature:knowledge", ":feature:voice", ":feature:create", ":feature:markets", ":feature:profile", ":feature:settings")
