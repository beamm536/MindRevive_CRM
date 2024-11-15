package com.example.vistasclientes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.screens.CalendarioApp
import com.appclass.myapplication.screens.InicioAppCRM
import com.appclass.myapplication.screens.PantallaInicio
//import com.appclass.pruebasautentificacion.screens.LoginUsuario
import com.appclass.myapplication.screens.RegistroUsuario
import com.appclass.myapplication.screens.LoginUsuario
import com.appclass.myapplication.screens.Questionario

//import com.appclass.pruebasautentificacion.screens.RegistroUsuario


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = "inicioAppCRM") {

        composable("pantallaInicio") { PantallaInicio(navHostController) }

        composable("home") { BottomNavigationBarComponent(navHostController) }
        composable("message") { PantallaInicio(navHostController) }
        composable("formulario") { BottomNavigationBarComponent(navHostController) }

        composable("inicioAppCRM"){ InicioAppCRM (navHostController)}
        composable("registroUsuario"){ RegistroUsuario (navHostController) }
        composable("loginUsuario"){ LoginUsuario (navHostController) }

        composable("calendarApp"){ CalendarioApp (navHostController) }


        composable("PantallaFormulario"){ Questionario (navHostController) }
    }
}
