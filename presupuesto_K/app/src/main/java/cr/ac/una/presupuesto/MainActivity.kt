package cr.ac.una.presupuesto

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import cr.ac.una.presupuesto.ui.screens.MovimientoScreen
import cr.ac.una.presupuesto.ui.theme.PresupuestoTheme

import cr.ac.una.presupuesto.viewmodel.MovimientoViewModel


class MainActivity : ComponentActivity() {
    val permissionLauncher = registerForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            )
        )
        enableEdgeToEdge()
        setContent {
            PresupuestoTheme {
                val viewModel : MovimientoViewModel = viewModel()
                MovimientoScreen(viewModel)
                }
            }
        }
    }


