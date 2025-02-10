package com.example.deber01b2

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BCancion (
    val idCancion: Int,
    val titulo: String,
    val duracion: Double,
    val fechaLanzamiento: Date,
    val idArtista: Int
):Parcelable {

    override fun toString(): String {
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateada = formatoFecha.format(fechaLanzamiento)
        return "$titulo - ($duracion) \nLanzamiento: $fechaFormateada"
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readDouble(),
        Date(parcel.readLong()),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idCancion)
        parcel.writeString(titulo)
        parcel.writeDouble(duracion)
        parcel.writeLong(fechaLanzamiento.time)
        parcel.writeInt(idArtista)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BCancion> {
        override fun createFromParcel(parcel: Parcel): BCancion {
            return BCancion(parcel)
        }

        override fun newArray(size: Int): Array<BCancion?> {
            return arrayOfNulls(size)
        }
    }
}