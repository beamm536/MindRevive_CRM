package com.appclass.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.appclass.myapplication.componentes.BottomNavigationBarComponent

@Composable
fun PantallaInicio(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavigationBarComponent(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Contenido de la pantalla PantallaInicio
            Text("Bienvenido a Pantalla Inicio")
        }
    }
}
