package com.example.deber01b2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AdministrarArtistas : AppCompatActivity() {

    private var idArtista: Int = -1
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_administrar_artistas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNombre = findViewById<EditText>(R.id.etNombreArtista)
        val etPopularidad = findViewById<EditText>(R.id.etPopularidadArtista)
        val switchActividad = findViewById<Switch>(R.id.switchActividadArtista)
        val etFechaCreacion = findViewById<EditText>(R.id.etFechaCreacionArtista)
        val etLatitud = findViewById<EditText>(R.id.etLatitudArtista)
        val etLongitud = findViewById<EditText>(R.id.etLongitudArtista)
        val botonGuardarArtista = findViewById<Button>(R.id.btnCrearArtista)

        idArtista = intent.getIntExtra("ID_Artista", -1)

        if (idArtista != -1) {
            botonGuardarArtista.text = "Actualizar Artista"

            val artista = EBaseDeDatos.tablaArtista?.consultarArtistaPorId(idArtista)
            artista?.let {
                etNombre.setText(it.nombre)
                etPopularidad.setText(it.popularidad.toString())
                switchActividad.isChecked = it.esActivo
                etFechaCreacion.setText(formatoFecha.format(it.fechaCreacion))
            }
        } else {
            botonGuardarArtista.text = "Crear Artista"
        }

        botonGuardarArtista.setOnClickListener {
            val nombre = etNombre.text.toString()
            val popularidad = etPopularidad.text.toString().toDoubleOrNull()
            val esActivo = switchActividad.isChecked
            val fechaCreacionStr = etFechaCreacion.text.toString()
            val latitudStr = etLatitud.text.toString()
            val longitudStr = etLongitud.text.toString()

            if (nombre.isNotEmpty() && popularidad != null && fechaCreacionStr.isNotEmpty() &&
                latitudStr.isNotEmpty() && longitudStr.isNotEmpty()) {

                val fecha = try {
                    formatoFecha.parse(fechaCreacionStr)
                } catch (e: ParseException) {
                    null
                }

                val latitud = latitudStr.toDoubleOrNull()
                val longitud = longitudStr.toDoubleOrNull()

                if (fecha != null && latitud != null && longitud != null) {
                    val respuesta = if (idArtista == -1) {
                        EBaseDeDatos.tablaArtista?.crearArtista(nombre, popularidad , esActivo, fecha, latitud, longitud)
                    } else {
                        EBaseDeDatos.tablaArtista?.actualizarArtista(idArtista, nombre, popularidad, esActivo, fecha, latitud, longitud)
                    }

                    if (respuesta == true) {
                        mostrarSnackbar(if (idArtista == -1) "Artista creada exitosamente" else "Artista actualizada exitosamente")

                        val intent = Intent(this, Artistas::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        mostrarSnackbar("Error al guardar la Artista")
                    }
                } else {
                    mostrarSnackbar("Datos de fecha, latitud o longitud no válidos")
                }
            } else {
                mostrarSnackbar("Por favor, completa todos los campos correctamente")
            }
        }

    }

    private fun mostrarSnackbar(texto: String) {
        Snackbar.make(findViewById(R.id.main), texto, Snackbar.LENGTH_LONG).show()
    }

}