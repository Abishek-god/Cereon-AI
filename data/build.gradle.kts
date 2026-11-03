plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.android) }
android { namespace = "ai.cereon.data"; compileSdk = 35; defaultConfig { minSdk = 26 } }
dependencies { implementation(project(":domain")); implementation(project(":core:database")); implementation(project(":core:network")); implementation(project(":core:security")); implementation(libs.okhttp); implementation(libs.kotlinx.serialization.json); implementation(libs.kotlinx.coroutines.android) }
