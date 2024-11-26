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
<<<<<<< HEAD
import com.appclass.myapplication.screens.InicioAppCRM
import com.appclass.myapplication.screens.LoginUsuario
/*
=======

>>>>>>> beatr2
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.screens.CalendarioApp
//import com.appclass.myapplication.screens.EntradaApp
import com.appclass.myapplication.screens.InicioAppCRM
import com.appclass.myapplication.screens.PantallaInicio
//import com.appclass.pruebasautentificacion.screens.LoginUsuario
import com.appclass.myapplication.screens.RegistroUsuario
import com.appclass.myapplication.screens.LoginUsuario
import com.appclass.myapplication.screens.PantallaGraficos
<<<<<<< HEAD
import com.appclass.myapplication.screens.PantallaInicio
=======
import com.appclass.myapplication.screens.PantallaPerfil
>>>>>>> beatr2
import com.appclass.myapplication.screens.Questionario
import com.appclass.myapplication.screens.RegistroUsuario
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.util.UUID


//import com.appclass.pruebasautentificacion.screens.RegistroUsuario


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(navHostController: NavHostController) {

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
    NavHost(navController = navHostController, startDestination = "calendarApp") {
=======
    NavHost(navController = navHostController, startDestination = "inicioAppCRM") {
>>>>>>> beatr2
=======
    NavHost(navController = navHostController, startDestination = "pantallaInicio") {
>>>>>>> beatr2
=======
    NavHost(navController = navHostController, startDestination = "inicioAppCRM") {
>>>>>>> beatr2

        composable("pantallaInicio") { PantallaInicio(navHostController) }


        //BARRA DE NAVEGACION -- COMPONENTE
        composable("home") { PantallaInicio(navHostController) }
        composable("citas") { CalendarioApp(navHostController) }
        composable("formulario") { Questionario(navHostController) }
        composable("perfil"){ PantallaPerfil(navHostController) }

        composable("inicioAppCRM"){ InicioAppCRM (navHostController)}
        composable("registroUsuario"){ RegistroUsuario (navHostController) }
        composable("loginUsuario"){ LoginUsuario (navHostController) }

        composable("calendarApp"){ CalendarioApp (navHostController) }

        composable("PantallaFormulario"){ Questionario (navHostController) }
        composable("pantallaGraficos") { PantallaGraficos(navHostController) }
        //para añadir los 29 días antes creo
        composable("buttonAddForms") { ButtonAddForms(navHostController) }

        composable("pantallaPerfil"){ PantallaPerfil(navHostController) }
}}
