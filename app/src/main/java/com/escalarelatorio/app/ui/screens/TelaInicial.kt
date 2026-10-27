package com.escalarelatorio.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TelaInicial(novaEscala: () -> Unit) {

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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Gestão de Escalas",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Crie, organize e compartilhe escalas de trabalho, plantão e serviço.",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = novaEscala,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("＋ Nova Escala")
            }

            HorizontalDivider()

            Text(
                text = "Minhas escalas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Nenhuma escala cadastrada.\nClique em Nova Escala para começar.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}