package com.example.deber01b2

import android.content.ContentValues
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ESqliteHelperCancion (
    private val dbHelper: ESqliteHelper)
{
    //-------------CRUD----------------------------
    fun crearCancion(
        titulo: String,
        duracion: Double,
        fechaLanzamiento: Date,
        idArtista: Int
    ): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresGuardar = ContentValues()

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaLanzamiento)

        valoresGuardar.put("titulo", titulo)
        valoresGuardar.put("duracion", duracion)
        valoresGuardar.put("fechaLanzamiento", fechaFormateada)
        valoresGuardar.put("idArtista", idArtista)

        val resultadoGuardar = baseDatosEscritura.insert(
            "Cancion",
            null,
            valoresGuardar
        )

        baseDatosEscritura.close()

        return resultadoGuardar != -1L
    }


    fun consultarCancionesPorArtista(idArtista: Int): List<BCancion> {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = """
        SELECT * FROM Cancion
        WHERE idArtista = ?
    """.trimIndent()

        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, arrayOf(idArtista.toString()))
        val existeAlMenosUno = resultadoConsultaLectura.moveToFirst()

        val arregloRespuesta = arrayListOf<BCancion>()
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (existeAlMenosUno) {
            do {
                val cancion = BCancion(
                    resultadoConsultaLectura.getInt(0),
                    resultadoConsultaLectura.getString(1)!!,
                    resultadoConsultaLectura.getDouble(2),
                    formatoFecha.parse(resultadoConsultaLectura.getString(3))!!,
                    resultadoConsultaLectura.getInt(4)
                )
                arregloRespuesta.add(cancion)
            } while (resultadoConsultaLectura.moveToNext())
        }

        Log.d("Canciones", "Canciones encontrados: $arregloRespuesta")

        resultadoConsultaLectura.close()
        return arregloRespuesta
    }

    fun eliminarCancion(idCancion: Int): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val parametrosConsultaDelete = arrayOf(idCancion.toString())

        val resultadoEliminar = baseDatosEscritura.delete(
            "Cancion",
            "idCancion=?",
            parametrosConsultaDelete
        )

        baseDatosEscritura.close()
        return resultadoEliminar > 0
    }

    fun actualizarCancion(idCancion: Int, titulo: String, duracion: Double, fechaLanzamiento: Date): Boolean {
        val baseDatosEscritura = dbHelper.writableDatabase
        val valoresAActualizar = ContentValues()

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaLanzamiento)

        valoresAActualizar.put("titulo", titulo)
        valoresAActualizar.put("duracion", duracion)
        valoresAActualizar.put("fechaLanzamiento", fechaFormateada)

        val parametrosConsultaActualizar = arrayOf(idCancion.toString())

        val resultadoActualizar = baseDatosEscritura.update(
            "Cancion",
            valoresAActualizar,
            "idCancion=?",
            parametrosConsultaActualizar
        )

        baseDatosEscritura.close()

        return resultadoActualizar > 0
    }



    fun consultarCancionPorId(idCancion: Int): BCancion? {
        val baseDatosLectura = dbHelper.readableDatabase
        val scriptConsultaLectura = """
        SELECT * FROM Cancion WHERE idCancion = ?
    """.trimIndent()

        val parametrosConsultaLectura = arrayOf(idCancion.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(scriptConsultaLectura, parametrosConsultaLectura)

        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        if (resultadoConsultaLectura.moveToFirst()) {
            val cancion = BCancion(
                idCancion = resultadoConsultaLectura.getInt(0),
                titulo = resultadoConsultaLectura.getString(1),
                duracion = resultadoConsultaLectura.getDouble(2),
                fechaLanzamiento = formatoFecha.parse(resultadoConsultaLectura.getString(3))!!,
                idArtista = resultadoConsultaLectura.getInt(4)
            )
            resultadoConsultaLectura.close()
            return cancion
        } else {
            resultadoConsultaLectura.close()
            return null
        }
    }


}