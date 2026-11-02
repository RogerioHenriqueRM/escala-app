
package com.escalarelatorio.app.utils

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File

object CompartilharEscala {

    fun compartilhar(
        context: Context,
        arquivo: File,
        titulo: String
    ) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            arquivo
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"

            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, "Escala: $titulo")

            clipData = ClipData.newUri(
                context.contentResolver,
                "Imagem da escala",
                uri
            )

            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(
            intent,
            "Compartilhar escala"
        )

        context.startActivity(chooser)
    }
}
