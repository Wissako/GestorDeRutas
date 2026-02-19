package com.example.gestorderutas.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "waypoint",
    foreignKeys = [ForeignKey(
        entity = Ruta::class,
        parentColumns = ["id"],
        childColumns = ["rutaId"]
    )]
)

data class Waypoint (
@PrimaryKey val id: Long = System.currentTimeMillis(),
    val rutaId: Long,
    val lat: Double,
    val lng: Double,
    val descripcion: String,
    val fotoPath: String? = null //Opcional
)