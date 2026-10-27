
package com.escalarelatorio.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    if (mostrarFormulario) {
        TelaNovaEscala(
            voltar = { mostrarFormulario = false }
        )
    } else {
        TelaInicial(
            novaEscala = { mostrarFormulario = true }
        )
    }
}




