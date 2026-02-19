package com.example.gestorderutas.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GestorRutasDao {

    //Métodos para Ruta
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertarRuta(ruta: Ruta): Long

    @Update
    suspend fun actualizarRuta(ruta: Ruta)

    @Query("SELECT * FROM ruta ORDER BY id DESC")
    fun obtenerTodasLasRutas(): Flow<List<Ruta>>

    //Métodos para Punto De Ruta
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertarPuntoRuta(punto: PuntoRuta)

    @Query("SELECT * FROM punto_ruta WHERE rutaId = :rutaId ORDER BY timestamp ASC")
    fun obtenerPuntosDeRuta(rutaId: Long): Flow<List<PuntoRuta>>

    //Métodos para Waypoint
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertarWaypoint(waypoint: Waypoint)

    @Query("SELECT * FROM waypoint WHERE rutaId = :rutaId")
    fun obtenerWaypointsDeRuta(rutaId: Long): Flow<List<Waypoint>>
}