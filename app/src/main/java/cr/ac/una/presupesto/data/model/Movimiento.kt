package cr.ac.una.presupesto.data.model

import java.io.Serializable


data class Movimiento(
    var id: String = "",
    var monto: Double = 0.0,
    var tipo: String = "",
    var fecha: String = "",
    var imagenUrl: String? = null,
    var longitud: Double? = null,
    var latitud: Double? = null

) : Serializable
