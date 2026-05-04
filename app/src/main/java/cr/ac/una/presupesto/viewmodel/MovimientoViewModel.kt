package cr.ac.una.presupesto.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cr.ac.una.presupesto.data.model.Movimiento
import cr.ac.una.presupesto.data.repository.MovimientoRepository

class MovimientoViewModel: ViewModel() {
    private val repo= MovimientoRepository()
    var listaMovimientos =
        mutableStateListOf<Movimiento>()

    var uiState by mutableStateOf(MovimientosUiState())

    val esEdicion: Boolean
        get() = uiState.movimientoEditandoId != null

    fun abrirDialog(){
        uiState = uiState.copy(
            movimientoEditandoId = null,
            showDialog = true,
            monto = "",
            tipo = "",
            fecha = "",
            montoError = false,
            tipoError = false,
            fechaError = false,
            imagenUri = null
        )
    }

    fun abrirDialogParaEditar(movimiento: Movimiento) {
        uiState = uiState.copy(
            movimientoEditandoId = movimiento.id,
            monto = movimiento.monto.toString(),
            tipo = movimiento.tipo,
            fecha = movimiento.fecha,
            montoError = false,
            tipoError = false,
            fechaError = false,
            showDialog = true
        )
    }

    fun cerrarDialog(){
        uiState = uiState.copy(showDialog = false)
        limpiarFormulario()
    }

    private fun cargarMovimientos() {
        repo.obtenerMovimientos { lista ->
            listaMovimientos.apply {
                clear()
                addAll(lista)
            }

        }
    }
    init {
        cargarMovimientos()

    }
    fun eliminar(id: String){
        repo.eliminarMovimiento(id)
    }

    fun actualizarMonto(nuevo: String) {
        uiState = uiState.copy(monto = nuevo)
    }

    fun actualizarTipo(nuevo: String) {
        uiState = uiState.copy(tipo = nuevo)
    }

    fun actualizarFecha(nuevo: String) {
        uiState = uiState.copy(fecha = nuevo)
    }

    fun actualizarImagen(uri: Uri?) {
        uiState = uiState.copy(imagenUri = uri)
    }

    fun guardarMovimiento(){
        val estado = uiState
        var montoError = false
        var tipoError = false
        var fechaError = false

        if (estado.monto.isBlank()) {
            montoError = true
        } else {
            if (!estado.monto.matches(Regex("^\\d+(\\.\\d+)?$"))) {
                montoError = true
            }
        }

        if (estado.tipo.isBlank()) {
            tipoError = true
        }

        if (estado.fecha.isBlank()) {
            fechaError = true
        }

        uiState = estado.copy(
            montoError = montoError,
            tipoError = tipoError,
            fechaError = fechaError
        )

        val esValido = !montoError && !tipoError && !fechaError
        if (esValido) {
            val movimiento = Movimiento(
                id = estado.movimientoEditandoId ?: "",
                monto = estado.monto.toDouble(),
                tipo = estado.tipo,
                fecha = estado.fecha
            )

            if (esEdicion) {
                repo.actualizarMovimiento(movimiento)
            } else {
                repo.guardarMovimientoConImagen(movimiento, estado.imagenUri)
            }

            cerrarDialog()
            cargarMovimientos()
        }
    }

    fun limpiarFormulario(){
        uiState = uiState.copy(
            monto = "",
            tipo = "",
            fecha = "",
            montoError = false,
            tipoError = false,
            fechaError = false,
            movimientoEditandoId = null,
            imagenUri = null
        )
    }

}
