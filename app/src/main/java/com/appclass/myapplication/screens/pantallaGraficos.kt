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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
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
import com.github.mikephil.charting.utils.ColorTemplate
import android.graphics.Color as AndroidColor

import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.navigation.NavController

//import com.jjoe64.graphview.GraphView
//import com.jjoe64.graphview.series.DataPoint
//import com.jjoe64.graphview.series.LineGraphSeries

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaGraficos(navController: NavHostController) {
    var data by remember { mutableStateOf<List<Formulario>>(emptyList()) }  // Estado para los datos de los últimos 30 días
    var data2 by remember { mutableStateOf<List<Formulario>>(emptyList()) }  // Estado para los datos de los últimos 7 días
    val coroutineScope = rememberCoroutineScope()

    // Llamar a las funciones suspendidas cuando la pantalla se inicializa
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            data = getDataFromFirestore()  // Llamada suspendida para obtener los datos de los últimos 30 días
            data2 = getDataFromFirestore2()  // Llamada suspendida para obtener los datos de los últimos 7 días
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pantalla de Gráficos") }
            )
        },
        bottomBar = { }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Pasamos data y data2 a PaginaGraficos
            Spacer(modifier = Modifier.size(30.dp))
            PaginaGraficos(navController = navController, data = data, data2 = data2)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaginaGraficos(navController: NavController, data: List<Formulario>, data2: List<Formulario>) {
    // Mostrar los datos de los últimos 30 días y 7 días
    Text(text = "Como te has sentido los últimos días", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(16.dp))

    // Mostrar el gráfico de barras con los datos de los últimos 7 días
    if (data2.isNotEmpty()) {
        Text(text = "Nota global de la última semana", style = MaterialTheme.typography.bodyMedium)
        DisplayMoodBarChart(data2 = data2)  // Usamos los datos de los últimos 7 días
    }
    // Mostrar el gráfico circular con los datos de los últimos 7 días
    if (data2.isNotEmpty()) {
        Text(text = "Como has dormido la última semana", style = MaterialTheme.typography.bodyMedium)
        DisplaySleepQualityPieChart(data2 = data2)  // Usamos los datos de los últimos 7 días
    }

    // Mostrar el calendario con los datos de los últimos 30 días
    if (data.isNotEmpty()) {
        Text(text = "Estado de ánimo el último mes", style = MaterialTheme.typography.bodyMedium)
        Last30DaysCalendar(data = data)  // Usamos los datos de los últimos 30 días
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

// Función para obtener los datos de Firestore (de la subcolección "formularios" del usuario)
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
            // Hacer una consulta en la subcolección "formulariosDiarios"
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

// Función para obtener los datos de Firestore para los últimos 7 días
@RequiresApi(Build.VERSION_CODES.O)
suspend fun getDataFromFirestore2(): List<Formulario> {
    val db = FirebaseFirestore.getInstance()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val formDataList = mutableListOf<Formulario>()

    // Verificar si el usuario está logueado
    if (currentUser != null) {
        val userId = currentUser.uid
        val today = LocalDate.now()
        val last7Days = (0 until 7).map { today.minusDays(it.toLong()) }  // Últimos 7 días
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        try {
            // Hacer una consulta en la subcolección "formulariosDiarios"
            val querySnapshot = db.collection("usuariosCRM") // Colección de usuarios
                .document(userId) // Documento del usuario logueado
                .collection("formulariosDiarios") // Subcolección de formularios del usuario
                .whereIn("fecha", last7Days.map { it.format(formatter) }) // Filtrar por los últimos 7 días
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayMoodBarChart(data2: List<Formulario>) {
    // Convertir los datos a las entradas necesarias para el gráfico de barras
    val entries = data2.mapIndexed { index, formulario ->
        BarEntry((data2.size - 1 - index).toFloat(), formulario.notaGlobal.toFloat()) // Usamos notaGlobal aquí
    }

    // Calcular los días de la semana para los últimos 7 días (incluyendo hoy)
    val today = LocalDate.now()
    val last7Days = (0 until 7).map { today.minusDays(it.toLong()) }.reversed() // Revertir el orden
    val dayNames = last7Days.map { date ->
        // Obtener el nombre del día de la semana
        date.dayOfWeek.name.take(3)  // Toma los primeros 3 caracteres (abreviatura)
    }

    // Mostrar el gráfico de barras usando AndroidView
    AndroidView(
        factory = { context ->
            // Inicializar el BarChart
            BarChart(context).apply {
                // Configuración básica del gráfico de barras
                val dataSet = BarDataSet(entries, "Nota Global")
                dataSet.color = ColorTemplate.MATERIAL_COLORS[0] // Color de las barras
                dataSet.valueTextColor = Color.Black.toArgb() // Color del texto

                // Configuración de datos
                val barData = BarData(dataSet)
                this.data = barData
                Log.e("Que sale en data", "$data2")

                // Configuración de los márgenes y desplazamientos
                setViewPortOffsets(50f, 50f, 50f, 50f) // Ajusta los márgenes según sea necesario

                // Usar un ValueFormatter para las etiquetas en el eje X (días de la semana)
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        // Usar la lista `dayNames` para las etiquetas
                        return dayNames.getOrElse(value.toInt()) { "" }
                    }
                }

                // Configuración de los ejes
                xAxis.position = XAxis.XAxisPosition.BOTTOM  // Colocar las etiquetas en la parte inferior
                isHighlightPerTapEnabled = false // Opcional: Desactiva el resaltado al tocar

                // Desactivar las líneas de la cuadrícula para el eje X
                xAxis.setDrawGridLines(false)

                // Desactivar las líneas de la cuadrícula para el eje Y
                axisLeft.setDrawGridLines(false)
                axisRight.setDrawGridLines(false)

                // Desactivar la descripción del gráfico
                description.isEnabled = false

                // Desactivar la leyenda (cuadrado verde con "Nota Global")
                legend.isEnabled = false

                // Configurar el ValueFormatter para los valores en las barras
                dataSet.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        // Quitar los decimales (ej. 3.0 -> 3)
                        return value.toInt().toString()
                    }
                }

                // Redibujar el gráfico
                invalidate()
            }
        },
        modifier = Modifier
            .fillMaxWidth()  // Asegurarse de que ocupe todo el ancho
            .height(300.dp)  // Puedes ajustar el tamaño según tus necesidades
            .padding(16.dp)  // Opcional: Agregar padding para evitar que se recorte el gráfico
    )
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplaySleepQualityPieChart(data2: List<Formulario>) {
    // Contadores para cada calidad de sueño
    val sleepQualityCounts = mutableMapOf(
        "Muy mal" to 0,
        "Mal" to 0,
        "Regular" to 0,
        "Bien" to 0,
        "Muy bien" to 0
    )

    // Contar los valores de calidad de sueño
    data2.forEach { formulario ->
        when (formulario.calidadSueno) {
            "Muy mal" -> sleepQualityCounts["Muy mal"] = sleepQualityCounts["Muy mal"]?.plus(1) ?: 1
            "Mal" -> sleepQualityCounts["Mal"] = sleepQualityCounts["Mal"]?.plus(1) ?: 1
            "Regular" -> sleepQualityCounts["Regular"] = sleepQualityCounts["Regular"]?.plus(1) ?: 1
            "Bien" -> sleepQualityCounts["Bien"] = sleepQualityCounts["Bien"]?.plus(1) ?: 1
            "Muy bien" -> sleepQualityCounts["Muy bien"] = sleepQualityCounts["Muy bien"]?.plus(1) ?: 1
            "Mala" -> sleepQualityCounts["Mal"] = sleepQualityCounts["Mal"]?.plus(1) ?: 1 // "Mala" va a "Mal"
            "Buena" -> sleepQualityCounts["Bien"] = sleepQualityCounts["Bien"]?.plus(1) ?: 1 // "Buena" va a "Bien"
            "Excelente" -> sleepQualityCounts["Muy bien"] = sleepQualityCounts["Muy bien"]?.plus(1) ?: 1 // "Excelente" va a "Muy bien"
        }
    }

    // Crear las entradas para el gráfico circular
    val entries = listOf(
        PieEntry(sleepQualityCounts["Muy mal"]?.toFloat() ?: 0f, "Muy mal"),
        PieEntry(sleepQualityCounts["Mal"]?.toFloat() ?: 0f, "Mal"),
        PieEntry(sleepQualityCounts["Regular"]?.toFloat() ?: 0f, "Regular"),
        PieEntry(sleepQualityCounts["Bien"]?.toFloat() ?: 0f, "Bien"),
        PieEntry(sleepQualityCounts["Muy bien"]?.toFloat() ?: 0f, "Muy bien")
    )

    // Mostrar el gráfico circular usando AndroidView
    AndroidView(
        factory = { context ->
            // Inicializar el PieChart
            PieChart(context).apply {
                // Configuración básica del gráfico circular
                val dataSet = PieDataSet(entries, "")
                dataSet.colors = listOf(
                    0xFFFF0000.toInt(),  // Muy mal (Rojo)
                    0xFFFF6F00.toInt(),  // Mal (Naranja)
                    0xFFFFFF00.toInt(),  // Regular (Amarillo)
                    0xFF4CAF50.toInt(),  // Bien (Verde)
                    0xFF388E3C.toInt()   // Muy bien (Verde oscuro)
                )
                dataSet.valueTextColor = Color.Transparent.toArgb() // Eliminar los números dentro del gráfico

                // Configuración de los datos
                val pieData = PieData(dataSet)
                this.data = pieData
                // Habilitar o deshabilitar etiquetas de valores
                setDrawEntryLabels(false) // Eliminar los números dentro de las porciones

                // Configuración de los márgenes y desplazamientos
                setExtraOffsets(10f, 10f, 10f, 10f)

                // Desactivar la leyenda si no la necesitas
                legend.isEnabled = true // Habilitar la leyenda
                // Desactivar el texto de la descripción
                description.isEnabled = false

                // Configuración de la leyenda
                legend.form = Legend.LegendForm.SQUARE // Usar cuadrados en la leyenda
                legend.formSize = 10f // Tamaño del cuadro en la leyenda
                legend.textSize = 12f // Tamaño del texto en la leyenda

                // Redibujar el gráfico, dice a la pag d actualizarse para asegurar que se dibujan los datos nuevos
                invalidate()
            }
        },
        modifier = Modifier
            .fillMaxWidth()  // Asegurarse de que ocupe todo el ancho
            .height(300.dp)  // Ajusta el tamaño según tus necesidades
            .padding(16.dp)  // Opcional: Agregar padding para evitar que se recorte el gráfico
    )
}









/*
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
*/
/*
// Función para mostrar el gráfico de líneas usando GraphView
@Composable
fun GraphViewExample(data: List<Formulario>) {
    val context = LocalContext.current

    // Crear el objeto GraphView y configurarlo
    val graphView = remember { GraphView(context) }

    // Crear una serie de datos basada en la lista 'data' (supuesto que Formulario tiene fecha y estadoAnimo)
    val dataPoints = data.mapIndexed { index, formulario ->
        DataPoint(index.toDouble(), formulario.estadoAnimo.toDouble())
    }

    val series = LineGraphSeries(dataPoints.toTypedArray())

    // Agregar la serie al gráfico
    graphView.addSeries(series)

    // Personalizar el gráfico (ejes, títulos)
    graphView.gridLabelRenderer.horizontalAxisTitle = "Días"
    graphView.gridLabelRenderer.verticalAxisTitle = "Estado de ánimo"

    // Configurar el gráfico para que se ajuste bien al contenido
    graphView.viewport.isXAxisBoundsManual = true
    graphView.viewport.setMinX(0.0)
    graphView.viewport.setMaxX(data.size.toDouble())

    // Mostrar el gráfico en Compose
    AndroidView(
        factory = { graphView },
        modifier = Modifier.fillMaxWidth().height(300.dp)
    )
}*/