package com.appclass.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp



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

    val (datosUser, setdatosUser) = remember { mutableStateOf<Map<String, Any>?>(null) }
    val (error, setError) = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            db.collection("usuariosCRM").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        setdatosUser(document.data)
                    } else {
                        setError("No se encontraron datos para el usuario.")
                    }
                }
                .addOnFailureListener { exception ->
                    setError("Error al obtener los datos: ${exception.message}")
                }
        }
    }

    val frasesMotivadoras = listOf(
        "¡Hoy es un buen día para empezar de nuevo!",
        "La felicidad depende de ti mismo.",
        "No te detengas hasta estar orgulloso.",
        "Cada paso te acerca a tu meta.",
        "Cree en ti mismo y todo será posible."
    )

    var fraseActual by remember { mutableStateOf(frasesMotivadoras.random()) }

    var citasDelMes by remember { mutableStateOf<List<String>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val uidUsuarioActual = FirebaseAuth.getInstance().currentUser?.uid

        if (uidUsuarioActual != null) {
            val citas = db.collection("citas")
                .whereEqualTo("usuario", uidUsuarioActual)
                .get()
                .await()

            // Convertir y ordenar por día
            citasDelMes = citas.documents
                .mapNotNull {
                    val dia = it.getString("dia")?.toIntOrNull() // Convertir día a entero
                    val hora = it.getString("hora") ?: "Sin hora"
                    if (dia != null) dia to "Dia: $dia - Hora: $hora" else null
                }
                .sortedBy { it.first } // Ordenar por día
                .map { it.second } // Solo conservar el texto
        } else {
            citasDelMes = emptyList()
        }

        loading = false
    }


    val recomendacionesPsicologo = listOf(
        "Hacer ejercicios de relajación cada mañana",
        "Practicar mindfulness durante 10 minutos al día",
        "Escribir un diario de gratitud cada noche",
        "Realizar actividades físicas de manera regular",
        "Mantener una rutina de sueño consistente"
    )

    Scaffold(
        bottomBar = { BottomNavigationBarComponent(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .background(Color.LightGray.copy(alpha = 0.1f), shape = MaterialTheme.shapes.medium)
                        .padding(16.dp)
                ) {
                    Text(
                        text = fraseActual,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item {
                Text(
                    text = "Citas del mes",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            } else {
                items(citasDelMes) { cita ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = cita,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Recomendaciones del psicólogo",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(recomendacionesPsicologo) { recomendacion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Marcar tarea como completada",
                            tint = Color.Green
                        )
                    }

                    Text(
                        text = recomendacion,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }
}
