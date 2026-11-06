
package com.escalarelatorio.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.escalarelatorio.app.database.EscalaEntity
import com.escalarelatorio.app.database.ParticipanteEntity
import java.io.File

object GeradorImagemEscala {

    private const val LARGURA = 1080
    private const val MARGEM = 48
    private const val ESPACO = 18
    private const val ALTURA_MAXIMA = 12000

    private val AZUL = Color.rgb(23, 62, 114)
    private val TEXTO = Color.rgb(37, 53, 74)
    private val CINZA = Color.rgb(104, 120, 140)
    private val FUNDO = Color.rgb(245, 248, 252)
    private val BRANCO = Color.WHITE

    private fun layoutTexto(
        texto: String,
        largura: Int,
        tamanho: Float,
        cor: Int,
        negrito: Boolean = false
    ): StaticLayout {

        val paint = TextPaint(
            Paint.ANTI_ALIAS_FLAG
        ).apply {
            color = cor
            textSize = tamanho
            typeface = if (negrito) {
                Typeface.DEFAULT_BOLD
            } else {
                Typeface.DEFAULT
            }
        }

        return StaticLayout.Builder.obtain(
            texto.ifEmpty { "—" },
            0,
            texto.ifEmpty { "—" }.length,
            paint,
            largura
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setIncludePad(false)
            .build()
    }

    private fun desenharTexto(
        canvas: Canvas,
        texto: String,
        x: Float,
        y: Float,
        largura: Int,
        tamanho: Float,
        cor: Int,
        negrito: Boolean = false
    ): Float {

        val layout = layoutTexto(
            texto,
            largura,
            tamanho,
            cor,
            negrito
        )

        canvas.save()
        canvas.translate(x, y)
        layout.draw(canvas)
        canvas.restore()

        return y + layout.height
    }

    private fun alturaTexto(
        texto: String,
        largura: Int,
        tamanho: Float,
        negrito: Boolean = false
    ): Int {
        return layoutTexto(
            texto,
            largura,
            tamanho,
            TEXTO,
            negrito
        ).height
    }

    fun gerar(
        context: Context,
        escala: EscalaEntity,
        participantes: List<ParticipanteEntity>
    ): File {

        val larguraUtil = LARGURA - 2 * MARGEM
        val larguraCard = larguraUtil
        val larguraInterna = larguraCard - 48

        val registros = participantes.sortedWith(
            compareBy<ParticipanteEntity>(
                { dataOrdenavel(it.data) },
                { it.inicio },
                { it.nome }
            )
        )

        // Calcular altura antes de criar o Bitmap.
        val alturaCabecalho = 270

        val alturaMetadados =
            40 +
                    alturaTexto(escala.local, larguraUtil, 30f) +
                    alturaTexto(escala.responsavel, larguraUtil, 30f) +
                    150

        val alturasCards = registros.map { pessoa ->

            val linhaNome = alturaTexto(
                pessoa.nome,
                larguraInterna,
                37f,
                true
            )

            val linhaFuncao = alturaTexto(
                pessoa.funcao.ifBlank { "Não informada" },
                larguraInterna,
                29f
            )

            val linhaData = alturaTexto(
                "${pessoa.data}  |  ${pessoa.inicio} - ${pessoa.fim}",
                larguraInterna,
                29f
            )

            val linhaObs = if (pessoa.observacoes.isBlank()) {
                0
            } else {
                alturaTexto(
                    "Obs.: ${pessoa.observacoes}",
                    larguraInterna,
                    27f
                ) + 14
            }

            48 + linhaNome + 10 +
                    linhaFuncao + 10 +
                    linhaData + linhaObs + 28
        }

        val alturaObservacoes =
            if (escala.observacoes.isBlank()) {
                0
            } else {
                70 + alturaTexto(
                    escala.observacoes,
                    larguraUtil,
                    29f
                )
            }

        val alturaTotal =
            alturaCabecalho +
                    alturaMetadados +
                    90 +
                    alturasCards.sum() +
                    registros.size * ESPACO +
                    alturaObservacoes +
                    140

        require(alturaTotal <= ALTURA_MAXIMA) {
            "Escala muito longa para uma única imagem. " +
                    "Divida os participantes em escalas menores."
        }

        val bitmap = Bitmap.createBitmap(
            LARGURA,
            alturaTotal.coerceAtLeast(700),
            Bitmap.Config.ARGB_8888
        )

        try {
            val canvas = Canvas(bitmap)

            val paint = Paint(
                Paint.ANTI_ALIAS_FLAG
            )

            canvas.drawColor(FUNDO)

            // Cabeçalho
            paint.color = AZUL
            canvas.drawRect(
                0f,
                0f,
                LARGURA.toFloat(),
                alturaCabecalho.toFloat(),
                paint
            )

            var y = 45f

            y = desenharTexto(
                canvas,
                "ESCALA • ${escala.tipo.uppercase()}",
                MARGEM.toFloat(),
                y,
                larguraUtil,
                30f,
                Color.rgb(215, 232, 251),
                true
            ) + 20f

            y = desenharTexto(
                canvas,
                escala.titulo,
                MARGEM.toFloat(),
                y,
                larguraUtil,
                48f,
                BRANCO,
                true
            ) + 15f

            desenharTexto(
                canvas,
                "${escala.dataInicial} a ${escala.dataFinal}",
                MARGEM.toFloat(),
                y,
                larguraUtil,
                30f,
                BRANCO
            )

            // Informações gerais
            y = alturaCabecalho + 35f

            y = desenharTexto(
                canvas,
                "LOCAL / UNIDADE",
                MARGEM.toFloat(),
                y,
                larguraUtil,
                25f,
                CINZA,
                true
            ) + 8f

            y = desenharTexto(
                canvas,
                escala.local,
                MARGEM.toFloat(),
                y,
                larguraUtil,
                30f,
                TEXTO
            ) + 28f

            y = desenharTexto(
                canvas,
                "RESPONSÁVEL",
                MARGEM.toFloat(),
                y,
                larguraUtil,
                25f,
                CINZA,
                true
            ) + 8f

            y = desenharTexto(
                canvas,
                escala.responsavel,
                MARGEM.toFloat(),
                y,
                larguraUtil,
                30f,
                TEXTO
            ) + 42f

            y = desenharTexto(
                canvas,
                "PARTICIPANTES (${registros.size})",
                MARGEM.toFloat(),
                y,
                larguraUtil,
                34f,
                AZUL,
                true
            ) + 24f

            // Cards de participantes
            registros.forEachIndexed { index, pessoa ->

                val alturaCard = alturasCards[index].toFloat()

                paint.color = BRANCO
                canvas.drawRoundRect(
                    MARGEM.toFloat(),
                    y,
                    (LARGURA - MARGEM).toFloat(),
                    y + alturaCard,
                    18f,
                    18f,
                    paint
                )

                val x = MARGEM + 24f
                var linhaY = y + 24f

                linhaY = desenharTexto(
                    canvas,
                    "${index + 1}. ${pessoa.nome}",
                    x,
                    linhaY,
                    larguraInterna,
                    37f,
                    AZUL,
                    true
                ) + 10f

                linhaY = desenharTexto(
                    canvas,
                    "Função: ${
                        pessoa.funcao.ifBlank { "Não informada" }
                    }",
                    x,
                    linhaY,
                    larguraInterna,
                    29f,
                    TEXTO
                ) + 10f

                linhaY = desenharTexto(
                    canvas,
                    "${pessoa.data}  |  " +
                            "${pessoa.inicio} - ${pessoa.fim}",
                    x,
                    linhaY,
                    larguraInterna,
                    29f,
                    TEXTO
                ) + 14f

                if (pessoa.observacoes.isNotBlank()) {
                    desenharTexto(
                        canvas,
                        "Obs.: ${pessoa.observacoes}",
                        x,
                        linhaY,
                        larguraInterna,
                        27f,
                        CINZA
                    )
                }

                y += alturaCard + ESPACO
            }

            // Observações gerais
            if (escala.observacoes.isNotBlank()) {
                y += 20f

                y = desenharTexto(
                    canvas,
                    "OBSERVAÇÕES GERAIS",
                    MARGEM.toFloat(),
                    y,
                    larguraUtil,
                    30f,
                    AZUL,
                    true
                ) + 15f

                y = desenharTexto(
                    canvas,
                    escala.observacoes,
                    MARGEM.toFloat(),
                    y,
                    larguraUtil,
                    29f,
                    TEXTO
                ) + 20f
            }

            // Rodapé
            desenharTexto(
                canvas,
                "Gerado pelo EscalaApp",
                MARGEM.toFloat(),
                (bitmap.height - 65).toFloat(),
                larguraUtil,
                25f,
                CINZA
            )

            val pasta = File(
                context.cacheDir,
                "escalas"
            ).apply {
                if (!exists()) mkdirs()
            }

            val arquivo = File(
                pasta,
                "escala_${escala.id}_${System.currentTimeMillis()}.png"
            )

            arquivo.outputStream().use { stream ->
                check(
                    bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        stream
                    )
                ) {
                    "Falha ao salvar a imagem."
                }
            }

            return arquivo

        } finally {
            bitmap.recycle()
        }
    }

    private fun dataOrdenavel(data: String): String {
        val partes = data.split("/")

        return if (partes.size == 3) {
            "${partes[2]}${partes[1]}${partes[0]}"
        } else {
            data
        }
    }
}
