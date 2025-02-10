package com.example.deber01b2

import android.content.Context

class EBaseDeDatos  {
    companion object{
        var dbHelper: ESqliteHelper? = null
        var tablaCancion: ESqliteHelperCancion? = null
        var tablaArtista: ESqliteHelperArtista? = null

        fun iniciarBaseDeDatos(contexto: Context) {
            dbHelper = ESqliteHelper(contexto)
            tablaCancion = ESqliteHelperCancion(dbHelper!!)
            tablaArtista = ESqliteHelperArtista(dbHelper!!)
        }
    }
}