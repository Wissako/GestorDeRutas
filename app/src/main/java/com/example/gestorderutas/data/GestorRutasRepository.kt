package com.example.gestorderutas.data

import com.example.gestorderutas.model.GestorRutasDao
import com.example.gestorderutas.model.Ruta
import com.example.gestorderutas.model.PuntoRuta
import com.example.gestorderutas.model.Waypoint
import kotlinx.coroutines.flow.Flow

class GestorRutasRepository(private val dao: GestorRutasDao) {

    suspend fun insertarRuta(ruta: Ruta): Long {
        return dao.insertarRuta(ruta)
    }

    suspend fun actualizarRuta(ruta: Ruta) {
        dao.actualizarRuta(ruta)
    }

    suspend fun obtenerRutaPorId(id: Long): Ruta? {
        return dao.obtenerRutaPorId(id)
    }

    fun obtenerTodasLasRutas(): Flow<List<Ruta>> {
        return dao.obtenerTodasLasRutas()
    }

    suspend fun guardarPuntoRuta(punto: PuntoRuta) {
        dao.insertarPuntoRuta(punto)
    }

    fun obtenerPuntosDeRuta(rutaId: Long): Flow<List<PuntoRuta>> {
        return dao.obtenerPuntosDeRuta(rutaId)
    }

    suspend fun insertarWaypoint(waypoint: Waypoint) {
        dao.insertarWaypoint(waypoint)
    }

    fun obtenerWaypointsDeRuta(rutaId: Long): Flow<List<Waypoint>> {
        return dao.obtenerWaypointsDeRuta(rutaId)
    }
}