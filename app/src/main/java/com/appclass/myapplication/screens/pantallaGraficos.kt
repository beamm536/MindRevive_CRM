//package com.appclass.myapplication.screens
//
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import com.appclass.myapplication.componentes.BottomNavigationBarComponent
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun PantallaGraficos(navController: NavHostController) {
//    var data by remember { mutableStateOf<List<Formulario>>(emptyList()) }  // Estado para los datos
//    val coroutineScope = rememberCoroutineScope()
//
//    // Llamar a la función suspendida cuando la pantalla se inicializa
//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            data = getDataFromFirestore()  // Llamada suspendida
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Pantalla de Gráficos") },
//                /*navigationIcon = {
//                    IconButton(onClick = { navHostController.popBackStack() }) {
//                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
//                    }
//                }*/
//            )
//        },
//        bottomBar = { }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp)
//        ) {
//            Text(text = "Como te has sentido los últimos días", style = MaterialTheme.typography.titleMedium)
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Mostrar el calendario
//            Last30DaysCalendar(data = data)
//        }
//    }
//}
//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun Last30DaysCalendar(data: List<Formulario>) {
//    val today = LocalDate.now()
//    val last30Days = (0 until 32).map { today.minusDays(it.toLong()) }
//    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(8),
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp),  // Espacio entre filas
//        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre columnas
//    ) {
//        last30Days.forEach { date ->
//            val formattedDate = date.format(formatter)
//            val formData = data.find { it.fecha == formattedDate }
//            val color = getColorForEstadoAnimo(formData?.estadoAnimo ?: 0)
//            item {
//                Box(
//                    modifier = Modifier
//                        .size(30.dp)
//                        .background(color, shape = CircleShape)
//                        .padding(2.dp),
//                )
//            }
//        }
//    }
//}
//
//// Función para obtener los datos de Firestore (ahora de la subcolección del usuario)
//@RequiresApi(Build.VERSION_CODES.O)
//suspend fun getDataFromFirestore(): List<Formulario> {
//    val db = FirebaseFirestore.getInstance()
//    val currentUser = FirebaseAuth.getInstance().currentUser
//    val formDataList = mutableListOf<Formulario>()
//
//    // Verificar si el usuario está logueado
//    if (currentUser != null) {
//        val userId = currentUser.uid
//        val today = LocalDate.now()
//        val last30Days = (0 until 28).map { today.minusDays(it.toLong()) }
//        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//
//        try {
//            // Hacer una consulta en la subcolección "formularios" bajo el usuario logueado
//            val querySnapshot = db.collection("usuariosCRM") // Colección de usuarios
//                .document(userId) // Documento del usuario logueado
//                .collection("formulariosDiarios") // Subcolección de formularios del usuario
//                .whereIn("fecha", last30Days.map { it.format(formatter) })
//                .get()
//                .await()
//
//            // Filtrar los datos y convertirlos en objetos Formulario
//            for (document in querySnapshot.documents) {
//                val formulario = document.toObject(Formulario::class.java)
//                formulario?.let { formDataList.add(it) }
//            }
//
//        } catch (e: Exception) {
//            Log.e("Firestore", "Error al obtener datos", e)
//        }
//    }
//
//    return formDataList
//}
//
//// Función para asignar colores según estado de ánimo
//fun getColorForEstadoAnimo(estadoAnimo: Int): Color {
//    return when (estadoAnimo) {
//        1 -> Color.Red
//        2 -> Color(0xFFFF6F00)
//        3 -> Color.Yellow
//        4 -> Color.Green
//        5 -> Color(0xFF4CAF50)
//        else -> Color.LightGray // Días sin datos
//    }
//}

