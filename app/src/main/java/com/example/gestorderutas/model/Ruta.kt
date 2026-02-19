package com.example.gestorderutas.model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(tableName = "ruta")
data class Ruta(
    @PrimaryKey val id: Long = System.currentTimeMillis(),
            val nombre: String,
            val distancia: Double =0.0,//Metros
            val duracion: Long = 0L,//Milisegundos
            val velocidadMedia: Double = 0.0 //KM/H
)