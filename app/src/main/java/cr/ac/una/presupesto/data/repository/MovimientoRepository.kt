package cr.ac.una.presupesto.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import cr.ac.una.presupesto.data.model.Movimiento

class MovimientoRepository {

    private val db =
        FirebaseDatabase.getInstance()
            .getReference("movimientos")

    fun guardarMovimiento(movimiento: Movimiento){
        val id = db.push().key!!
        movimiento.id = id
        db.child(id).setValue(movimiento)
    }

    fun actualizarMovimiento(movimiento: Movimiento){
        db.child(movimiento.id).setValue(movimiento)
    }

    fun eliminarMovimiento(id: String){
        db.child(id).removeValue()
    }

    fun obtenerMovimientos(
        onResult: (List<Movimiento>) -> Unit)
    {
        db.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot){
                    val lista = mutableListOf<Movimiento>()
                    for (dato in p0.children){
                        val mov =
                            dato.getValue(Movimiento::class.java)
                        mov?.let{ lista.add(it) }
                    }
                    onResult(lista)

                }

                override fun onCancelled(p0: DatabaseError){
                    onResult(emptyList())
                }
            }
        )

    }
}