package com.escalarelatorio.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.escalarelatorio.app.model.ParticipanteEscala

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaNovaEscala(voltar: () -> Unit) {

    var titulo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Trabalho") }
    var dataInicial by remember { mutableStateOf("") }
    var dataFinal by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var responsavel by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    var menuAberto by remember { mutableStateOf(false) }
    var mostrarResumo by remember { mutableStateOf(false) }

    val tipos = listOf(
        "Trabalho",
        "Plantão",
        "Serviço",
        "Equipe",
        "Evento",
        "Outro"
    )

    val participantes = remember {
        mutableStateListOf(ParticipanteEscala())
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
            text = "Nova Escala",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título da escala") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = menuAberto,
            onExpandedChange = { menuAberto = it }
        ) {

            OutlinedTextField(
                value = tipo,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de escala") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menuAberto
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = menuAberto,
                onDismissRequest = { menuAberto = false }
            ) {

                tipos.forEach { opcao ->

                    DropdownMenuItem(
                        text = { Text(opcao) },
                        onClick = {
                            tipo = opcao
                            menuAberto = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = dataInicial,
            onValueChange = { dataInicial = it },
            label = { Text("Data inicial (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = dataFinal,
            onValueChange = { dataFinal = it },
            label = { Text("Data final (DD/MM/AAAA)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = local,
            onValueChange = { local = it },
            label = { Text("Local / Unidade") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = responsavel,
            onValueChange = { responsavel = it },
            label = { Text("Responsável") },
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider()

        Text(
            text = "Participantes da escala",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        participantes.forEachIndexed { indice, participante ->

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Participante ${indice + 1}",
                            fontWeight = FontWeight.Bold
                        )

                        if (participantes.size > 1) {
                            TextButton(
                                onClick = {
                                    participantes.removeAt(indice)
                                }
                            ) {
                                Text("Remover")
                            }
                        }
                    }

                    OutlinedTextField(
                        value = participante.nome,
                        onValueChange = {
                            participantes[indice] =
                                participante.copy(nome = it)
                        },
                        label = { Text("Nome / Equipe") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = participante.funcao,
                        onValueChange = {
                            participantes[indice] =
                                participante.copy(funcao = it)
                        },
                        label = { Text("Função / Atividade") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = participante.data,
                        onValueChange = {
                            participantes[indice] =
                                participante.copy(data = it)
                        },
                        label = { Text("Data (DD/MM/AAAA)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {

                        OutlinedTextField(
                            value = participante.inicio,
                            onValueChange = {
                                participantes[indice] =
                                    participante.copy(inicio = it)
                            },
                            label = { Text("Início") },
                            placeholder = { Text("08:00") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = participante.fim,
                            onValueChange = {
                                participantes[indice] =
                                    participante.copy(fim = it)
                            },
                            label = { Text("Fim") },
                            placeholder = { Text("17:00") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = participante.observacoes,
                        onValueChange = {
                            participantes[indice] =
                                participante.copy(observacoes = it)
                        },
                        label = { Text("Observações") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        OutlinedButton(
            onClick = {
                participantes.add(ParticipanteEscala())
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Adicionar participante")
        }

        OutlinedTextField(
            value = observacoes,
            onValueChange = { observacoes = it },
            label = { Text("Observações gerais") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                mostrarResumo = true
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = titulo.isNotBlank() &&
                    participantes.any { it.nome.isNotBlank() }
        ) {
            Text("Pré-visualizar Escala")
        }
    }

    if (mostrarResumo) {

        AlertDialog(
            onDismissRequest = { mostrarResumo = false },
            title = { Text("Resumo da Escala") },
            text = {

                Column(
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    Text("Título: $titulo")
                    Text("Tipo: $tipo")
                    Text("Período: $dataInicial até $dataFinal")
                    Text("Local: $local")
                    Text("Responsável: $responsavel")

                    HorizontalDivider()

                    participantes.forEachIndexed { index, item ->

                        Text(
                            "Participante ${index + 1}: ${item.nome}"
                        )

                        Text(
                            "Função: ${item.funcao}"
                        )

                        Text(
                            "Data: ${item.data}"
                        )

                        Text(
                            "Horário: ${item.inicio} - ${item.fim}"
                        )

                        if (item.observacoes.isNotBlank()) {
                            Text("Obs.: ${item.observacoes}")
                        }

                        HorizontalDivider()
                    }

                    if (observacoes.isNotBlank()) {
                        Text("Observações gerais: $observacoes")
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { mostrarResumo = false }
                ) {
                    Text("Fechar")
                }
            }
        )
    }
}




