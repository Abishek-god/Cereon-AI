plugins { alias(libs.plugins.android.library); alias(libs.plugins.kotlin.android); alias(libs.plugins.ksp) }
android { namespace = "ai.cereon.core.database"; compileSdk = 35; defaultConfig { minSdk = 26 } }
dependencies { implementation(libs.androidx.core.ktx); implementation(libs.androidx.room.runtime); implementation(libs.androidx.room.ktx); ksp(libs.androidx.room.compiler); implementation(libs.kotlinx.coroutines.android) }
