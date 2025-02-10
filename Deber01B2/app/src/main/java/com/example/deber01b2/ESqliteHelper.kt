package com.example.deber01b2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelper (
    contexto: Context?
) : SQLiteOpenHelper(
    contexto,
    "movilesExamen",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaArtista = """
            CREATE TABLE Artista (
            idArtista INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            popularidad REAL,
            esActivo BOOLEAN NOT NULL,
            fechaCreacion DATE NOT NULL,
            latitud REAL,
            longitud REAL
            );
        """.trimIndent()

        val scriptSQLCrearTablaCancion = """
            CREATE TABLE Cancion (
            idCancion INTEGER PRIMARY KEY AUTOINCREMENT,
            titulo TEXT NOT NULL,
            duracion REAL,
            fechaLanzamiento DATE NOT NULL,
            idArtista INTEGER NOT NULL,
            FOREIGN KEY (idArtista) REFERENCES Artista(idArtista) ON DELETE CASCADE
            );
        """.trimIndent()

        db?.execSQL(scriptSQLCrearTablaArtista)
        db?.execSQL(scriptSQLCrearTablaCancion)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Artista")
        db?.execSQL("DROP TABLE IF EXISTS Cancion")
        onCreate(db)
    }
}