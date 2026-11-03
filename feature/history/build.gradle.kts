plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.android); alias(libs.plugins.kotlin.compose) }
android { namespace = "ai.cereon.feature.history"; compileSdk = 35; defaultConfig { minSdk = 26 }; buildFeatures { compose = true } }
dependencies { implementation(project(":core:database")); implementation(libs.androidx.compose.material3); implementation(libs.androidx.lifecycle.runtime.compose) }