package com.appclass.myapplication.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import android.graphics.Color as AndroidColor
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaGraficos(navController: NavHostController) {
    var data by remember { mutableStateOf<List<Formulario>>(emptyList()) }  // Estado para los datos
    val coroutineScope = rememberCoroutineScope()

    // Llamar a la función suspendida cuando la pantalla se inicializa
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            data = getDataFromFirestore()  // Llamada suspendida
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pantalla de Gráficos") }
            )
        },
        bottomBar = { }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "Como te has sentido los últimos días", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar el calendario
            Last30DaysCalendar(data = data)

            Spacer(modifier = Modifier.height(32.dp))  // Espacio antes del gráfico

            // Mostrar gráfico de barras
            DisplayMoodBarChart(data = data)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Last30DaysCalendar(data: List<Formulario>) {
    val today = LocalDate.now()
    val last30Days = (0 until 32).map { today.minusDays(it.toLong()) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    LazyVerticalGrid(
        columns = GridCells.Fixed(8),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),  // Espacio entre filas
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre columnas
    ) {
        last30Days.forEach { date ->
            val formattedDate = date.format(formatter)
            val formData = data.find { it.fecha == formattedDate }
            val color = getColorForEstadoAnimo(formData?.estadoAnimo ?: 0)
            item {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(color, shape = CircleShape)
                        .padding(2.dp),
                )
            }
        }
    }
}

// Función para obtener los datos de Firestore (ahora de la subcolección del usuario)
@RequiresApi(Build.VERSION_CODES.O)
suspend fun getDataFromFirestore(): List<Formulario> {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val formDataList = mutableListOf<Formulario>()

    // Verificar si el usuario está logueado
    if (currentUser != null) {
        val userId = currentUser.uid
        val today = LocalDate.now()
        val last30Days = (0 until 28).map { today.minusDays(it.toLong()) }
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        try {
            // Hacer una consulta en la subcolección "formularios" bajo el usuario logueado
            val querySnapshot = db.collection("usuariosCRM") // Colección de usuarios
                .document(userId) // Documento del usuario logueado
                .collection("formulariosDiarios") // Subcolección de formularios del usuario
                .whereIn("fecha", last30Days.map { it.format(formatter) })
                .get()
                .await()

            // Filtrar los datos y convertirlos en objetos Formulario
            for (document in querySnapshot.documents) {
                val formulario = document.toObject(Formulario::class.java)
                formulario?.let { formDataList.add(it) }
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener datos", e)
        }
    }

    return formDataList
}

// Función para asignar colores según estado de ánimo
fun getColorForEstadoAnimo(estadoAnimo: Int): Color {
    return when (estadoAnimo) {
        1 -> Color.Red
        2 -> Color(0xFFFF6F00)
        3 -> Color.Yellow
        4 -> Color.Green
        5 -> Color(0xFF4CAF50)
        else -> Color.LightGray // Días sin datos
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayMoodBarChart(data: List<Formulario>) {
    val moods = mutableListOf<BarEntry>()
    val moodCount = IntArray(6)  // Array para contar los días de cada estado de ánimo (0-5)

    // Contar los estados de ánimo en los últimos 30 días
    data.forEach { formulario ->
        if (formulario.estadoAnimo in 1..5) {
            moodCount[formulario.estadoAnimo]++
        }
    }

    // Crear las entradas para el gráfico de barras
    moodCount.forEachIndexed { index, count ->
        if (count > 0) {
            moods.add(BarEntry(index.toFloat(), count.toFloat()))
        }
    }

    // Crear el gráfico de barras si hay datos para mostrar
    if (moods.isNotEmpty()) {
        AndroidView(
            factory = { context ->
                // Crear el BarChart
                val barChart = BarChart(context)
                val dataSet = BarDataSet(moods, "Estado de Ánimo")
                dataSet.color = AndroidColor.BLUE  // Color de las barras
                val barData = BarData(dataSet)
                barChart.data = barData
                barChart.description.isEnabled = false
                barChart.animateY(1000)  // Animación para las barras

                // Configurar el gráfico
                barChart.xAxis.apply {
                    isEnabled = true
                    granularity = 1f
                    valueFormatter = object : com.github.mikephil.charting.formatter.IndexAxisValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return when (value.toInt()) {
                                1 -> "Triste"
                                2 -> "Desanimado"
                                3 -> "Neutral"
                                4 -> "Feliz"
                                5 -> "Muy Feliz"
                                else -> "Sin Datos"
                            }
                        }
                    }
                }
                barChart.legend.isEnabled = true
                barChart.invalidate()
                barChart
            },
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )
    }
}
