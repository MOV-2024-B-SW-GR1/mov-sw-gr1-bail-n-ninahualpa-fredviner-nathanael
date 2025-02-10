package com.example.deber01b2

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class Artistas : AppCompatActivity() {
    var artistas = mutableListOf<BArtista>()

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_artistas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonIrCrearArtistas = findViewById<Button>(R.id.btnIrCrearArtistas)
        botonIrCrearArtistas.setOnClickListener {
            irActividad(AdministrarArtistas::class.java)
        }

        listView = findViewById<ListView>(R.id.lv_lista_artistas)

        actualizarListaArtistas()

        registerForContextMenu(listView)

    }

    var posicionItemSeleccionado = -1


    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_ver_cancion -> {

                val artistaSeleccionada = artistas[posicionItemSeleccionado]
                val intent = Intent(this, Canciones::class.java)
                intent.putExtra("ID_Artista", artistaSeleccionada.idArtista)
                startActivity(intent)
                return true
            }

            R.id.mi_editar -> {
                val artistaSeleccionada = artistas[posicionItemSeleccionado]
                val intent = Intent(this, AdministrarArtistas::class.java)
                intent.putExtra("ID_Artista", artistaSeleccionada.idArtista)
                startActivity(intent)
                finish()
                return true
            }

            R.id.mi_eliminar -> {
                abrirDialogo()
                return true
            }

            R.id.ver_ubicacion -> {
                val artistaSeleccionada = artistas[posicionItemSeleccionado]
                val intent = Intent(this, GGoogleMaps::class.java)

                intent.putExtra("LATITUD", artistaSeleccionada.latitud)
                intent.putExtra("LONGITUD", artistaSeleccionada.longitud)
                intent.putExtra("NOMBRE_Artista", artistaSeleccionada.nombre)

                startActivity(intent)

                return true
            }


            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Deseas Eliminar este Artista?")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            if (posicionItemSeleccionado != -1) {
                val artistaSeleccionada = artistas[posicionItemSeleccionado]
                val exito = EBaseDeDatos.tablaArtista?.eliminarArtista(artistaSeleccionada.idArtista)
                if (exito == true) {
                    mostrarSnackbar("Artista eliminado exitosamente")
                    actualizarListaArtistas()
                } else {
                    mostrarSnackbar("Error al eliminar el Artista")
                }
            }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialogo = builder.create()
        dialogo.show()


    }

    fun actualizarListaArtistas() {
        artistas.clear()
        artistas.addAll(EBaseDeDatos.tablaArtista?.consultarTodasLosArtistas() ?: listOf())

        if (artistas.isNotEmpty()) {
            val adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                artistas
            )
            listView.adapter = adaptador
            adaptador.notifyDataSetChanged()
        } else {
            listView.adapter = null
        }
    }

    fun irActividad(clase: Class<*>) {
        startActivity(Intent(this, clase))
    }

}