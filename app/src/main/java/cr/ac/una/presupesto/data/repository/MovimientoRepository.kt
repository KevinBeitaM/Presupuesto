package cr.ac.una.presupesto.data.repository

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import cr.ac.una.presupesto.data.model.Movimiento
import java.util.UUID

class MovimientoRepository {

    private val db =
        FirebaseDatabase.getInstance()
            .getReference("movimientos")

    fun guardarMovimiento(movimiento: Movimiento) {
        val id = db.push().key!!
        movimiento.id = id
        db.child(id).setValue(movimiento)
    }

    fun guardarMovimientoConImagen(
        movimiento: Movimiento,
        imagenUri: Uri?
    ) {
        if (imagenUri != null) {
            var storage =
                FirebaseStorage.getInstance()
            val nombreArchivo=
                "movimientos/${UUID.randomUUID()}.jpg"
            val referencia =
                storage.getReference(nombreArchivo)
            referencia.putFile(imagenUri).continueWithTask {
                referencia.downloadUrl
            }
                .addOnSuccessListener { uri ->
                    movimiento.imagenUrl = uri.toString()
                    guardarMovimiento(movimiento)
                }

            }


        }

        fun actualizarMovimiento(movimiento: Movimiento) {
            db.child(movimiento.id).setValue(movimiento)
        }

        fun eliminarMovimiento(id: String) {
            db.child(id).removeValue()
        }

        fun obtenerMovimientos(
            onResult: (List<Movimiento>) -> Unit
        ) {
            db.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val lista = mutableListOf<Movimiento>()
                        for (dato in p0.children) {
                            val mov =
                                dato.getValue(Movimiento::class.java)
                            mov?.let { lista.add(it) }
                        }
                        onResult(lista)

                    }

                    override fun onCancelled(p0: DatabaseError) {
                        onResult(emptyList())
                    }
                }
            )

        }
    }
