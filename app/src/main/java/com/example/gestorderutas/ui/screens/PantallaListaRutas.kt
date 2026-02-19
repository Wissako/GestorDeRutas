package com.example.gestorderutas.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gestorderutas.viewmodel.UbicacionViewModel
import java.util.concurrent.TimeUnit

@Composable
fun PantallaListaRutas(
    viewModel: UbicacionViewModel,
    onRutaClick: (Long) -> Unit,
    onVolver: () -> Unit
) {
    val rutas by viewModel.todasLasRutas.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Fila superior con botón de volver y título
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver al mapa"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mis Rutas Guardadas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Lista de rutas
        LazyColumn {
            items(rutas) { ruta ->
                CardRuta(ruta = ruta, onClick = { onRutaClick(ruta.id) })
            }
        }
    }
}

@Composable
fun CardRuta(ruta: com.example.gestorderutas.model.Ruta, onClick: () -> Unit) {
    // Formatear duración de milisegundos a MM:SS
    val tiempoFormateado = String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(ruta.duracion),
        TimeUnit.MILLISECONDS.toSeconds(ruta.duracion) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ruta.duracion))
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Mostrar nombre, distancia y duración
            Text(
                text = ruta.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = "Distancia: ${String.format("%.1f", ruta.distancia)} m")
            Text(text = "Duración: $tiempoFormateado")
        }
    }
}