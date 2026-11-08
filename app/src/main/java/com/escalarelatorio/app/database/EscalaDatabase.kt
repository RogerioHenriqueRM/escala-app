package com.escalarelatorio.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS configuracao_empresa (
                id INTEGER NOT NULL,
                nomeEmpresa TEXT NOT NULL,
                corPrimaria TEXT NOT NULL,
                corSecundaria TEXT NOT NULL,
                textoRodape TEXT NOT NULL,
                caminhoLogo TEXT,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )
    }
}


@Database(
    entities = [
        EscalaEntity::class,
        ParticipanteEntity::class,
        ConfiguracaoEmpresaEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class EscalaDatabase : RoomDatabase() {

    abstract fun escalaDao(): EscalaDao

    companion object {

        @Volatile
        private var INSTANCE: EscalaDatabase? = null

        fun obterBanco(context: Context): EscalaDatabase {
            return INSTANCE ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    EscalaDatabase::class.java,
                    "escala_app.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()


                INSTANCE = instancia
                instancia
            }
        }
    }
}
