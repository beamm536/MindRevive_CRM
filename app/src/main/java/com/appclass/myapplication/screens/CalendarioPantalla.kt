package com.appclass.myapplication.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.ui.theme.Purple40
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarioPantalla(navHostController: NavHostController) {
    val mesActual = YearMonth.now()
    val diasMes = mesActual.lengthOfMonth()
    val db = FirebaseFirestore.getInstance()
    val coleccion = "citas"
    var citas by remember { mutableStateOf<List<Citas>>(emptyList()) } // Lista para almacenar las citas
    var diasConCitas by remember { mutableStateOf<Set<Int>>(emptySet()) }
    val uidUsuarioActual = FirebaseAuth.getInstance().currentUser?.uid

    // Carga los días con citas
    LaunchedEffect(mesActual) {
        if (uidUsuarioActual != null) {
            db.collection(coleccion)
                .whereGreaterThanOrEqualTo("dia", String.format("%02d", 1)) // Aseguramos que recuperamos citas desde el día 1
                .whereLessThanOrEqualTo("dia", String.format("%02d", diasMes)) // Hasta el último día del mes
                .get()
                .addOnSuccessListener { documents ->
                    // Filtramos las citas que pertenecen al usuario actual
                    val listaCitas = documents.mapNotNull { it.toObject(Citas::class.java) }
                    // diasConCitas = listaCitas.map { it.dia.toInt() }.toSet()
                    Log.d("CitasUsuario", listaCitas.toString())
                    // Filtramos las citas que pertenecen al usuario actual
                    val citasUsuario = listaCitas.filter { it.usuario == uidUsuarioActual }
                    citas = citasUsuario.sortedBy { it.dia.toInt() } // Ordenamos las citas por día                 // Guardamos los días que tienen citas del usuario actual
                    diasConCitas = citasUsuario.map { it.dia.toInt() }.toSet()
                    Log.d("CitasUsuario", citasUsuario.toString())
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Calendario de Citas",
                        fontSize = 24.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF6650a4)
                )
            )
        },
        bottomBar = { BottomNavigationBarComponent(navController = navHostController) }
    ) { paddingValues ->
        // Asegúrate de agregar el padding de Scaffold
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Text(
                text = "${mesActual.month} ${mesActual.year}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )

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

            // Recuperar todas las citas del mes
            LaunchedEffect(mesActual) {
                if (uidUsuarioActual != null) {
                    db.collection(coleccion)
                        .whereEqualTo("usuario", uidUsuarioActual)
                        .whereGreaterThanOrEqualTo("dia", String.format("%02d", 1)) // Formato consistente de día
                        .whereLessThanOrEqualTo("dia", String.format("%02d", diasMes)) // Formato consistente de día
                        .get()
                        .addOnSuccessListener { documents ->
                            // Convertimos los documentos de Firestore en objetos Citas
                            val listaCitas = documents.mapNotNull { doc ->
                                doc.toObject(Citas::class.java)
                            }

                            // Filtramos las citas que pertenecen al usuario actual
                            val citasUsuario = listaCitas.filter { it.usuario == uidUsuarioActual }

                            // Formatear el día a dos dígitos (asegura que siempre tenga dos dígitos)
                            citas = listaCitas.sortedBy { it.dia.toInt() }

                            // Guardamos los días que tienen citas del usuario actual
                            diasConCitas = listaCitas.map { it.dia.toInt() }.toSet()

                            Log.d("CitasUsuario", citasUsuario.toString())
                        }
                        .addOnFailureListener {
                            // Manejo de error si no se pueden cargar las citas
                        }
                }
            }

            Text(
                text = "Citas del Mes",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(start = 16.dp)
            )

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
                                    fontWeight = FontWeight.Bold,
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
}




//package com.appclass.myapplication.screens
//
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.background
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.Clear
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.appclass.myapplication.componentes.BottomNavigationBarComponent
//import com.appclass.myapplication.ui.theme.Purple40
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import java.time.LocalDate
//import java.time.YearMonth
//
//@OptIn(ExperimentalMaterial3Api::class)
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun CalendarioPantalla(navHostController: NavHostController) {
//    val mesActual = YearMonth.now()
//    val diasMes = mesActual.lengthOfMonth()
//    val db = FirebaseFirestore.getInstance()
//    val coleccion = "citas"
//    var citas by remember { mutableStateOf<List<Citas>>(emptyList()) } // Lista para almacenar las citas
//    var diasConCitas by remember { mutableStateOf<Set<Int>>(emptySet()) }
//    val uidUsuarioActual = FirebaseAuth.getInstance().currentUser?.uid
//
//    // Carga los días con citas
//    LaunchedEffect(mesActual) {
//        if (uidUsuarioActual != null) {
//            db.collection(coleccion)
//                .whereGreaterThanOrEqualTo("dia", String.format("%02d", 1)) // Aseguramos que recuperamos citas desde el día 1
//                .whereLessThanOrEqualTo("dia", String.format("%02d", diasMes)) // Hasta el último día del mes
//                .get()
//                .addOnSuccessListener { documents ->
//                    // Filtramos las citas que pertenecen al usuario actual
//                    val listaCitas = documents.mapNotNull { it.toObject(Citas::class.java) }
//                    diasConCitas = listaCitas.map { it.dia.toInt() }.toSet()
//                    Log.d("CitasUsuario", listaCitas.toString())
//                }
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Calendario de Citas",
//                        fontSize = 24.sp,
//                        color = Color.White,
//                        modifier = Modifier
//                            .padding(start = 90.dp)
//
//                    )
//                },
//                colors = TopAppBarDefaults.mediumTopAppBarColors(
//                    containerColor = Color(0xFF6650a4)
//                )
//            )
//        },
//        bottomBar = { BottomNavigationBarComponent(navController = navHostController) }
//    ) { paddingValues ->
//        // Asegúrate de agregar el padding de Scaffold
//        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
//            Text(
//                text = "${mesActual.month} ${mesActual.year}",
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier
//                    .padding(16.dp),
//                textAlign = TextAlign.Center,
//                fontWeight = FontWeight.Bold
//            )
//
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(7), // 7 columnas, una para cada día de la semana
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 8.dp)
//            ) {
//                listOf("L", "M", "X", "J", "V", "S", "D").forEach { diaSemana ->
//                    item {
//                        Text(
//                            text = diaSemana,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = Purple40,
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .fillMaxWidth(),
//                            textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.ExtraBold
//
//                        )
//                    }
//                }
//                for (dia in 1..diasMes) {
//                    item {
//                        // Para cada día, mostramos una celda que al hacer clic lleva a la pantalla del día
//                        DiaCasilla(dia = dia, diaActual = dia == LocalDate.now().dayOfMonth, tieneCita = diasConCitas.contains(dia)) {
//                            navHostController.navigate("day/$dia") // Navegar a la pantalla de detalles del día
//                        }
//                    }
//                }
//            }
//
//            // Recuperar todas las citas del mes
//            LaunchedEffect(mesActual) {
//                if (uidUsuarioActual != null) {
//                    db.collection(coleccion)
//                        .whereGreaterThanOrEqualTo("dia", String.format("%02d", 1)) // Formato consistente de día
//                        .whereLessThanOrEqualTo("dia", String.format("%02d", diasMes)) // Formato consistente de día
//                        .get()
//                        .addOnSuccessListener { documents ->
//                            // Convertimos los documentos de Firestore en objetos Citas
//                            val listaCitas = documents.mapNotNull { doc ->
//                                doc.toObject(Citas::class.java)
//                            }
//
//                            // Filtramos las citas que pertenecen al usuario actual
//                            val citasUsuario = listaCitas.filter { it.usuario == uidUsuarioActual }
//
//                            // Formatear el día a dos dígitos (asegura que siempre tenga dos dígitos)
//                            citas = listaCitas.sortedBy { it.dia.toInt() }
//
//                            // Guardamos los días que tienen citas del usuario actual
//                            diasConCitas = listaCitas.map { it.dia.toInt() }.toSet()
//
//                            Log.d("CitasUsuario", citasUsuario.toString())
//                        }
//                        .addOnFailureListener {
//                            // Manejo de error si no se pueden cargar las citas
//                        }
//                }
//            }
//
//            Text(
//                text = "Citas del Mes",
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier
//                    .padding(16.dp),
//                fontWeight = FontWeight.Bold
//            )
//
//            // Mostrar las citas ordenadas en LazyColumn para que sean desplazables
//            LazyColumn(modifier = Modifier.weight(1f)) { // Solo ocupa el espacio restante
//                items(citas) { cita ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Row(
//                                modifier = Modifier.fillMaxWidth(),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                // Texto de "Día"
//                                Text(
//                                    text = "Día: ${cita.dia}",
//                                    style = MaterialTheme.typography.bodyLarge,
//                                    modifier = Modifier.weight(1f) // Ocupa el espacio restante
//                                )
//
//                                // Icono alineado a la derecha
//                                // IconButton dentro del LazyColumn
//                                IconButton(onClick = {
//                                    if (cita.nombre.isNotBlank()) {
//                                        db.collection(coleccion)
//                                            .whereEqualTo("nombre", cita.nombre)
//                                            .get()
//                                            .addOnSuccessListener { documents ->
//                                                for (document in documents) {
//                                                    db.collection(coleccion)
//                                                        .document(document.id)
//                                                        .delete()
//                                                        .addOnSuccessListener {
//                                                            // Actualiza la lista de citas para eliminar la cita eliminada en Firestore
//                                                            citas = citas.filter { it.nombre != cita.nombre }
//                                                        }
//                                                }
//                                            }
//                                    }
//                                }) {
//                                    Icon(
//                                        imageVector = Icons.Rounded.Clear,
//                                        contentDescription = "Back",
//                                        tint = Color.Black
//                                    )
//                                }
//                            }
//
//                            // Otros textos en la columna
//                            Text(text = "Tipo de Cita: ${cita.nombre}", style = MaterialTheme.typography.bodyLarge)
//                            Text(text = "Médico: ${cita.medico}", style = MaterialTheme.typography.bodyLarge)
//                            Text(text = "Hora: ${cita.hora}", style = MaterialTheme.typography.bodyLarge)
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
