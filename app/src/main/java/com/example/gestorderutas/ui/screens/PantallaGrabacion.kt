package com.example.gestorderutas.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestorderutas.ui.components.ControlesRuta
import com.example.gestorderutas.ui.components.DialogoWaypoint
import com.example.gestorderutas.ui.components.MapaOSM
import com.example.gestorderutas.ui.components.PanelMetricas
import com.example.gestorderutas.viewmodel.UbicacionViewModel
import java.util.concurrent.TimeUnit

@Composable
fun PantallaGrabacion(
    viewModel: UbicacionViewModel,
    onVerLista: () -> Unit // Cambiamos el nombre para reflejar la navegación al historial
) {
    val isRecording = viewModel.isRecording
    val tiempoTranscurrido by viewModel.tiempoTranscurrido.collectAsState()
    val distanciaAcumulada by viewModel.distanciaAcumulada.collectAsState()
    val ubicacionActual by viewModel.ubicacionActual.collectAsState()
    val puntosRuta by viewModel.puntosRutaActual.collectAsState()
    val waypoints by viewModel.waypointsActuales.collectAsState()
    var mostrarDialogoWaypoint by remember { mutableStateOf(false) }

    val tiempoFormateado = remember(tiempoTranscurrido) {
        String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(tiempoTranscurrido),
            TimeUnit.MILLISECONDS.toSeconds(tiempoTranscurrido) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tiempoTranscurrido))
        )
    }

    val velocidadMedia = remember(distanciaAcumulada, tiempoTranscurrido) {
        if (tiempoTranscurrido > 0) {
            (distanciaAcumulada / 1000.0) / (tiempoTranscurrido / 3600000.0) // km/h [cite: 29, 117]
        } else 0.0
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MapaOSM(
            modifier = Modifier.fillMaxSize(),
            puntosRuta = puntosRuta,
            waypoints = waypoints,
            ubicacionActual = ubicacionActual
        )

        // Botón para ver rutas guardadas (Historial)
        if (!isRecording) {
            IconButton(
                onClick = onVerLista,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Ver rutas",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (isRecording || puntosRuta.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp)
            ) {
                PanelMetricas(
                    distancia = distanciaAcumulada,
                    tiempoFormateado = tiempoFormateado,
                    velocidadMedia = velocidadMedia
                )
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)) {
            ControlesRuta(
                isRecording = isRecording,
                onStartClick = { viewModel.iniciarNuevaRuta() },
                onStopClick = { viewModel.finalizarRutaActual() }, // Llamamos a la lógica de cierre [cite: 31]
                onAddWaypointClick = { mostrarDialogoWaypoint = true }
            )
        }

        if (mostrarDialogoWaypoint) {
            DialogoWaypoint(
                onDismiss = { mostrarDialogoWaypoint = false },
                onConfirm = { desc ->
                    viewModel.guardarWaypoint(desc)
                    mostrarDialogoWaypoint = false
                }
            )
        }
    }
}