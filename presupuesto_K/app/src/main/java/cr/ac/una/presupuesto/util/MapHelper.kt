package cr.ac.una.presupuesto.util

import android.content.Intent
import android.content.Context
import android.net.Uri

object MapHelper {
    fun abrirMapa(
        lat: Double,
        long: Double,
        context: Context
    ) {

        val uri = Uri.parse("geo:$lat,$long?q=$lat,$long")


        val intent = Intent(Intent.ACTION_VIEW, uri)

        intent.setPackage("com.google.android.apps.maps")


        context.startActivity(intent)
    }
}