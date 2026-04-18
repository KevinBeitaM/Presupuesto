package cr.ac.una.presupesto.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import cr.ac.una.presupesto.viewmodel.MovimientoViewModel

@Composable
@Suppress("UNUSED", "UNUSED_PARAMETER")
fun MovimientoDialog(
    viewModel: MovimientoViewModel
) {
    AlertDialog(
        onDismissRequest = { viewModel.cerrarDialog() },
        title = {
            Text("Nuevo Movimiento")
        },
        text = {
            Column {
                OutlinedTextField(
                    value= viewModel.monto,
                    onValueChange = {viewModel.monto= it },
                    label = { Text("Ingrese un Monto") },
                    isError = viewModel.montoError,
                    supportingText = {
                        if (viewModel.montoError) {
                            Text("El monto no puede estar vacío")
                        }

                    }
                )
                OutlinedTextField(
                    value = viewModel.tipo,
                    onValueChange = {viewModel.tipo= it },
                    label = { Text("Ingrese un Tipo") }
                )
                OutlinedTextField(
                    value = viewModel.fecha,
                    onValueChange = {viewModel.fecha= it },
                    label = { Text("Ingrese una Fecha") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.guardarMovimiento()
                    viewModel.cerrarDialog() }) {
                Text("Guardar")

            }
        },
        dismissButton = {
            Button(onClick = { viewModel.cerrarDialog() }) {
                Text("Cancelar")
            }
        }
    )
}
