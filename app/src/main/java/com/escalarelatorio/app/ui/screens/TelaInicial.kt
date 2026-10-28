package com.escalarelatorio.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.escalarelatorio.app.database.EscalaDatabase
import com.escalarelatorio.app.database.EscalaEntity

@Composable
fun TelaInicial(novaEscala: () -> Unit) {

    val context = LocalContext.current

    val dao = remember(context) {
        EscalaDatabase.obterBanco(context).escalaDao()
    }

    val fluxoEscalas = remember(dao) {
        dao.listarEscalas()
    }

    val escalas by fluxoEscalas.collectAsState(
        initial = emptyList()
    )

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    text = "EscalaApp",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Gestão de Escalas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Crie e gerencie suas escalas.",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = novaEscala,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Nova Escala")
            }

            HorizontalDivider()

            Text(
                text = "Minhas escalas (${escalas.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (escalas.isEmpty()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma escala cadastrada.\nCrie sua primeira escala!",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = escalas,
                        key = { it.id }
                    ) { escala ->

                        CardEscala(escala)
                    }
                }
            }
        }
    }
}

@Composable
fun CardEscala(escala: EscalaEntity) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Text(
                text = escala.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Tipo: ${escala.tipo}"
            )

            Text(
                text = "Período: ${escala.dataInicial} até ${escala.dataFinal}"
            )

            Text(
                text = "Local: ${escala.local}"
            )

            Text(
                text = "Responsável: ${escala.responsavel}"
            )
        }
    }
}
