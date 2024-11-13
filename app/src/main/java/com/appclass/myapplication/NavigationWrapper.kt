package com.example.vistasclientes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.screens.PantallaInicio
import com.appclass.myapplication.screens.RegistroUsuario
import com.appclass.myapplication.screens.LoginUsuario


@Composable
fun NavigationWrapper(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = "pantallaInicio") {
        composable("pantallaInicio") { PantallaInicio(navHostController) }

        composable("home") { BottomNavigationBarComponent(navHostController) }
        composable("message") { PantallaInicio(navHostController) }
        composable("formulario") { BottomNavigationBarComponent(navHostController) }
        
        composable("registroUsuario"){ RegistroUsuario (navHostController) }
        composable("loginUsuario"){ LoginUsuario (navHostController) }
    }
}