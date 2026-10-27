package com.escalarelatorio.app.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun SeletorData(
    titulo: String,
    valor: String,
    aoSelecionar: (String) -> Unit
) {
    val context = LocalContext.current

    OutlinedButton(
        onClick = {
            val calendario = Calendar.getInstance()

            if (valor.isNotBlank()) {
                val formato = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale("pt", "BR")
                ).apply {
                    isLenient = false
                }

                formato.parse(valor)?.let {
                    calendario.time = it
                }
            }

            DatePickerDialog(
                context,
                { _, ano, mes, dia ->
                    val data = String.format(
                        Locale("pt", "BR"),
                        "%02d/%02d/%04d",
                        dia,
                        mes + 1,
                        ano
                    )

                    aoSelecionar(data)
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$titulo: ${
                valor.ifBlank { "Selecionar data" }
            }"
        )
    }
}

@Composable
fun SeletorHorario(
    titulo: String,
    valor: String,
    aoSelecionar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    OutlinedButton(
        onClick = {
            val calendario = Calendar.getInstance()
            val partes = valor.split(":")

            val hora = partes.getOrNull(0)
                ?.toIntOrNull()
                ?.takeIf { it in 0..23 }
                ?: calendario.get(Calendar.HOUR_OF_DAY)

            val minuto = partes.getOrNull(1)
                ?.toIntOrNull()
                ?.takeIf { it in 0..59 }
                ?: calendario.get(Calendar.MINUTE)

            TimePickerDialog(
                context,
                { _, horaEscolhida, minutoEscolhido ->
                    val horario = String.format(
                        Locale("pt", "BR"),
                        "%02d:%02d",
                        horaEscolhida,
                        minutoEscolhido
                    )

                    aoSelecionar(horario)
                },
                hora,
                minuto,
                true
            ).show()
        },
        modifier = modifier
    ) {
        Text(
            text = "$titulo: ${
                valor.ifBlank { "--:--" }
            }"
        )
    }
}
