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
    var montoError by  mutableStateOf(true)

    fun abrirDialog(){
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
        val movimiento = Movimiento(
            monto= monto.toDouble(),
            tipo= tipo,
            fecha= fecha)
        repo.guardarMovimiento(movimiento)
    }
    fun limpiarFormulario(){
        monto = ""
        tipo = ""
        fecha = ""

    }

}