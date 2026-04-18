package cr.ac.una.presupesto.viewmodel

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
    var showDialog by mutableStateOf(false)

    var monto by mutableStateOf("")
    var tipo by mutableStateOf("")
    var fecha by mutableStateOf("")
    var montoError by mutableStateOf(false)
    var tipoError by mutableStateOf(false)
    var fechaError by mutableStateOf(false)
    var movimientoEditandoId by mutableStateOf<String?>(null)
    val esEdicion: Boolean
        get() = movimientoEditandoId != null

    fun abrirDialog(){
        movimientoEditandoId = null
        showDialog = true
    }

    fun abrirDialogParaEditar(movimiento: Movimiento) {
        movimientoEditandoId = movimiento.id
        monto = movimiento.monto.toString()
        tipo = movimiento.tipo
        fecha = movimiento.fecha
        montoError = false
        tipoError = false
        fechaError = false
        showDialog = true
    }

    fun cerrarDialog(){
        showDialog = false
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
    fun guardarMovimiento(){
        // Validar campos
        var esValido = true

        if (monto.isBlank()) {
            montoError = true
            esValido = false
        } else {
            // Validar que solo contenga números y puntos
            if (!monto.matches(Regex("^\\d+(\\.\\d+)?$"))) {
                montoError = true
                esValido = false
            } else {
                montoError = false
            }
        }

        if (tipo.isBlank()) {
            tipoError = true
            esValido = false
        } else {
            tipoError = false
        }

        if (fecha.isBlank()) {
            fechaError = true
            esValido = false
        } else {
            fechaError = false
        }

        // Solo guardar si es válido
        if (esValido) {
            val movimiento = Movimiento(
                id = movimientoEditandoId ?: "",
                monto = monto.toDouble(),
                tipo = tipo,
                fecha = fecha
            )

            if (esEdicion) {
                repo.actualizarMovimiento(movimiento)
            } else {
                repo.guardarMovimiento(movimiento)
            }

            cerrarDialog()
            // Recargar movimientos después de guardar para asegurar que aparezcan
            cargarMovimientos()
        }
    }
    fun limpiarFormulario(){
        monto = ""
        tipo = ""
        fecha = ""
        montoError = false
        tipoError = false
        fechaError = false
        movimientoEditandoId = null
    }

}