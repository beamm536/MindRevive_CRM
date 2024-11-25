package com.appclass.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/*@Composable
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
}*/

@Composable
fun PantallaInicio(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    // Estado para guardar los datos del usuario
    val (userData, setUserData) = remember { mutableStateOf<Map<String, Any>?>(null) }
    val (error, setError) = remember { mutableStateOf<String?>(null) }

    // Solo ejecuta la consulta si el usuario está autenticado
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            db.collection("usuariosCRM").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        setUserData(document.data) // Guardamos los datos en el estado
                    } else {
                        setError("No se encontraron datos para el usuario.")
                    }
                }
                .addOnFailureListener { exception ->
                    setError("Error al obtener los datos: ${exception.message}")
                }
        }
    }

    Scaffold(
        bottomBar = { BottomNavigationBarComponent(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (userData != null) {
                // Mostrar los datos del usuario
                Text("Bienvenido, ${userData["nombre"] ?: "Usuario"}", fontSize = 24.sp)
                Text("Email: ${userData["email"] ?: "No disponible"}", fontSize = 16.sp)
                Text("Edad: ${userData["edad"] ?: "No especificada"}", fontSize = 16.sp)
            } else if (error != null) {
                // Mostrar un mensaje de error si falla la consulta
                Text("Error: $error", color = Color.Red, fontSize = 16.sp)
            } else {
                // Mostrar un indicador de carga mientras se obtienen los datos
                CircularProgressIndicator()
            }
        }
    }
}