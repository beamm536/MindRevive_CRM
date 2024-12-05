package com.example.vistasclientes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appclass.myapplication.screens.* // Importación compacta de pantallas

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(navHostController: NavHostController) {

    // Configuración principal de navegación
    NavHost(navController = navHostController, startDestination = "inicioAppCRM") {

        // Rutas principales
        composable("inicioAppCRM") { InicioAppCRM(navHostController) }
        composable("registroUsuario") { RegistroUsuario(navHostController) }
        composable("loginUsuario") { LoginUsuario(navHostController) }

        // Rutas de navegación de la barra inferior
        composable("home") { PantallaInicio(navHostController) }
        composable("citas") { CalendarioPantalla(navHostController) }
        composable("formulario") { Questionario(navHostController) }
        composable("perfil") { PantallaPerfil(navHostController) }
        composable("graficos") { PantallaGraficos(navHostController) }

        // Otras rutas específicas
        composable("buttonAddFormsUser") { ButtonAddForms2(navHostController) }
        composable("pantallaInicio") { PantallaInicio(navHostController) }
        composable("PantallaFormulario") { Questionario(navHostController) }
        composable("pantallaGraficos") { PantallaGraficos(navHostController) }

        // Aquí se inicializa un segundo controlador de navegación para el calendario
        composable("calendar") {
            // Este controlador de navegación es específico para las pantallas de calendario
            val calendarNavController = rememberNavController()
            CalendarioPantalla(calendarNavController)
        }

        // Ruta dinámica para un día específico
        composable("day/{day}") { backStackEntry ->
            val dia = backStackEntry.arguments?.getString("day")?.toIntOrNull() ?: 1
            DiaCitas(dia)
        }
    }
}
