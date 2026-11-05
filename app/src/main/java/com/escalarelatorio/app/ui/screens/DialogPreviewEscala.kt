
package com.escalarelatorio.app.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DialogPreviewEscala(
    arquivo: File,
    aoFechar: () -> Unit,
    aoCompartilhar: () -> Unit
) {
    var imagem by remember(arquivo) {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    var erro by remember(arquivo) {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(arquivo) {
        imagem = null
        erro = null

        try {
            imagem = withContext(Dispatchers.IO) {
                val opcoes = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }

                BitmapFactory.decodeFile(
                    arquivo.absolutePath,
                    opcoes
                )

                var amostragem = 1

                while (
                    opcoes.outWidth / amostragem > 1080 ||
                    opcoes.outHeight / amostragem > 2048
                ) {
                    amostragem *= 2
                }

                val leitura = BitmapFactory.Options().apply {
                    inSampleSize = amostragem
                }

                BitmapFactory.decodeFile(
                    arquivo.absolutePath,
                    leitura
                ) ?: error("Imagem não encontrada")
            }
        } catch (e: Exception) {
            erro = e.localizedMessage
                ?: "Não foi possível abrir a imagem."
        }
    }

    DisposableEffect(imagem) {
        val bitmapAtual = imagem
        onDispose {
            bitmapAtual?.recycle()
        }
    }

    AlertDialog(
        onDismissRequest = aoFechar,
        title = {
            Text("Pré-visualizar escala")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Confira a imagem antes de compartilhar.",
                    style = MaterialTheme.typography.bodyMedium
                )

                if (imagem == null && erro == null) {
                    CircularProgressIndicator()
                }

                imagem?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Imagem da escala",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                erro?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = imagem != null,
                onClick = aoCompartilhar
            ) {
                Text("Compartilhar")
            }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) {
                Text("Cancelar")
            }
        }
    )
}
