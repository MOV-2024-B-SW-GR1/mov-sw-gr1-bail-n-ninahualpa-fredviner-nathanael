package com.example.deber01b2

import android.content.ContentValues
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Log

class ESqliteHelperArtista (private val dbHelper: ESqliteHelper) {

    //------------- CRUD ----------------------------

    fun crearArtista(
        nombre: String,
        popularidad: Double,
        esActivo: Boolean,
        fechaCreacion: Date,
        latitud: Double,
        longitud: Double
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresGuardar = ContentValues()

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaCreacion)

        valoresGuardar.put("nombre", nombre)
        valoresGuardar.put("popularidad", popularidad)
        valoresGuardar.put("esActivo", if (esActivo) 1 else 0)
        valoresGuardar.put("fechaCreacion", fechaFormateada)
        valoresGuardar.put("latitud", latitud)
        valoresGuardar.put("longitud", longitud)

        val resultadoGuardar = baseDatosEscritura.insert("Artista", null, valoresGuardar)

        baseDatosEscritura.close()
        return resultadoGuardar != -1L
    }

    fun eliminarArtista(id: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())

        val resultadoEliminar = baseDatosEscritura.delete(
            "Artista",
            "idArtista=?",
            parametrosConsultaDelete
        )

        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    fun consultarTodasLosArtistas(): List<BArtista> {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = "SELECT * FROM Artista"
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, null)

        val arregloRespuesta = arrayListOf<BArtista>()
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (resultadoConsultaLectura.moveToFirst()) {
            do {
                val artista = BArtista(
                    idArtista = resultadoConsultaLectura.getInt(0),
                    nombre = resultadoConsultaLectura.getString(1),
                    popularidad = resultadoConsultaLectura.getDouble(2),
                    esActivo = resultadoConsultaLectura.getInt(3) == 1,
                    fechaCreacion = formatoFecha.parse(resultadoConsultaLectura.getString(4))!!,
                    latitud = resultadoConsultaLectura.getDouble(5),
                    longitud = resultadoConsultaLectura.getDouble(6)
                )
                arregloRespuesta.add(artista)
            } while (resultadoConsultaLectura.moveToNext())
        }
        Log.d("ConsultaArtistas", "$arregloRespuesta")

        resultadoConsultaLectura.close()
        return arregloRespuesta
    }

    fun actualizarArtista(
        idArtista: Int,
        nombre: String,
        popularidad: Double,
        esActivo: Boolean,
        fechaCreacion: Date,
        latitud: Double,
        longitud: Double
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresAActualizar = ContentValues()

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaCreacion)

        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("popularidad", popularidad)
        valoresAActualizar.put("esActivo", if (esActivo) 1 else 0)
        valoresAActualizar.put("fechaCreacion", fechaFormateada)
        valoresAActualizar.put("latitud", latitud)
        valoresAActualizar.put("longitud", longitud)

        val parametrosConsultaActualizar = arrayOf(idArtista.toString())

        val resultadoActualizar = baseDatosEscritura.update(
            "Artista",
            valoresAActualizar,
            "idArtista=?",
            parametrosConsultaActualizar
        )

        baseDatosEscritura.close()
        return resultadoActualizar > 0
    }

    fun consultarArtistaPorId(idArtista: Int): BArtista? {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = "SELECT * FROM Artista WHERE idArtista = ?"
        val parametrosConsultaLectura = arrayOf(idArtista.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        if (resultadoConsultaLectura.moveToFirst()) {
            val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val artista = BArtista(
                idArtista = resultadoConsultaLectura.getInt(0),
                nombre = resultadoConsultaLectura.getString(1),
                popularidad = resultadoConsultaLectura.getDouble(2),
                esActivo = resultadoConsultaLectura.getInt(3) == 1,
                fechaCreacion = formatoFecha.parse(resultadoConsultaLectura.getString(4))!!,
                latitud = resultadoConsultaLectura.getDouble(5),
                longitud = resultadoConsultaLectura.getDouble(6)
            )
            resultadoConsultaLectura.close()
            return artista
        }
        resultadoConsultaLectura.close()
        return null
    }
}