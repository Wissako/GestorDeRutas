package com.example.gestorderutas.data

import com.example.gestorderutas.model.GestorRutasDao
import com.example.gestorderutas.model.Ruta
import com.example.gestorderutas.model.PuntoRuta
import com.example.gestorderutas.model.Waypoint
import kotlinx.coroutines.flow.Flow

class GestorRutasRepository(private val dao: GestorRutasDao) {
    //Rutas

    suspend fun insertarRuta(ruta: Ruta): Long {
        return dao.insertarRuta(ruta) // Devuelve el ID generado
    }

    suspend fun actualizarRuta(ruta: Ruta) {
        dao.actualizarRuta(ruta)
    }

    fun obtenerTodasLasRutas(): Flow<List<Ruta>> {
        return dao.obtenerTodasLasRutas()
    }

    // Puntos
    suspend fun guardarPuntoRuta(punto: PuntoRuta) {
        dao.insertarPuntoRuta(punto) // [cite: 336]
    }

    fun obtenerPuntosDeRuta(rutaId: Long): Flow<List<PuntoRuta>> {
        return dao.obtenerPuntosDeRuta(rutaId)
    }

    //WayPoints

    suspend fun insertarWaypoint(waypoint: Waypoint) {
        dao.insertarWaypoint(waypoint)
    }

    fun obtenerWaypointsDeRuta(rutaId: Long): Flow<List<Waypoint>> {
        return dao.obtenerWaypointsDeRuta(rutaId)
    }
}