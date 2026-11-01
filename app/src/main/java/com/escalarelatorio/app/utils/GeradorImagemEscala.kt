
package com.escalarelatorio.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.escalarelatorio.app.database.EscalaEntity
import com.escalarelatorio.app.database.ParticipanteEntity
import java.io.File

object GeradorImagemEscala {

    fun gerar(
        context: Context,
        escala: EscalaEntity,
        participantes: List<ParticipanteEntity>
    ): File {

        val largura = 1080
        val margem = 54f
        val larguraTexto = largura - margem * 2

        val azul = Color.rgb(29, 79, 145)
        val cinza = Color.rgb(85, 100, 120)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = 34f
            color = Color.BLACK
        }

        data class Linha(
            val texto: String,
            val tamanho: Float,
            val cor: Int,
            val negrito: Boolean,
            val altura: Float
        )

        val linhas = mutableListOf<Linha>()

        fun adicionar(
            texto: String,
            tamanho: Float = 34f,
            cor: Int = Color.BLACK,
            negrito: Boolean = false,
            espacamento: Float = 12f
        ) {
            paint.textSize = tamanho
            paint.typeface = if (negrito) {
                Typeface.DEFAULT_BOLD
            } else {
                Typeface.DEFAULT
            }

            val alturaLinha = tamanho * 1.35f

            for (paragrafo in texto.split("\n")) {
                var restante = paragrafo.trim()

                if (restante.isEmpty()) {
                    linhas.add(
                        Linha("", tamanho, cor, negrito, alturaLinha)
                    )
                    continue
                }

                while (restante.isNotEmpty()) {
                    var quantidade = paint.breakText(
                        restante,
                        true,
                        larguraTexto,
                        null
                    )

                    if (quantidade <= 0) quantidade = 1

                    if (quantidade < restante.length) {
                        val espaco = restante
                            .substring(0, quantidade)
                            .lastIndexOf(' ')

                        if (espaco > 0) quantidade = espaco
                    }

                    val trecho = restante
                        .substring(0, quantidade)
                        .trim()

                    linhas.add(
                        Linha(
                            trecho,
                            tamanho,
                            cor,
                            negrito,
                            alturaLinha
                        )
                    )

                    restante = restante
                        .substring(quantidade)
                        .trimStart()
                }
            }

            linhas.add(
                Linha("", tamanho, cor, false, espacamento)
            )
        }

        adicionar(
            escala.titulo.uppercase(),
            52f,
            azul,
            true,
            24f
        )

        adicionar("Tipo: ${escala.tipo}", 30f, cinza)
        adicionar(
            "Período: ${escala.dataInicial} até ${escala.dataFinal}",
            30f
        )
        adicionar("Local: ${escala.local}", 30f)
        adicionar("Responsável: ${escala.responsavel}", 30f)

        adicionar(
            "PARTICIPANTES (${participantes.size})",
            39f,
            azul,
            true,
            18f
        )

        participantes.forEachIndexed { indice, pessoa ->
            adicionar(
                "${indice + 1}. ${pessoa.nome}",
                36f,
                azul,
                true,
                8f
            )
            adicionar("Função: ${pessoa.funcao}", 30f)
            adicionar("Data: ${pessoa.data}", 30f)
            adicionar(
                "Horário: ${pessoa.inicio} - ${pessoa.fim}",
                30f
            )

            if (pessoa.observacoes.isNotBlank()) {
                adicionar(
                    "Observações: ${pessoa.observacoes}",
                    28f,
                    cinza
                )
            }
         }

        if (escala.observacoes.isNotBlank()) {
            adicionar("OBSERVAÇÕES GERAIS", 36f, azul, true)
            adicionar(escala.observacoes, 30f)
        }

        adicionar(
            "Gerado pelo EscalaApp",
            25f,
            cinza
        )

        val altura = (
                linhas.sumOf { it.altura.toDouble() }
                        + margem * 2
                        + 30
                ).toInt().coerceAtLeast(600)

        val bitmap = Bitmap.createBitmap(
            largura,
            altura,
            Bitmap.Config.ARGB_8888
        )

        try {
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.WHITE)

            var y = margem

            for (linha in linhas) {
                paint.textSize = linha.tamanho
                paint.color = linha.cor
                paint.typeface = if (linha.negrito) {
                    Typeface.DEFAULT_BOLD
                } else {
                    Typeface.DEFAULT
                }

                if (linha.texto.isNotEmpty()) {
                    y -= paint.fontMetrics.ascent
                    canvas.drawText(
                        linha.texto,
                        margem,
                        y,
                        paint
                    )
                    y += paint.fontMetrics.descent
                } else {
                    y += linha.altura
                }
            }

            val pasta = File(context.cacheDir, "escalas")
            if (!pasta.exists()) pasta.mkdirs()

            val arquivo = File(
                pasta,
                "escala_${escala.id}_${System.currentTimeMillis()}.png"
            )

            arquivo.outputStream().use {
                check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)) {
                    "Falha ao gerar PNG"
                }
            }

            return arquivo

        } finally {
            bitmap.recycle()
        }
    }
}
