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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cr.ac.una.presupesto.viewmodel.MovimientoViewModel

@Composable
fun MovimientoScreen(
    viewModel: MovimientoViewModel
) {
    val movimientoPendienteEliminar = remember { mutableStateOf<String?>(null) }
    val uiState = viewModel.uiState
    var mapaCords by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    if (mapaCords != null) {
        MapaScreen(
            latitud = mapaCords!!.first,
            longitud = mapaCords!!.second,
            onVolver = { mapaCords = null }
        )
        return
    }

    val saldoTotal = viewModel.listaMovimientos.sumOf { mov ->
        val tipo = mov.tipo.trim().lowercase()
        if (tipo == "ingreso" || tipo == "crédito" || tipo == "credito") {
            mov.monto
        } else {
            -mov.monto
        }
    }

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
                item {
                    SaldoTotalCard(saldo = saldoTotal)
                }
                items(viewModel.listaMovimientos) { mov ->
                    MovimientoCard(
                        movimiento = mov,
                        onEdit = { viewModel.abrirDialogParaEditar(mov) },
                        onDelete = { movimientoPendienteEliminar.value = mov.id },
                        onShowLocation = { lat, lng -> mapaCords = Pair(lat, lng) }
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
