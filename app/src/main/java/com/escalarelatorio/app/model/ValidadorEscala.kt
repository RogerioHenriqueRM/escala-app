package com.escalarelatorio.app.model

import java.text.SimpleDateFormat
import java.util.Locale

object ValidadorEscala {

    private fun converterData(valor: String): Long? {
        val formato = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale("pt", "BR")
        ).apply {
            isLenient = false
        }

        return try {
            formato.parse(valor)?.time
        } catch (e: Exception) {
            null
        }
    }

    private fun horarioValido(valor: String): Boolean {
        val partes = valor.split(":")

        if (partes.size != 2) return false

        val hora = partes[0].toIntOrNull() ?: return false
        val minuto = partes[1].toIntOrNull() ?: return false

        return hora in 0..23 && minuto in 0..59
    }

    fun validar(
        titulo: String,
        dataInicial: String,
        dataFinal: String,
        local: String,
        responsavel: String,
        participantes: List<ParticipanteEscala>
    ): String? {

        if (titulo.isBlank()) {
            return "Informe o título da escala."
        }

        val inicio = converterData(dataInicial)
            ?: return "Selecione uma data inicial válida."

        val fim = converterData(dataFinal)
            ?: return "Selecione uma data final válida."

        if (fim < inicio) {
            return "A data final não pode ser anterior à inicial."
        }

        if (local.isBlank()) {
            return "Informe o local ou unidade."
        }

        if (responsavel.isBlank()) {
            return "Informe o responsável pela escala."
        }

        if (participantes.isEmpty()) {
            return "Adicione pelo menos um participante."
        }

        participantes.forEachIndexed { indice, item ->

            if (item.nome.isBlank()) {
                return "Informe o nome do participante ${indice + 1}."
            }

            val data = converterData(item.data)
                ?: return "Informe a data do participante ${indice + 1}."

            if (data < inicio || data > fim) {
                return "A data do participante ${indice + 1} está fora do período."
            }

            if (!horarioValido(item.inicio) ||
                !horarioValido(item.fim)
            ) {
                return "Informe os horários do participante ${indice + 1}."
            }

            if (item.inicio == item.fim) {
                return "Os horários do participante ${indice + 1} não podem ser iguais."
            }
        }

        return null
    }
}
