package cr.ac.una.presupuesto.viewmodel

import android.net.Uri
import cr.ac.una.presupuesto.data.model.Movimiento

data class MovimientoUIState(
    val listaMovimientos: List<Movimiento> = emptyList(),
    val showDialog: Boolean = false,
    val monto: String = "",
    val tipo: String = "",
    val fecha: String = "",
    val montoError: Boolean = false,
    val tipoError: Boolean = false,
    val fechaError: Boolean = false,
    val movimientoEditandoId: String? = null,
    val imagenUri: Uri? = null,
    val estaCargando: Boolean = false,
    val balanceTotal: Double = 0.0
) {
    val esEdicion: Boolean get() = movimientoEditandoId != null
}

