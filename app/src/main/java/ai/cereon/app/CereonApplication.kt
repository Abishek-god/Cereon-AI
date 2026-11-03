package ai.cereon.app

import android.app.Application
import ai.cereon.core.database.CereonDatabase

class CereonApplication : Application() {
    val database by lazy { CereonDatabase.create(this) }
}
