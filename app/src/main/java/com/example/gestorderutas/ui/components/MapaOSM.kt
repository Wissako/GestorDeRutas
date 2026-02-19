package com.example.gestorderutas.ui.components

import android.content.Context
import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gestorderutas.model.PuntoRuta
import com.example.gestorderutas.model.Ubicacion
import com.example.gestorderutas.model.Waypoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun MapaOSM(
    modifier: Modifier = Modifier,
    puntosRuta: List<PuntoRuta>,
    waypoints: List<Waypoint>,
    ubicacionActual: Ubicacion?
) {
    // AndroidView permite usar vistas clásicas como MapView de OSMDroid en Compose
    AndroidView(
        modifier = modifier,
        factory = { context ->
            //Configuración obligatoria de OSMDroid, identifica tu app ante los servidores de mapas
            Configuration.getInstance().load(context, context.getSharedPreferences("osm_pref", Context.MODE_PRIVATE))
            Configuration.getInstance().userAgentValue = "mi_app_de_rutas_123"
            //Crear y configurar el mapa base
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK) // El estilo visual del mapa
                setMultiTouchControls(true) // Permitir hacer zoom con dos dedos
                controller.setZoom(18.0) // Nivel de zoom
            }
        },
        update = { mapView ->

            // Limpiamos
            mapView.overlays.clear()

            //Dibujar la línea de la ruta
            if (puntosRuta.isNotEmpty()) {
                val polyline = Polyline()
                //Convertimos nuestra lista de PuntoRuta a GeoPoints de OSMDroid
                polyline.setPoints(puntosRuta.map { GeoPoint(it.lat, it.lng) })
                polyline.outlinePaint.color = Color.BLUE
                polyline.outlinePaint.strokeWidth = 10f // Grosor de la línea
                mapView.overlays.add(polyline)
            }

            //Dibujar los Waypoints
            waypoints.forEach { wp ->
                val marker = Marker(mapView)
                marker.position = GeoPoint(wp.lat, wp.lng)
                marker.title = wp.descripcion
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(marker)
            }

            //Centrar la cámara en la ubicación actual del GPS
            ubicacionActual?.let { ubi ->
                val geoPoint = GeoPoint(ubi.lat, ubi.lng)
                mapView.controller.animateTo(geoPoint)
            }

            //Obligamos al mapa a redibujarse con los nuevos datos
            mapView.invalidate()
        }
    )
}