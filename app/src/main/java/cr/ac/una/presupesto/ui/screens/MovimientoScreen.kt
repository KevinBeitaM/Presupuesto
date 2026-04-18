package cr.ac.una.presupesto.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cr.ac.una.presupesto.viewmodel.MovimientoViewModel

@Composable
fun MovimientoScreen(
    viewModel: MovimientoViewModel
) {
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
            if (viewModel.showDialog) {
                MovimientoDialog(viewModel = viewModel)
            }

            LazyColumn {
                items(viewModel.listaMovimientos) { mov ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(text = "Monto: ${mov.monto}")
                                Text(text = "Tipo: ${mov.tipo}")
                                Text(text = "Fecha: ${mov.fecha}")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(onClick = { viewModel.eliminar(mov.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar"
                                )


                            }
                        }
                    }
                }
            }
        }
    }
}
