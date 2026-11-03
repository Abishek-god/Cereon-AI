package ai.cereon.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ai.cereon.app.ui.CereonRoot
import ai.cereon.app.ui.theme.CereonTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CereonTheme { CereonRoot() } }
    }
}
