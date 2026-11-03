plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.android) }
android { namespace = "ai.cereon.core.security"; compileSdk = 35; defaultConfig { minSdk = 26 } }
dependencies { implementation(libs.androidx.security.crypto) }
