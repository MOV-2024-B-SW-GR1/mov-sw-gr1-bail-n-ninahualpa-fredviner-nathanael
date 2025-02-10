package com.example.deber01b2

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BArtista (
    val idArtista: Int,
    val nombre: String,
    val popularidad: Double,
    val esActivo: Boolean,
    val fechaCreacion: Date,
    val latitud: Double,
    val longitud: Double
):Parcelable {

    override fun toString(): String {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaCreacion)
        val tipo = if (esActivo) "Activo" else "Inactivo"
        return "$nombre - ($tipo) \nPopularidad: ($popularidad) \nCreado: $fechaFormateada \nUbicación: ($latitud, $longitud)"
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readByte() != 0.toByte(),
        Date(parcel.readLong()),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idArtista)
        parcel.writeString(nombre)
        parcel.writeDouble(popularidad)
        parcel.writeByte(if (esActivo) 1 else 0)
        parcel.writeLong(fechaCreacion.time)
        parcel.writeDouble(latitud)
        parcel.writeDouble(longitud)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BArtista> {
        override fun createFromParcel(parcel: Parcel): BArtista {
            return BArtista(parcel)
        }

        override fun newArray(size: Int): Array<BArtista?> {
            return arrayOfNulls(size)
        }
    }
}
