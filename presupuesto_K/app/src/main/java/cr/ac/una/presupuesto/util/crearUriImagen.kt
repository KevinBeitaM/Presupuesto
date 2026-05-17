package cr.ac.una.presupuesto.util

import android.net.Uri
import android.content.Context
import androidx.core.content.FileProvider
import java.io.File

fun crearUriImagen (
    context: Context
): Uri{
    var archivo = File.createTempFile(
        "movimiento",
         ".jpg",
         context.externalCacheDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        archivo
    )
}