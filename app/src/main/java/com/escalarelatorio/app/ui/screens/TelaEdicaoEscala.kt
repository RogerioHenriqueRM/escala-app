package com.escalarelatorio.app.ui.screens

import androidx.compose.runtime.Composable

@Composable
fun TelaEdicaoEscala(
    escalaId: Long,
    voltar: () -> Unit,
    concluido: () -> Unit
) {
    TelaNovaEscala(
        escalaId = escalaId,
        voltar = voltar,
        aoSalvar = concluido
    )
}
