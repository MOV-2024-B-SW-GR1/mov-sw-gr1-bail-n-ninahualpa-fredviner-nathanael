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

class Canciones : AppCompatActivity() {

    var canciones = mutableListOf<BCancion>()
    var idArtista: Int = 0

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_canciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        listView = findViewById<ListView>(R.id.lv_canciones)

        idArtista = intent.getIntExtra("ID_Artista", -1)
        if (idArtista != -1) {
            actualizarListaCanciones(idArtista)
        }

        val botonCrearCancion = findViewById<Button>(R.id.btnIrCrearCanciones)
        botonCrearCancion.setOnClickListener {
            val intent = Intent(this, AdministrarCanciones::class.java)
            intent.putExtra("ID_Artista", idArtista)
            startActivity(intent)
        }
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
            R.id.mi_editar -> {
                val CancionSeleccionado = canciones[posicionItemSeleccionado]
                val intent = Intent(this, AdministrarCanciones::class.java)
                intent.putExtra("ID_Cancion", CancionSeleccionado.idCancion)
                intent.putExtra("ID_Artista", idArtista)
                startActivity(intent)
                finish()
                return true
            }

            R.id.mi_eliminar -> {
                abrirDialogo()
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    fun abrirDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Deseas Eliminar este Cancion?")

        builder.setPositiveButton("Aceptar") { dialog, which ->
            if (posicionItemSeleccionado != -1) {
                val CancionSeleccionado = canciones[posicionItemSeleccionado]
                val exito = EBaseDeDatos.tablaCancion?.eliminarCancion(CancionSeleccionado.idCancion)
                if (exito == true) {
                    mostrarSnackbar("Cancion eliminado exitosamente")
                    actualizarListaCanciones(idArtista)
                } else {
                    mostrarSnackbar("Error al eliminar el Cancion")
                }
            }
        }

        builder.setNegativeButton("Cancelar", null)

        val dialogo = builder.create()
        dialogo.show()
    }


    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.main),
            texto,
            Snackbar.LENGTH_INDEFINITE
        )
        snack.show()
    }

    fun irActividad(clase:Class<*>){
        startActivity(Intent(this, clase))
    }

    fun actualizarListaCanciones(idArtista: Int) {
        canciones.clear()
        canciones.addAll(EBaseDeDatos.tablaCancion?.consultarCancionesPorArtista(idArtista) ?: listOf())

        if (canciones.isNotEmpty()) {
            val adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                canciones
            )
            listView.adapter = adaptador
            adaptador.notifyDataSetChanged()
        } else {
            listView.adapter = null
        }
    }

}