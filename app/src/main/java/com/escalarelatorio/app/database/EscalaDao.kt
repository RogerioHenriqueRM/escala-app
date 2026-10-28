package com.escalarelatorio.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface EscalaDao {

    @Insert
    suspend fun inserirEscala(
        escala: EscalaEntity
    ): Long

    @Insert
    suspend fun inserirParticipantes(
        participantes: List<ParticipanteEntity>
    )

    @Query("SELECT * FROM escalas ORDER BY criadoEm DESC")
    fun listarEscalas(): Flow<List<EscalaEntity>>

    @Query("SELECT * FROM escalas WHERE id = :id")
    suspend fun buscarEscala(id: Long): EscalaEntity?

    @Query("SELECT * FROM participantes WHERE escalaId = :id ORDER BY id")
    suspend fun buscarParticipantes(
        id: Long
    ): List<ParticipanteEntity>

    @Update
    suspend fun atualizarEscala(escala: EscalaEntity)

    @Delete
    suspend fun excluirEscala(escala: EscalaEntity)

    @Transaction
    suspend fun salvarEscalaCompleta(
        escala: EscalaEntity,
        participantes: List<ParticipanteEntity>
    ): Long {
        val escalaId = inserirEscala(escala)

        inserirParticipantes(
            participantes.map {
                it.copy(escalaId = escalaId)
            }
        )

        return escalaId
    }
}
