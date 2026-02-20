package com.example.gestorderutas.viewmodel

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestorderutas.data.GestorRutasRepository
import com.example.gestorderutas.model.PuntoRuta
import com.example.gestorderutas.model.Ruta
import com.example.gestorderutas.model.Ubicacion
import com.example.gestorderutas.model.Waypoint
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class UbicacionViewModel(
    application: Application,
    private val repository: GestorRutasRepository,
    private val fusedLocationClient: FusedLocationProviderClient
) : AndroidViewModel(application) {

    var isRecording = false
    private val _tiempoTranscurrido = MutableStateFlow(0L)
    val tiempoTranscurrido: StateFlow<Long> = _tiempoTranscurrido

    private val _distanciaAcumulada = MutableStateFlow(0.0)
    val distanciaAcumulada: StateFlow<Double> = _distanciaAcumulada

    private var recordingJob: Job? = null
    private var metricasJob: Job? = null
    private var ultimoPunto: PuntoRuta? = null

    private val _ubicacionActual = MutableStateFlow<Ubicacion?>(null)
    val ubicacionActual: StateFlow<Ubicacion?> = _ubicacionActual

    private val _puntosRutaActual = MutableStateFlow<List<PuntoRuta>>(emptyList())
    val puntosRutaActual: StateFlow<List<PuntoRuta>> = _puntosRutaActual

    var currentRutaId: Long? = null
    private val _waypointsActuales = MutableStateFlow<List<Waypoint>>(emptyList())
    val waypointsActuales: StateFlow<List<Waypoint>> = _waypointsActuales

    private val _todasLasRutas = MutableStateFlow<List<Ruta>>(emptyList())
    val todasLasRutas: StateFlow<List<Ruta>> = _todasLasRutas

    init {
        viewModelScope.launch {
            repository.obtenerTodasLasRutas().collect { rutas ->
                _todasLasRutas.value = rutas
            }
        }
    }

    fun iniciarGrabacionRuta(rutaId: Long) {
        if (isRecording) return
        isRecording = true
        ultimoPunto = null
        _tiempoTranscurrido.value = 0L
        _distanciaAcumulada.value = 0.0

        viewModelScope.launch {
            repository.obtenerPuntosDeRuta(rutaId).collect { puntos ->
                _puntosRutaActual.value = puntos
            }
        }

        metricasJob = viewModelScope.launch {
            while (isRecording) {
                delay(1000)
                _tiempoTranscurrido.value += 1000
            }
        }

        recordingJob = viewModelScope.launch {
            while (isRecording) {
                obtenerUbicacionActual { ubicacion ->
                    if (ubicacion != null) {
                        _ubicacionActual.value = ubicacion
                        val nuevoPunto = PuntoRuta(
                            rutaId = rutaId,
                            lat = ubicacion.lat,
                            lng = ubicacion.lng,
                            timestamp = System.currentTimeMillis()
                        )
                        guardarPuntoRuta(nuevoPunto)

                        ultimoPunto?.let { anterior ->
                            val dist = calcularDistancia(anterior, nuevoPunto)
                            _distanciaAcumulada.value += dist
                        }
                        ultimoPunto = nuevoPunto
                    }
                }
                delay(10_000)
            }
        }
    }

    fun detenerGrabacionRuta() {
        isRecording = false
        recordingJob?.cancel()
        metricasJob?.cancel()
    }

    override fun onCleared() {
        detenerGrabacionRuta()
        super.onCleared()
    }

    suspend fun crearRuta(ruta: Ruta): Long {
        return repository.insertarRuta(ruta)
    }

    private fun guardarPuntoRuta(punto: PuntoRuta) {
        viewModelScope.launch {
            repository.guardarPuntoRuta(punto)
        }
    }

    private fun obtenerUbicacionActual(callback: (Ubicacion?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    callback(Ubicacion(location.latitude, location.longitude))
                } else {
                    callback(null)
                }
            }.addOnFailureListener {
                callback(null)
            }
        } else {
            callback(null)
        }
    }

    private fun calcularDistancia(p1: PuntoRuta, p2: PuntoRuta): Double {
        val r = 6371000.0
        val lat1 = Math.toRadians(p1.lat)
        val lat2 = Math.toRadians(p2.lat)
        val deltaLat = Math.toRadians(p2.lat - p1.lat)
        val deltaLng = Math.toRadians(p2.lng - p1.lng)
        val a = sin(deltaLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(deltaLng / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    fun iniciarNuevaRuta() {
        viewModelScope.launch {
            val nuevaRuta = Ruta(nombre = "Ruta ${System.currentTimeMillis()}")
            val id = crearRuta(nuevaRuta)
            currentRutaId = id
            iniciarGrabacionRuta(id)

            launch {
                repository.obtenerWaypointsDeRuta(id).collect { waypoints ->
                    _waypointsActuales.value = waypoints
                }
            }
        }
    }

    fun guardarWaypoint(descripcion: String) {
        val rutaIdActual = currentRutaId ?: return
        val ubi = ubicacionActual.value ?: return
        viewModelScope.launch {
            val nuevoWaypoint = Waypoint(
                rutaId = rutaIdActual,
                lat = ubi.lat,
                lng = ubi.lng,
                descripcion = descripcion
            )
            repository.insertarWaypoint(nuevoWaypoint)
        }
    }

    fun pedirUbicacionActual() {
        obtenerUbicacionActual { ubicacion ->
            _ubicacionActual.value = ubicacion
        }
    }

    fun finalizarRutaActual() {
        val id = currentRutaId ?: return
        detenerGrabacionRuta()
        viewModelScope.launch {
            val distFinal = _distanciaAcumulada.value
            val tiempoFinal = _tiempoTranscurrido.value
            val velMedia = if (tiempoFinal > 0) (distFinal / 1000.0) / (tiempoFinal / 3600000.0) else 0.0
            val rutaFinalizada = Ruta(
                id = id,
                nombre = "Ruta ${System.currentTimeMillis()}",
                distancia = distFinal,
                duracion = tiempoFinal,
                velocidadMedia = velMedia
            )
            repository.actualizarRuta(rutaFinalizada)
            currentRutaId = null
        }
    }

    fun cargarRutaHistorica(rutaId: Long) {
        detenerGrabacionRuta()

        viewModelScope.launch {
            val ruta = repository.obtenerRutaPorId(rutaId)
            ruta?.let {
                _distanciaAcumulada.value = it.distancia
                _tiempoTranscurrido.value = it.duracion
            }
        }

        viewModelScope.launch {
            repository.obtenerPuntosDeRuta(rutaId).collect { puntos ->
                _puntosRutaActual.value = puntos
            }
        }

        viewModelScope.launch {
            repository.obtenerWaypointsDeRuta(rutaId).collect { waypoints ->
                _waypointsActuales.value = waypoints
            }
        }
    }

    fun limpiarMapa() {
        _puntosRutaActual.value = emptyList()
        _waypointsActuales.value = emptyList()
        _distanciaAcumulada.value = 0.0
        _tiempoTranscurrido.value = 0L
    }
}