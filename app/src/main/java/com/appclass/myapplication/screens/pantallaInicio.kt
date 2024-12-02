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

    // Estado para guardar los datos del usuario
    val (datosUser, setdatosUser) = remember { mutableStateOf<Map<String, Any>?>(null) }
    val (error, setError) = remember { mutableStateOf<String?>(null) }

    //solo ejecuta la consulta si el usuario está autenticado
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            db.collection("usuariosCRM").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        setdatosUser(document.data) // Guardamos los datos en el estado
                    } else {
                        setError("No se encontraron datos para el usuario.")
                    }
                }
                .addOnFailureListener { exception ->
                    setError("Error al obtener los datos: ${exception.message}")
                }
        }
    }




    // Lista de frases motivadoras
    val frasesMotivadoras = listOf(
        "¡Hoy es un buen día para empezar de nuevo!",
        "La felicidad depende de ti mismo.",
        "No te detengas hasta estar orgulloso.",
        "Cada paso te acerca a tu meta.",
        "Cree en ti mismo y todo será posible."
    )

    // Establecemos la frase motivadora actual (aleatoria)
    var fraseActual by remember { mutableStateOf(frasesMotivadoras.random()) }

    // Estado para las citas del mes
    var citasDelMes by remember { mutableStateOf<List<String>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

// Obtenemos las citas del Firestore
    LaunchedEffect(Unit) {
        // Referenciamos a la base de datos de Firebase
        val db = FirebaseFirestore.getInstance()

        // Obtenemos el UID del usuario actual
        val uidUsuarioActual = FirebaseAuth.getInstance().currentUser?.uid

        // Comprobamos si el UID es nulo (usuario no autenticado)
        if (uidUsuarioActual != null) {
            // Obtención de los documentos de la colección "citas", filtrados por el usuario
            val citas = db.collection("citas")
                .whereEqualTo("usuario", uidUsuarioActual) // Filtramos por el campo "usuario"
                .get() // Llamada asíncrona para obtener las citas
                .await() // Esperamos a que la operación termine

            // Asignamos los datos de las citas en la lista
            citasDelMes = citas.documents.map {
                val dia = it.getString("dia") ?: "Sin día"  // Obtenemos el día de la cita
                val hora = it.getString("hora") ?: "Sin hora"  // Obtenemos la hora de la cita
                "Dia: $dia - Hora: $hora"  // Combinamos día y hora en un solo texto
            }
        } else {
            // Si el UID es nulo, significa que no hay usuario autenticado
            citasDelMes = emptyList()
        }

        // Al finalizar, cambiamos el estado de loading a false
        loading = false
    }


    // Lista de recomendaciones del psicólogo (fijas)
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
        // Contenedor principal con padding
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp) // Margen general para el contenido
        ) {
            // Box para la frase motivadora
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .background(Color.LightGray.copy(alpha = 0.1f), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                // Texto de la frase motivadora, centrado
                Text(
                    text = fraseActual,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Título de citas
            Text(
                text = "Citas del mes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Mostrar carga hasta que las citas estén disponibles
            if (loading) {
                // Indicador de carga centrado
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                // Mostrar lista de citas del mes
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(citasDelMes) { cita ->
                        // Cada elemento de la lista es una tarjeta
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            // Mostrar el texto de cada cita (día y hora)
                            Text(
                                text = cita,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Espacio entre secciones

            // Título para el Too do List de recomendaciones
            Text(
                text = "Recomendaciones del psicólogo",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de recomendaciones del psicólogo
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(recomendacionesPsicologo) { recomendacion ->
                    // Cada recomendación es una fila con un botón de check
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        // Botón de check para marcar tarea completada
                        IconButton(onClick = {

                        }) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Marcar tarea como completada",
                                tint = Color.Green
                            )
                        }

                        // Texto de la recomendación
                        Text(
                            text = recomendacion,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f) // Aseguramos que el texto ocupe el espacio disponible
                        )
                    }
                }
            }
        }
    }

}