package com.example.deber01b2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class AdministrarCanciones : AppCompatActivity() {

    private var idCancion: Int = -1
    private var idArtista: Int = -1

    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_administrar_canciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etTitulo = findViewById<EditText>(R.id.etTituloCanciones)
        val etDuracion = findViewById<EditText>(R.id.etDuracionCanciones)
        val etFechaCancion = findViewById<EditText>(R.id.etFechaCanciones)
        val botonGuardarCancion = findViewById<Button>(R.id.btnCrearCanciones)

        idArtista = intent.getIntExtra("ID_Artista", -1)
        idCancion = intent.getIntExtra("ID_Cancion", -1)

        if (idCancion != -1) {
            botonGuardarCancion.text = "Actualizar Cancion"

            val cancion = EBaseDeDatos.tablaCancion?.consultarCancionPorId(idCancion)
            cancion?.let {
                etTitulo.setText(it.titulo)
                etDuracion.setText(it.duracion.toString())
                etFechaCancion.setText(formatoFecha.format(it.fechaLanzamiento))
            }
        } else {
            botonGuardarCancion.text = "Crear Cancion"
        }

        botonGuardarCancion.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val duracion = etDuracion.text.toString().toDoubleOrNull()
            val fechaLanzamientoStr = etFechaCancion.text.toString()

            if (titulo.isNotEmpty() && duracion != null && fechaLanzamientoStr.isNotEmpty()) {

                val fecha = try {
                    formatoFecha.parse(fechaLanzamientoStr)
                } catch (e: ParseException) {
                    null
                }

                if (fecha != null){

                    val respuesta = if (idCancion == -1) {
                        EBaseDeDatos.tablaCancion?.crearCancion(titulo,duracion,fecha,idArtista)
                    } else {
                        EBaseDeDatos.tablaCancion?.actualizarCancion(idCancion, titulo, duracion, fecha)
                    }

                    if (respuesta == true) {
                        mostrarSnackbar(if (idCancion == -1) "Cancion creado exitosamente" else "Cancion actualizado exitosamente")

                        val intent = Intent(this, Canciones::class.java)
                        intent.putExtra("ID_Artista", idArtista)
                        startActivity(intent)
                        finish()

                    } else {
                        mostrarSnackbar("Error al guardar la cancion")
                    }
                }else{
                    mostrarSnackbar("Datos de fecha no válidos")
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



