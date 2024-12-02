package com.appclass.myapplication.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.ui.theme.Purple40
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarioApp(navHostController: NavHostController) {
    val navController = rememberNavController() // Creamos un controlador de navegación
    Scaffold(
        topBar = {
            // Barra superior de la app (puedes personalizarla)
            TopAppBar(
                title = { Text("Calendario de Citas") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("pantallaInicio")
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                }
            )

        },
        content = { paddingValues -> // Se pasa paddingValues a la columna
            Column(modifier = Modifier.padding(paddingValues)) {
                // Definimos las rutas de navegación. La ruta "calendar" lleva a la ventana del calendario,
                // y la ruta "day/{day}" nos lleva a la ventana de un día específico.
                NavHost(navController = navController, startDestination = "calendar") {
                    composable("calendar") { CalendarioPantalla(navController) } // Pantalla principal del calendario
                    composable("day/{day}") { backStackEntry ->
                        // Obtenemos el día de la para mostrar las citas correspondientes
                        val dia = backStackEntry.arguments?.getString("day")?.toInt() ?: 1
                        DiaCitas(dia) // Pantalla del día específico
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBarComponent(navController = navController)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarioPantalla(navHostController: NavHostController) {

    val mesActual = YearMonth.now() // Obtenemos el mes y el año actual
    val diasMes = mesActual.lengthOfMonth() // Número total de días del mes
    val db = FirebaseFirestore.getInstance() // Instancia de Firestore
    val coleccion = "citas" // Nombre de la colección de citas
    var citas by remember { mutableStateOf<List<Citas>>(emptyList()) } // Lista para almacenar las citas
    var diasConCitas by remember { mutableStateOf<Set<Int>>(emptySet()) } // Conjunto para almacenar los días con citas
    val uidUsuarioActual = FirebaseAuth.getInstance().currentUser?.uid

    // Recuperar todas las citas del mes
//    LaunchedEffect(mesActual) {
//        if (uidUsuarioActual != null) {
//            db.collection(coleccion)
//                .whereGreaterThanOrEqualTo("dia", "1") // Aseguramos que recuperamos citas desde el día 1
//                .whereLessThanOrEqualTo("dia", diasMes.toString()) // Hasta el último día del mes
//                .get()
//                .addOnSuccessListener { documents ->
//                    // Convertimos los documentos de Firestore en objetos Citas
//                    val listaCitas = documents.mapNotNull { doc ->
//                        doc.toObject(Citas::class.java)
//                    }
//                    // Filtramos las citas que pertenecen al usuario actual
//                    val citasUsuario = listaCitas.filter { it.usuario == uidUsuarioActual }
//                    citas = citasUsuario.sortedBy { it.dia.toInt() } // Ordenamos las citas por día
//                    // Guardamos los días que tienen citas del usuario actual
//                    diasConCitas = citasUsuario.map { it.dia.toInt() }.toSet()
//                    Log.d("CitasUsuario", citasUsuario.toString())
//                }
//                .addOnFailureListener {
//                    // Manejo de error si no se pueden cargar las citas
//                }
//        }
//    }

    // Recuperar todas las citas del mes
    LaunchedEffect(mesActual) {
        if (uidUsuarioActual != null) {
            db.collection(coleccion)
                .whereGreaterThanOrEqualTo("dia", "1") // Aseguramos que recuperamos citas desde el día 1
                .whereLessThanOrEqualTo("dia", diasMes.toString()) // Hasta el último día del mes
                .get()
                .addOnSuccessListener { documents ->
                    // Convertimos los documentos de Firestore en objetos Citas
                    val listaCitas = documents.mapNotNull { doc ->
                        doc.toObject(Citas::class.java)
                    }

                    // Filtramos las citas que pertenecen al usuario actual
                    val citasUsuario = listaCitas.filter { it.usuario == uidUsuarioActual }

                    // Formatear el día a dos dígitos (asegura que siempre tenga dos dígitos)
                    citas = citasUsuario.sortedBy { it.dia.padStart(2, '0').toInt() }

                    // Guardamos los días que tienen citas del usuario actual
                    diasConCitas = citasUsuario.map { it.dia.padStart(2, '0').toInt() }.toSet()
                    Log.d("CitasUsuario", citasUsuario.toString())
                }
                .addOnFailureListener {
                    // Manejo de error si no se pueden cargar las citas
                }
        }
    }



    Column(modifier = Modifier.fillMaxSize()) {
        // Título con el mes y el año actual
        Text(
            text = "${mesActual.month} ${mesActual.year}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Grid de días del mes. Usamos LazyVerticalGrid para mostrar los días en una cuadrícula.
        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // 7 columnas, una para cada día de la semana
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach { diaSemana ->
                item {
                    Text(
                        text = diaSemana,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Purple40,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold

                    )
                }
            }
            for (dia in 1..diasMes) {
                item {
                    // Para cada día, mostramos una celda que al hacer clic lleva a la pantalla del día
                    DiaCasilla(dia = dia, diaActual = dia == LocalDate.now().dayOfMonth, tieneCita = diasConCitas.contains(dia)) {
                        navHostController.navigate("day/$dia") // Navegar a la pantalla de detalles del día
                    }
                }
            }
        }

        Text(text = "Citas del Mes", style = MaterialTheme.typography.headlineMedium)

        // Mostrar las citas ordenadas en LazyColumn para que sean desplazables
        LazyColumn(modifier = Modifier.weight(1f)) { // Solo ocupa el espacio restante
            items(citas) { cita ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Texto de "Día"
                            Text(
                                text = "Día: ${cita.dia}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f) // Ocupa el espacio restante
                            )

                            // Icono alineado a la derecha
                            // IconButton dentro del LazyColumn
                            IconButton(onClick = {
                                if (cita.nombre.isNotBlank()) {
                                    db.collection(coleccion)
                                        .whereEqualTo("nombre", cita.nombre)
                                        .get()
                                        .addOnSuccessListener { documents ->
                                            for (document in documents) {
                                                db.collection(coleccion)
                                                    .document(document.id)
                                                    .delete()
                                                    .addOnSuccessListener {
                                                        // Actualiza la lista de citas para eliminar la cita eliminada en Firestore
                                                        citas = citas.filter { it.nombre != cita.nombre }
                                                    }
                                            }
                                        }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }
                        }

                        // Otros textos en la columna
                        Text(text = "Tipo de Cita: ${cita.nombre}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Médico: ${cita.medico}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Hora: ${cita.hora}", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

// Componente que representa una celda del día en el calendario.
@Composable
fun DiaCasilla(dia: Int, diaActual: Boolean, tieneCita: Boolean, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() } // Al hacer clic, se navega a la pantalla del día
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Caja que muestra el número del día. Se resalta si es el día actual.
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (diaActual) Color.Blue else Color.Transparent, // Resaltar el día actual
                    shape = CircleShape // Forma circular
                ),
            contentAlignment = Alignment.Center
        ) {
            // Texto con el número del día, blanco si es el día actual, negro si no lo es.
            Text(
                text = dia.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = if (diaActual) Color.White else Color.Black
            )
        }

        // Si el día tiene citas, se muestra un punto rojo debajo del número del día
        if (tieneCita) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.Red, CircleShape)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

// Pantalla que muestra las citas de un día específico, obteniendo los datos de Firestore.
@Composable
fun DiaCitas(dia: Int) {
    val db = FirebaseFirestore.getInstance()
    val coleccion = "citas"
    val auth = FirebaseAuth.getInstance()  // Instancia de FirebaseAuth para obtener el UID del usuario
    var nombre by remember { mutableStateOf("") }
    var medico by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var citas by remember { mutableStateOf<List<Citas>>(emptyList()) }
    var editada by remember { mutableStateOf(false) }
    var editandoCitaId by remember { mutableStateOf<String?>(null) }

    // Recuperar las citas de Firestore cuando el día cambia
    LaunchedEffect(dia) {
        cargarCitas(dia, db, coleccion) { listaCitas ->
            citas = listaCitas
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Día $dia", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de texto para ingresar los detalles de la cita
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Tipo de Cita") }
        )

        OutlinedTextField(
            value = medico,
            onValueChange = { medico = it },
            label = { Text("Nombre del Médico") }
        )

        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (HH:mm)") }
        )

        Spacer(modifier = Modifier.size(5.dp))

        // Botón para añadir o editar cita
        Button(
            onClick = {
                val horaNueva = try {
                    hora.split(":").let { parts ->
                        parts[0].toInt() * 60 + parts[1].toInt() // Convertimos hora y minutos a minutos totales
                    }
                } catch (e: Exception) {
                    mensaje = "Formato de hora no válido. Usa HH:mm"
                    return@Button
                }

                val conflicto = citas.any { cita ->
                    val horaExistente = cita.hora.split(":").let { parts ->
                        parts[0].toInt() * 60 + parts[1].toInt()
                    }
                    val diferencia = kotlin.math.abs(horaNueva - horaExistente)
                    diferencia < 60 // Conflicto si la diferencia es menor a una hora
                }

                if (conflicto) {
                    mensaje = "Conflicto de horarios: debe haber al menos una hora entre citas."
                    return@Button
                }

                val userUid = auth.currentUser?.uid // Obtén el UID del usuario actual

                if (userUid == null) {
                    mensaje = "Usuario no autenticado. No se puede guardar la cita."
                    return@Button
                }

                // Formatea el día con 2 dígitos
                //val diaFormateado = String.format("%02d", dia)  // Esto asegura que el día tenga dos dígitos
                // "%02d" es el formato que le decimos a String.format():
                //  - "%" indica el inicio de un especificador de formato.
                //  - "0" le dice que, si el número tiene menos de 2 dígitos, lo rellene con ceros a la izquierda.
                //  - "2" le indica que debe asegurarse de que el número tenga al menos 2 dígitos.
                //  - "d" significa que estamos tratando con un número entero (día en este caso).
                // Si el día es 1, el formato lo convertirá a "01". Si el día es 10, lo dejará como "10".


                // Datos que se enviarán a Firestore
                val datos = hashMapOf(
                    "nombre" to nombre,
                    "medico" to medico,
                    "dia" to dia.toString(),
                    "hora" to hora,
                    "usuario" to userUid  // Agregar el UID del usuario
                )

                if (editada && editandoCitaId != null) {
                    // Modo edición: actualizar cita existente
                    db.collection(coleccion)
                        .document(editandoCitaId!!)
                        .update(datos as Map<String, Any>)
                        .addOnSuccessListener {
                            mensaje = "Cita actualizada correctamente"
                            editada = false
                            editandoCitaId = null
                            nombre = ""
                            medico = ""
                            hora = ""
                            cargarCitas(dia, db, coleccion) { listaCitas ->
                                citas = listaCitas
                            }
                        }
                        .addOnFailureListener {
                            mensaje = "Error al actualizar la cita"
                        }
                } else {
                    // Modo agregar: crear nueva cita
                    db.collection(coleccion)
                        .document()
                        .set(datos)
                        .addOnSuccessListener {
                            mensaje = "Cita guardada correctamente"
                            nombre = ""
                            medico = ""
                            hora = ""
                            cargarCitas(dia, db, coleccion) { listaCitas ->
                                citas = listaCitas
                            }
                        }
                        .addOnFailureListener {
                            mensaje = "No se ha podido guardar la cita correctamente"
                        }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Text(text = if (editada) "Actualizar Cita" else "Añadir Cita")
        }

        Spacer(modifier = Modifier.size(8.dp))
        Text(text = mensaje)

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar citas del día
        citas.forEach { cita ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        editada = true
                        editandoCitaId = cita.id
                        nombre = cita.nombre
                        medico = cita.medico
                        hora = cita.hora
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Tipo de Cita: ${cita.nombre}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Médico: ${cita.medico}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Hora: ${cita.hora}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

// Función que carga las citas de un día específico desde Firestore
fun cargarCitas(dia: Int, db: FirebaseFirestore, coleccion: String, onCitasLoaded: (List<Citas>) -> Unit) {
    db.collection(coleccion)
        .whereEqualTo("dia", dia.toString()) // Cargar solo las citas de este día
        .get()
        .addOnSuccessListener { documents ->
            val citas = documents.mapNotNull { doc ->
                doc.toObject(Citas::class.java)
            }
            onCitasLoaded(citas)
        }
        .addOnFailureListener {
        }
}
