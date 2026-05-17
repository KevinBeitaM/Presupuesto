package cr.ac.una.presupuesto.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import cr.ac.una.presupuesto.data.repository.MovimientoRepository
import cr.ac.una.presupuesto.data.model.Movimiento
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.net.Uri
import cr.ac.una.presupuesto.util.LocationHelper
import androidx.lifecycle.AndroidViewModel
import android.app.Application

class MovimientoViewModel (application: Application): AndroidViewModel(application) {
    private val repo = MovimientoRepository()

    var uiState by mutableStateOf(MovimientoUIState())
        private set

    init {
        cargarMovimientos()

    }

    fun onMontoChange(valor: String) {
        uiState = uiState.copy(monto = valor, montoError = false)
    }

    fun onTipoChange(valor: String) {
        uiState = uiState.copy(tipo = valor, tipoError = false)
    }

    fun onFechaChange(valor: String) {
        uiState = uiState.copy(fecha = valor, fechaError = false)
    }

    fun onImagenChange(uri: Uri?) {
        uiState = uiState.copy(imagenUri = uri)
    }

    fun abrirDialog() {
        uiState = uiState.copy(showDialog = true, movimientoEditandoId = null)
        limpiarFormulario()
    }

    fun abrirDialogParaEditar(movimiento: Movimiento) {
        uiState = uiState.copy(
            showDialog = true,
            movimientoEditandoId = movimiento.id,
            monto = movimiento.monto.toString(),
            tipo = movimiento.tipo,
            fecha = movimiento.fecha,
            imagenUri = null
        )
    }

    fun cerrarDialog() {
        uiState = uiState.copy(showDialog = false)
        limpiarFormulario()
    }

    private fun cargarMovimientos() {
        uiState = uiState.copy(estaCargando = true)
        repo.obtenerMovimientos { lista ->
            uiState = uiState.copy(
                listaMovimientos = lista,
                balanceTotal = calcularBalance(lista),
                estaCargando = false
            )
        }
    }

    fun eliminar(id: String) {
        repo.eliminarMovimiento(id)
    }

    fun guardarMovimiento() {
        val s = uiState
        val montoValido = s.monto.isNotBlank() && s.monto.matches(Regex("^\\d+(\\.\\d+)?$"))
        val tipoValido = s.tipo.isNotBlank()
        val fechaValida = s.fecha.isNotBlank()

        if (montoValido && tipoValido && fechaValida) {
            LocationHelper.obtenerUbicacion(getApplication()){
                lat,lng->
                val movimiento = Movimiento(
                    id = s.movimientoEditandoId ?: "",
                    monto = s.monto.toDouble(),
                    tipo = s.tipo,
                    fecha = s.fecha,
                    latitud = lat,
                    longitud = lng
                )
                if (s.esEdicion) {
                    repo.actualizarMovimiento(movimiento)
                } else {
                    repo.guardarMovimientoConImagen(movimiento, s.imagenUri)
                }
                cerrarDialog()
            }

        } else {
            uiState = uiState.copy(
                montoError = !montoValido,
                tipoError = !tipoValido,
                fechaError = !fechaValida
            )
        }
    }

    private fun calcularBalance(lista: List<Movimiento>): Double {
        var balance = 0.0
        lista.forEach { mov ->
            if (mov.tipo == "Ingreso") {
                balance += mov.monto
            } else if (mov.tipo == "Egreso") {
                balance -= mov.monto
            }
        }

        return balance
    }

    fun limpiarFormulario() {
        uiState = uiState.copy(
            monto = "",
            tipo = "",
            fecha = "",
            montoError = false,
            tipoError = false,
            fechaError = false,
            movimientoEditandoId = null,

            imagenUri = null //
        )
    }
}

