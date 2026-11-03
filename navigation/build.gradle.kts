plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.android); alias(libs.plugins.kotlin.compose) }
android { namespace = "ai.cereon.navigation"; compileSdk = 35; defaultConfig { minSdk = 26 }; buildFeatures { compose = true } }
dependencies { implementation(libs.androidx.navigation.compose); implementation(libs.androidx.compose.material3); implementation(libs.androidx.lifecycle.viewmodel.compose) }
