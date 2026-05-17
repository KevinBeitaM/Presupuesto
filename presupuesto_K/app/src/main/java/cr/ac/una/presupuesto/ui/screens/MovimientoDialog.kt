package cr.ac.una.presupuesto.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import cr.ac.una.presupuesto.viewmodel.MovimientoViewModel
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.net.Uri
import cr.ac.una.presupuesto.util.crearUriImagen
import androidx.compose.foundation.layout.size
import coil.compose.AsyncImage

@Composable
fun MovimientoDialog(
    viewModel: MovimientoViewModel
) {
    val context = LocalContext.current
    val state = viewModel.uiState // Obtenemos el estado aquí

    var locarUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()){ success ->
        if(success){
            // Usamos la función del ViewModel
            viewModel.onImagenChange(locarUri)
        }
    }



    val opciones = listOf("Ingreso", "Egreso")
    val expandedTipo = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { viewModel.cerrarDialog() },
        title = {
            // Usamos state.esEdicion
            Text(if (state.esEdicion) "Editar Movimiento" else "Nuevo Movimiento")
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {

                OutlinedTextField(
                    value = state.monto, // Cambiado a state
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            // Usamos la función del ViewModel
                            viewModel.onMontoChange(newValue)
                        }
                    },
                    label = { Text("Monto") },
                    placeholder = { Text("0.00") },
                    isError = state.montoError, // Cambiado a state
                    supportingText = {
                        if (state.montoError) {
                            Text("El monto no puede estar vacío y debe ser un número")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = state.tipo, // Cambiado a state
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo") },
                    placeholder = { Text("Seleccionar") },
                    trailingIcon = {
                        IconButton(onClick = { expandedTipo.value = !expandedTipo.value }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                        }
                    },
                    isError = state.tipoError, // Cambiado a state
                    supportingText = {
                        if (state.tipoError) {
                            Text("Debe seleccionar un tipo")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                DropdownMenu(
                    expanded = expandedTipo.value,
                    onDismissRequest = { expandedTipo.value = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    opciones.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                // Usamos la función del ViewModel
                                viewModel.onTipoChange(opcion)
                                expandedTipo.value = false
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = state.fecha, // Cambiado a state
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha") },
                    placeholder = { Text("DD/MM/YYYY") },
                    trailingIcon = {
                        IconButton(onClick = {
                            val c = java.util.Calendar.getInstance()
                            val datePickerDialog = android.app.DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    val sdf = java.text.SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        java.util.Locale.getDefault()
                                    )
                                    val calendar = java.util.Calendar.getInstance()
                                    calendar.set(year, month, dayOfMonth)
                                    // Usamos la función del ViewModel
                                    viewModel.onFechaChange(sdf.format(calendar.time))
                                },
                                c.get(java.util.Calendar.YEAR),
                                c.get(java.util.Calendar.MONTH),
                                c.get(java.util.Calendar.DAY_OF_MONTH)
                            )
                            datePickerDialog.show()
                        }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    },
                    isError = state.fechaError, // Cambiado a state
                    supportingText = {
                        if (state.fechaError) {
                            Text("La fecha no puede estar vacía")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = {
                        val uri = crearUriImagen(context)
                        locarUri = uri
                        cameraLauncher.launch(uri)
                    }
                ) {
                    Text("Tomar foto")
                }

                // Usamos state
                state.imagenUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Foto",
                        modifier = Modifier.size(128.dp).padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.guardarMovimiento()
                }) {
                // Usamos state
                Text(if (state.esEdicion) "Actualizar" else "Guardar")
            }
        },
        dismissButton = {
            Button(onClick = { viewModel.cerrarDialog() }) {
                Text("Cancelar")
            }
        }
    )
}