package cr.ac.una.presupuesto.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Movimiento(
    var id: String = "",
    var monto: Double = 0.0,
    var tipo: String = "",
    var fecha: String = "",
    var imagenUrl: String = "",
    var longitud: Double? = null,
    var latitud: Double? = null
)
