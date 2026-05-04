package cr.ac.una.presupesto.viewmodel

import android.net.Uri

data class MovimientosUiState(
    val monto: String = "",
    val tipo: String = "",
    val fecha: String = "",
    val montoError: Boolean = false,
    val tipoError: Boolean = false,
    val fechaError: Boolean = false,
    val movimientoEditandoId: String? = null,
    val showDialog: Boolean = false,
    val imagenUri: Uri? = null
)

