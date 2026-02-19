package com.example.gestorderutas // Asegúrate de que este sea tu paquete principal

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestorderutas.data.AppDatabase
import com.example.gestorderutas.data.GestorRutasRepository
import com.example.gestorderutas.ui.screens.PantallaGrabacion
import com.example.gestorderutas.viewmodel.UbicacionViewModel
import com.google.android.gms.location.LocationServices
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestorderutas.ui.screens.PantallaListaRutas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = GestorRutasRepository(database.gestorRutasDao())
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UbicacionViewModel(application, repository, fusedLocationClient) as T
            }
        }

        setContent {
            val navController = rememberNavController()
            val viewModel: UbicacionViewModel = viewModel(factory = factory)

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: UbicacionViewModel = viewModel(factory = factory)

                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions()
                    ) { permissions ->
                        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                            viewModel.pedirUbicacionActual()
                        }
                    }

                    LaunchedEffect(Unit) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                    NavHost(navController = navController, startDestination = "grabacion") {
                        composable("grabacion") {
                            PantallaGrabacion(
                                viewModel = viewModel,
                                onVerLista = { navController.navigate("lista") }
                            )
                        }
                        composable("lista") {
                            PantallaListaRutas(
                                viewModel = viewModel,
                                onRutaClick = { rutaId ->
                                    //Cargamos los datos en el ViewModel
                                    viewModel.cargarRutaHistorica(rutaId)
                                    // Volvemos a la pantalla del mapa
                                    navController.popBackStack()
                                },
                                onVolver = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}