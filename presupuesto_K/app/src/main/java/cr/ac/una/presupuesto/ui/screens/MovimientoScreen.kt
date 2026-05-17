package cr.ac.una.presupuesto.ui.screens

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
import cr.ac.una.presupuesto.viewmodel.MovimientoViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun MovimientoScreen(viewModel: MovimientoViewModel) {
    val state = viewModel.uiState
    val context = LocalContext.current
    val movimientoPendienteEliminar = remember { mutableStateOf<String?>(null) }
    var mapaCords by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    if (mapaCords != null) {
        MapaScreen(
            latitud = mapaCords!!.first,
            longitud = mapaCords!!.second,
            onVolver = { mapaCords = null }
        )
        return
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Tarjeta de Balance
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (state.balanceTotal >= 0) Color(0xFF2E7D32) else Color(0xFFB71C1C)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Balance total",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "₡${state.balanceTotal}",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.showDialog) {
                    MovimientoDialog(viewModel = viewModel)
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.listaMovimientos) { mov ->
                        MovimientoCard(
                            movimiento = mov,
                            onEdit = { viewModel.abrirDialogParaEditar(mov) },
                            onDelete = { movimientoPendienteEliminar.value = mov.id },
                            onShowLocation = { lat,lng->
                                mapaCords = Pair(lat,lng)
                            }

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
                            TextButton(onClick = { movimientoPendienteEliminar.value = null }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}

