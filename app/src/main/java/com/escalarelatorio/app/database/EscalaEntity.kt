package com.escalarelatorio.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "escalas")
data class EscalaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val titulo: String,
    val tipo: String,
    val dataInicial: String,
    val dataFinal: String,
    val local: String,
    val responsavel: String,
    val observacoes: String = "",
    val criadoEm: Long = System.currentTimeMillis()
)
