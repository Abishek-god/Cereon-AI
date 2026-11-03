plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.android); alias(libs.plugins.kotlin.compose) }
android { namespace = "ai.cereon.feature.models"; compileSdk = 35; defaultConfig { minSdk = 26 }; buildFeatures { compose = true } }
dependencies { implementation(project(":core:network")); implementation(project(":core:security")); implementation(project(":data")); implementation(libs.androidx.compose.material3); implementation(libs.androidx.compose.ui); implementation(libs.kotlinx.coroutines.android) }
