package com.escalarelatorio.app.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "participantes",
    foreignKeys = [
        ForeignKey(
            entity = EscalaEntity::class,
            parentColumns = ["id"],
            childColumns = ["escalaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("escalaId")]
)
data class ParticipanteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val escalaId: Long,
    val nome: String,
    val funcao: String,
    val data: String,
    val inicio: String,
    val fim: String,
    val observacoes: String = ""
)
