package ec.edu.epn

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Artista(
    val idArtista: Int,
    val nombre: String,
    val popularidad: Double,
    val esActivo: Boolean,
    val fechaCreacion: LocalDate
)

data class Cancion(
    val idCancion: Int,
    val titulo: String,
    val duracion: Double,
    val fechaLanzamiento: LocalDate,
    val idArtista: Int
)

object FileManager {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun readArtistas(): MutableList<Artista> {
        val file = File("artistas.txt")
        if (!file.exists()) return mutableListOf()
        return file.readLines().map { line ->
            val parts = line.split(",")
            Artista(
                idArtista = parts[0].toInt(),
                nombre = parts[1],
                popularidad = parts[2].toDouble(),
                esActivo = parts[3].toBoolean(),
                fechaCreacion = LocalDate.parse(parts[4], formatter)
            )
        }.toMutableList()
    }

    fun saveArtistas(artistas: List<Artista>) {
        val file = File("artistas.txt")
        file.writeText(artistas.joinToString("\n") { artista ->
            "${artista.idArtista},${artista.nombre},${artista.popularidad},${artista.esActivo},${artista.fechaCreacion.format(formatter)}"
        })
    }

    fun readCanciones(): MutableList<Cancion> {
        val file = File("canciones.txt")
        if (!file.exists()) return mutableListOf()
        return file.readLines().map { line ->
            val parts = line.split(",")
            Cancion(
                idCancion = parts[0].toInt(),
                titulo = parts[1],
                duracion = parts[2].toDouble(),
                fechaLanzamiento = LocalDate.parse(parts[3], formatter),
                idArtista = parts[4].toInt()
            )
        }.toMutableList()
    }

    fun saveCanciones(canciones: List<Cancion>) {
        val file = File("canciones.txt")
        file.writeText(canciones.joinToString("\n") { cancion ->
            "${cancion.idCancion},${cancion.titulo},${cancion.duracion},${cancion.fechaLanzamiento.format(formatter)},${cancion.idArtista}"
        })
    }
}

object CRUD {
    private val artistas = FileManager.readArtistas()
    private val canciones = FileManager.readCanciones()

    fun createArtista(artista: Artista) {
        artistas.add(artista)
        FileManager.saveArtistas(artistas)
        println("Artista creado: $artista")
    }

    fun readArtistas() {
        println("Lista de artistas:")
        artistas.forEach { println(it) }
    }

    fun updateArtista(id: Int, newArtista: Artista) {
        val index = artistas.indexOfFirst { it.idArtista == id }
        if (index != -1) {
            artistas[index] = newArtista
            FileManager.saveArtistas(artistas)
            println("Artista actualizado: $newArtista")
        } else {
            println("Artista no encontrado")
        }
    }

    fun deleteArtista(idArtista: Any) {
        // Verificar si el artista existe
        val artistaEliminado = artistas.find { it.idArtista == idArtista }
        if (artistaEliminado != null) {
            // Eliminar el artista
            artistas.remove(artistaEliminado)

            // Eliminar canciones asociadas
            val cancionesEliminadas = canciones.filter { it.idArtista == idArtista }
            canciones.removeAll(cancionesEliminadas)

            // Guardar cambios en los archivos
            FileManager.saveArtistas(artistas)
            FileManager.saveCanciones(canciones)

            println("Artista y sus canciones asociadas eliminados exitosamente.")
            println("Artista eliminado: $artistaEliminado")
            println("Canciones eliminadas: $cancionesEliminadas")
        } else {
            println("Error: No existe un artista con el ID $idArtista.")
        }
    }

    fun createCancion(cancion: Cancion) {
        if (artistas.any { it.idArtista == cancion.idArtista }) {
            canciones.add(cancion)
            FileManager.saveCanciones(canciones)
            println("Canción creada: $cancion")
        } else {
            println("El ID del artista no existe. Canción no creada.")
        }
    }

    fun readCanciones() {
        println("Lista de canciones:")
        canciones.forEach { println(it) }
    }

    fun updateCancion(id: Int, newCancion: Cancion) {
        val index = canciones.indexOfFirst { it.idCancion == id }
        if (index != -1) {
            canciones[index] = newCancion
            FileManager.saveCanciones(canciones)
            println("Canción actualizada: $newCancion")
        } else {
            println("Canción no encontrada")
        }
    }

    fun deleteCancion(id: Int) {
        if (canciones.removeIf { it.idCancion == id }) {
            FileManager.saveCanciones(canciones)
            println("Canción eliminada con ID: $id")
        } else {
            println("Canción no encontrada")
        }
    }
}

fun main() {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    while (true) {
        println("Seleccione una opción:\n1. Crear Artista\n2. Leer Artistas\n3. Actualizar Artista\n4. Eliminar Artista")
        println("5. Crear Canción\n6. Leer Canciones\n7. Actualizar Canción\n8. Eliminar Canción\n9. Salir")
        when (readLine()?.toIntOrNull()) {
            1 -> {
                println("Ingrese datos del artista: ID, Nombre, Popularidad, EsActivo(true/false), FechaCreacion(yyyy-MM-dd)")
                val input = readLine()?.split(",") ?: continue
                CRUD.createArtista(
                    Artista(
                        idArtista = input[0].toInt(),
                        nombre = input[1],
                        popularidad = input[2].toDouble(),
                        esActivo = input[3].toBoolean(),
                        fechaCreacion = LocalDate.parse(input[4], formatter)
                    )
                )
            }
            2 -> CRUD.readArtistas()
            3 -> {
                println("Ingrese el ID del artista a actualizar y los nuevos datos")
                val id = readLine()?.toIntOrNull() ?: continue
                val input = readLine()?.split(",") ?: continue
                CRUD.updateArtista(
                    id,
                    Artista(
                        idArtista = id,
                        nombre = input[0],
                        popularidad = input[1].toDouble(),
                        esActivo = input[2].toBoolean(),
                        fechaCreacion = LocalDate.parse(input[3], formatter)
                    )
                )
            }
            4 -> {
                println("Ingrese el ID del artista que desea eliminar:")
                val idArtista = readLine()?.toIntOrNull() ?: run {
                    println("ID inválido.")
                    return@run
                }

                CRUD.deleteArtista(idArtista)
            }
            5 -> {
                println("Ingrese datos de la canción: ID, Titulo, Duracion, FechaLanzamiento(yyyy-MM-dd), IDArtista")
                val input = readLine()?.split(",") ?: continue
                CRUD.createCancion(
                    Cancion(
                        idCancion = input[0].toInt(),
                        titulo = input[1],
                        duracion = input[2].toDouble(),
                        fechaLanzamiento = LocalDate.parse(input[3], formatter),
                        idArtista = input[4].toInt()
                    )
                )
            }
            6 -> CRUD.readCanciones()
            7 -> {
                println("Ingrese el ID de la canción a actualizar y los nuevos datos")
                val id = readLine()?.toIntOrNull() ?: continue
                val input = readLine()?.split(",") ?: continue
                CRUD.updateCancion(
                    id,
                    Cancion(
                        idCancion = id,
                        titulo = input[0],
                        duracion = input[1].toDouble(),
                        fechaLanzamiento = LocalDate.parse(input[2], formatter),
                        idArtista = input[3].toInt()
                    )
                )
            }
            8 -> {
                println("Ingrese el ID de la canción a eliminar")
                val id = readLine()?.toIntOrNull() ?: continue
                CRUD.deleteCancion(id)
            }
            9 -> {
                println("Saliendo...")
                return
            }
            else -> println("Opción inválida")
        }
    }
}
