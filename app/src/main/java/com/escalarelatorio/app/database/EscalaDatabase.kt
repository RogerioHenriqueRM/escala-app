package com.escalarelatorio.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        EscalaEntity::class,
        ParticipanteEntity::class
    ],
    version = 1,
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
                ).build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}
