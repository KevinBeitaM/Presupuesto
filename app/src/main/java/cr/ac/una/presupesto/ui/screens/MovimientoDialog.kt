package cr.ac.una.presupesto.ui.screens

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cr.ac.una.presupesto.util.crearUriImagen
import cr.ac.una.presupesto.viewmodel.MovimientoViewModel

@Composable
fun MovimientoDialog(
    viewModel: MovimientoViewModel
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState
    var localUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { success ->
        if (success) {
            viewModel.actualizarImagen(localUri)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract =
            ActivityResultContracts.RequestPermission()
    ){ granted ->
        if (granted){
            var uri = crearUriImagen(context)
            localUri = uri
            cameraLauncher.launch(uri)
        }
    }

    val opciones = listOf("Ingreso", "Egreso")
    val expandedTipo = remember { mutableStateOf(false) }

    val openDatePicker = {
        val c = java.util.Calendar.getInstance()
        val datePickerDialog = android.app.DatePickerDialog(
            android.view.ContextThemeWrapper(
                context,
                android.R.style.Theme_Material_Light_Dialog
            ),
            { _, year, month, dayOfMonth ->
                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val calendar = java.util.Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                viewModel.actualizarFecha(sdf.format(calendar.time))
            },
            c.get(java.util.Calendar.YEAR),
            c.get(java.util.Calendar.MONTH),
            c.get(java.util.Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    AlertDialog(
        onDismissRequest = { viewModel.cerrarDialog() },
        title = {
            Text(if (viewModel.esEdicion) "Editar Movimiento" else "Nuevo Movimiento")
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = uiState.monto,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            viewModel.actualizarMonto(newValue)
                        }
                    },
                    label = { Text("Monto") },
                    placeholder = { Text("0.00") },
                    isError = uiState.montoError,
                    supportingText = {
                        if (uiState.montoError) {
                            Text("El monto no puede estar vacio y debe ser un numero")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = uiState.tipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo") },
                    placeholder = { Text("Seleccionar") },
                    trailingIcon = {
                        IconButton(onClick = { expandedTipo.value = !expandedTipo.value }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                        }
                    },
                    isError = uiState.tipoError,
                    supportingText = {
                        if (uiState.tipoError) {
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
                                viewModel.actualizarTipo(opcion)
                                expandedTipo.value = false
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = uiState.fecha,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha") },
                    placeholder = { Text("DD/MM/YYYY") },
                    trailingIcon = {
                        IconButton(onClick = { openDatePicker() }) {
                            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
                        }
                    },
                    isError = uiState.fechaError,
                    supportingText = {
                        if (uiState.fechaError) {
                            Text("La fecha no puede estar vacía")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openDatePicker() }
                )
                Button(
                    onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                ) {
                    Text("Tomar foto")
                }
                uiState.imagenUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Foto",
                        modifier = Modifier.size(120.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.guardarMovimiento()
                }) {
                Text(if (viewModel.esEdicion) "Actualizar" else "Guardar")

            }
        },
        dismissButton = {
            Button(onClick = { viewModel.cerrarDialog() }) {
                Text("Cancelar")
            }
        }
    )
}
