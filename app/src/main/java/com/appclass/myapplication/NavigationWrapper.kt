package com.example.vistasclientes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.screens.ButtonAddForms
import com.appclass.myapplication.screens.CalendarioApp
import com.appclass.myapplication.screens.Formulario
/*
import com.appclass.myapplication.screens.InicioAppCRM
import com.appclass.myapplication.screens.LoginUsuario

import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.screens.CalendarioApp
import com.appclass.myapplication.screens.InicioAppCRM
import com.appclass.myapplication.screens.PantallaInicio
//import com.appclass.pruebasautentificacion.screens.LoginUsuario
import com.appclass.myapplication.screens.RegistroUsuario
import com.appclass.myapplication.screens.LoginUsuario*/
import com.appclass.myapplication.screens.PantallaGraficos/*
import com.appclass.myapplication.screens.PantallaInicio*/
import com.appclass.myapplication.screens.Questionario
import com.appclass.myapplication.screens.RegistroUsuario
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.util.UUID


//import com.appclass.pruebasautentificacion.screens.RegistroUsuario


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(navHostController: NavHostController) {


    NavHost(navController = navHostController, startDestination = "pantallaGraficos") {

/*
        composable("pantallaInicio") { PantallaInicio(navHostController) }

        composable("home") { BottomNavigationBarComponent(navHostController) }
        composable("message") { PantallaInicio(navHostController) }
        composable("formulario") { BottomNavigationBarComponent(navHostController) }

        composable("inicioAppCRM"){ InicioAppCRM (navHostController)}
        composable("registroUsuario"){ RegistroUsuario (navHostController) }
        composable("loginUsuario"){ LoginUsuario (navHostController) }

        composable("calendarApp"){ CalendarioApp (navHostController) }

*/
        composable("PantallaFormulario"){ Questionario (navHostController) }
        composable("pantallaGraficos") { PantallaGraficos(navHostController) }
        //para añadir los 29 días antes creo
        composable("buttonAddForms") { ButtonAddForms(navHostController) }
}}
