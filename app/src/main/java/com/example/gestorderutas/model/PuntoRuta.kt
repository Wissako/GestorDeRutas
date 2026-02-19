package com.example.gestorderutas.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(
    tableName = "punto_ruta",
    foreignKeys = [ForeignKey(
        entity = Ruta::class,
        parentColumns = ["id"],
        childColumns = ["rutaId"]
    )]
)
data class PuntoRuta (
    @PrimaryKey val id: Long = System.currentTimeMillis(),
    val rutaId: Long,
    val lat: Double,
    val lng: Double,
    val timestamp: Long = System.currentTimeMillis()
)
