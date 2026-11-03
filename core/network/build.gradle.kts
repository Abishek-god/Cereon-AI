plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.android) }
android { namespace = "ai.cereon.core.network"; compileSdk = 35; defaultConfig { minSdk = 26 } }
dependencies { implementation(libs.okhttp); implementation(libs.okhttp.sse); implementation(libs.kotlinx.serialization.json); implementation(libs.kotlinx.coroutines.android) }
