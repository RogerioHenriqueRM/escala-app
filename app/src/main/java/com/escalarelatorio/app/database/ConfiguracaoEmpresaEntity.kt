
package com.escalarelatorio.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configuracao_empresa")
data class ConfiguracaoEmpresaEntity(

    @PrimaryKey
    val id: Int = 1,

    val nomeEmpresa: String = "Minha Empresa",

    val corPrimaria: String = "#173E72",

    val corSecundaria: String = "#E9EEF5",

    val textoRodape: String = "Gerado pelo EscalaApp",

    val caminhoLogo: String? = null
)

