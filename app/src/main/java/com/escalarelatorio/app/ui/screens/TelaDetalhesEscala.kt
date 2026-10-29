package com.escalarelatorio.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.escalarelatorio.app.database.EscalaDatabase
import com.escalarelatorio.app.database.EscalaEntity
import com.escalarelatorio.app.database.ParticipanteEntity
import kotlinx.coroutines.launch

@Composable
fun TelaDetalhesEscala(
    escalaId: Long,
    voltar: () -> Unit,
    editar: (Long) -> Unit
) {
    val context = LocalContext.current
    val dao = remember(context) {
        EscalaDatabase.obterBanco(context).escalaDao()
    }
    val scope = rememberCoroutineScope()

    var escala by remember(escalaId) {
        mutableStateOf<EscalaEntity?>(null)
    }
    var participantes by remember(escalaId) {
        mutableStateOf<List<ParticipanteEntity>>(emptyList())
    }
    var carregando by remember(escalaId) {
        mutableStateOf(true)
    }
    var processando by remember { mutableStateOf(false) }
    var confirmarExclusao by remember { mutableStateOf(false) }
    var erro by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(escalaId) {
        try {
            escala = dao.buscarEscala(escalaId)
            participantes = dao.buscarParticipantes(escalaId)
        } catch (e: Exception) {
            erro = e.localizedMessage ?: "Erro ao carregar escala"
        } finally {
            carregando = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = voltar) {
            Text("← Voltar")
        }

        Text(
            "Detalhes da Escala",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        if (carregando) {
            CircularProgressIndicator()
        } else {
            val registro = escala

            if (registro == null) {
                Text("Escala não encontrada.")
            } else {
                Text(
                    registro.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text("Tipo: ${registro.tipo}")
                Text("Início: ${registro.dataInicial}")
                Text("Fim: ${registro.dataFinal}")
                Text("Local: ${registro.local}")
                Text("Responsável: ${registro.responsavel}")

                if (registro.observacoes.isNotBlank()) {
                    Text("Observações: ${registro.observacoes}")
                }

                HorizontalDivider()

                Text(
                    "Participantes (${participantes.size})",
                    style = MaterialTheme.typography.titleLarge
                )

                participantes.forEach { participante ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                participante.nome,
                                fontWeight = FontWeight.Bold
                            )
                            Text("Função: ${participante.funcao}")
                            Text("Data: ${participante.data}")
                            Text(
                                "Horário: ${participante.inicio} " +
                                        "até ${participante.fim}"
                            )

                            if (participante.observacoes.isNotBlank()) {
                                Text(
                                    "Obs.: ${participante.observacoes}"
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { editar(escalaId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !processando
                ) {
                    Text("Editar Escala")
                }

                OutlinedButton(
                    onClick = {
                        processando = true
                        erro = null

                        scope.launch {
                            try {
                                dao.duplicarEscala(escalaId)
                                voltar()
                            } catch (e: Exception) {
                                erro = e.localizedMessage
                                    ?: "Erro ao duplicar"
                            } finally {
                                processando = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !processando
                ) {
                    Text("Duplicar Escala")
                }

                OutlinedButton(
                    onClick = { confirmarExclusao = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !processando,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Excluir Escala")
                }
            }
        }

        erro?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    if (confirmarExclusao) {
        AlertDialog(
            onDismissRequest = {
                if (!processando) confirmarExclusao = false
            },
            title = { Text("Excluir escala?") },
            text = {
                Text(
                    "A escala e todos os participantes serão " +
                            "excluídos permanentemente deste aparelho."
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !processando,
                    onClick = {
                        processando = true
                        erro = null

                        scope.launch {
                            try {
                                val registro = dao.buscarEscala(escalaId)
                                    ?: error("Escala não encontrada")

                                dao.excluirEscala(registro)
                                confirmarExclusao = false
                                voltar()
                            } catch (e: Exception) {
                                erro = e.localizedMessage
                                    ?: "Erro ao excluir"
                            } finally {
                                processando = false
                            }
                        }
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    enabled = !processando,
                    onClick = { confirmarExclusao = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
