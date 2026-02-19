package com.example.gestorderutas.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun DialogoWaypoint(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Waypoint") },
        text = {
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción del lugar") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(descripcion) },
                enabled = descripcion.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}