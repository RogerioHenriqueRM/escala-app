
package com.escalarelatorio.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.escalarelatorio.app.ui.screens.TelaDetalhesEscala
import com.escalarelatorio.app.ui.screens.TelaEdicaoEscala
import com.escalarelatorio.app.ui.screens.TelaInicial
import com.escalarelatorio.app.ui.screens.TelaNovaEscala

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                EscalaApp()
            }
        }
    }
}



@Composable
fun EscalaApp() {

    var telaAtual by remember {
        mutableStateOf("inicio")
    }

    var escalaSelecionadaId by remember {
        mutableLongStateOf(0L)
    }

    when (telaAtual) {

        "inicio" -> {
            TelaInicial(
                novaEscala = {
                    telaAtual = "formulario"
                },
                abrirEscala = { id ->
                    escalaSelecionadaId = id
                    telaAtual = "detalhes"
                }
            )
        }

        "formulario" -> {
            TelaNovaEscala(
                voltar = {
                    telaAtual = "inicio"
                }
            )
        }

        "detalhes" -> {
            TelaDetalhesEscala(
                escalaId = escalaSelecionadaId,
                voltar = {
                    telaAtual = "inicio"
                },
                editar = { id ->
                    escalaSelecionadaId = id
                    telaAtual = "edicao"
                }
            )
        }

        "edicao" -> {
            TelaEdicaoEscala(
                escalaId = escalaSelecionadaId,
                voltar = {
                    telaAtual = "detalhes"
                },
                concluido = {
                    telaAtual = "inicio"
                }
            )
        }
    }
}
