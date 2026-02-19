package com.example.gestorderutas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ControlesRuta(
    isRecording: Boolean,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onAddWaypointClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly // Reparte el espacio por igual
    ) {
        if (!isRecording) {
            // Botón Iniciar ruta
            ExtendedFloatingActionButton(
                onClick = onStartClick,
                icon = { Icon(Icons.Filled.PlayArrow, contentDescription = "Iniciar ruta") },
                text = { Text("Iniciar ruta") }
            )
        } else {
            // BotónDetener
            ExtendedFloatingActionButton(
                onClick = onStopClick,
                icon = { Icon(Icons.Filled.Stop, contentDescription = "Detener") },
                text = { Text("Detener") }
            )

            // Botón Añadir Waypoint (Solo visible al grabar)
            ExtendedFloatingActionButton(
                onClick = onAddWaypointClick,
                icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Añadir Waypoint") },
                text = { Text("Waypoint") }
            )
        }
    }
}