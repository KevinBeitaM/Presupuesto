package cr.ac.una.presupesto

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import cr.ac.una.presupesto.ui.screens.MovimientoScreen
import cr.ac.una.presupesto.ui.theme.PresupuestoTheme
import cr.ac.una.presupesto.util.crearUriImagen
import cr.ac.una.presupesto.viewmodel.MovimientoViewModel

class MainActivity : ComponentActivity() {

    val permissionLauncher =
        registerForActivityResult(
        contract =
            ActivityResultContracts.RequestMultiplePermissions()
    ){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA))
        enableEdgeToEdge()
        setContent {
            PresupuestoTheme {
                val viewModel : MovimientoViewModel = viewModel()
                MovimientoScreen(viewModel)
            }
        }
    }
}

