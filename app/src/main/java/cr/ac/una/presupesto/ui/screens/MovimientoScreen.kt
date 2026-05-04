package cr.ac.una.presupesto.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cr.ac.una.presupesto.viewmodel.MovimientoViewModel

@Composable
fun MovimientoScreen(
    viewModel: MovimientoViewModel
) {
    val movimientoPendienteEliminar = remember { mutableStateOf<String?>(null) }
    val uiState = viewModel.uiState

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.abrirDialog() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar"
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.showDialog) {
                MovimientoDialog(viewModel = viewModel)
            }

            LazyColumn {
                items(viewModel.listaMovimientos) { mov ->
                    MovimientoCard(
                        movimiento = mov,
                        onEdit = { viewModel.abrirDialogParaEditar(mov) },
                        onDelete = { movimientoPendienteEliminar.value = mov.id }
                    )
                }
            }

            if (movimientoPendienteEliminar.value != null) {
                AlertDialog(
                    onDismissRequest = { movimientoPendienteEliminar.value = null },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Seguro que deseas eliminar este movimiento?") },
                    confirmButton = {
                        Button(onClick = {
                            movimientoPendienteEliminar.value?.let { id ->
                                viewModel.eliminar(id)
                            }
                            movimientoPendienteEliminar.value = null
                        }) {
                            Text("Eliminar")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { movimientoPendienteEliminar.value = null }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
