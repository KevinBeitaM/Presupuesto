package cr.ac.una.presupesto.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun crearUriImagen(
    context: Context
): Uri {
    
    val archivo = File.createTempFile(
        "Movimiento_",
        ".jpg",
        context.externalCacheDir
    )
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        archivo
    )
    
}
