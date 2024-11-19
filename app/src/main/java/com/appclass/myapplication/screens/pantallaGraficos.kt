package com.appclass.myapplication.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaGraficos(navHostController: NavController) {
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
            // Usamos un Row para colocar el título y el formulario completado en una misma fila
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp), // Padding para el top y los lados
                horizontalArrangement = Arrangement.SpaceBetween, // Espacio entre los elementos
                verticalAlignment = Alignment.CenterVertically // Alineación vertical en el centro
            ) {

                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                // Título "Estadísticas recientes" a la izquierda
                Text(
                    text = "Estadísticas recientes",
                    style = MaterialTheme.typography.titleLarge

                )

                // Texto "Formulario completado" con el checkmark a la derecha
                Row(
                    verticalAlignment = Alignment.CenterVertically // Alineación vertical de los elementos
                ) {
                    // Texto de formulario completado
                    Text(
                        text = "Formulario completado",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        modifier = Modifier.padding(start = 30.dp)
                    )
                    // Icono de checkmark
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Formulario completado",
                        tint = Color.Green,
                        modifier = Modifier.padding(start = 2.dp) // Espacio entre el texto y el ícono
                    )
                }
            }
            }
            }


}
    ) { padding ->
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(60.dp))
            Text(text = "Como te has sentido los últimos días", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar el calendario
            Last30DaysCalendar(data = data)
            //contar dias buenos
            DiasSobresalientes(data=data)
            TempEstaSemana(data=data)
            HorasDedicadasA(data=data)
            Spacer(modifier = Modifier.height(16.dp))
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
        modifier = Modifier.fillMaxWidth(),
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

// Función para obtener los datos de Firestore
@RequiresApi(Build.VERSION_CODES.O)
suspend fun getDataFromFirestore(): List<Formulario> {
    val db = FirebaseFirestore.getInstance()
    val today = LocalDate.now()
    val last30Days = (0 until 28).map { today.minusDays(it.toLong()) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val formDataList = mutableListOf<Formulario>()

    try {
        // Hacer una consulta en la colección "formularioDiario" para obtener los formularios de los últimos 28 días
        val querySnapshot = db.collection("formularioDiario")
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DiasSobresalientes(data: List<Formulario>) {
    // Filtrar los formularios con notaGlobal >= 8
    val diasSobresalientes = data.filter { it.notaGlobal >= 8 }.size

    // Mostrar el resultado
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Total de días sobresalientes: $diasSobresalientes",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TempEstaSemana(data: List<Formulario>) {
    // Crear un mapa para contar las ocurrencias de cada tipo de clima
    val climaCount = mutableMapOf(
        "soleado" to 0,
        "nublado" to 0,
        "nevado" to 0,
        "lluvioso" to 0,
        "tormentoso" to 0,
        "niebla" to 0
    )

    // Recorrer los últimos 7 días y contar las ocurrencias
    val today = LocalDate.now()
    val last7Days = (0 until 7).map { today.minusDays(it.toLong()) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    last7Days.forEach { date ->
        val formattedDate = date.format(formatter)
        val formData = data.find { it.fecha == formattedDate }
        val tiempoClima = formData?.tiempoClima?.lowercase() ?: ""
        if (tiempoClima in climaCount) {
            climaCount[tiempoClima] = climaCount[tiempoClima]!! + 1
        }
    }

    // Filtrar los tipos de clima que tienen un conteo mayor a 0
    val filteredClimaCount = climaCount.filter { it.value > 0 }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Clima de la última semana",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Ancho máximo de las barras
        val maxBarWidth = 180.dp
        val maxCount = filteredClimaCount.values.maxOrNull() ?: 1

        // Crear una barra horizontal para cada tipo de clima que tenga más de 0 ocurrencias
        filteredClimaCount.forEach { (tipoClima, count) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),  // Reducir el espacio entre filas
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mostrar el nombre del clima con un width fijo
                Text(
                    text = tipoClima.replaceFirstChar { it.uppercase() },
                    modifier = Modifier.width(80.dp),  // Espacio fijo para el texto
                    style = MaterialTheme.typography.bodySmall
                )

                // Mostrar la barra
                Box(
                    modifier = Modifier
                        .height(20.dp)  // Reducir la altura de la barra
                        .width((maxBarWidth * count / maxCount).coerceAtMost(maxBarWidth)) // Ajustar a maxBarWidth
                        .background(
                            color = getColorForClima(tipoClima),
                            shape = MaterialTheme.shapes.small
                        )
                )

                // Mostrar el conteo al lado de la barra
                Text(
                    text = "$count días",
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

// Función para asignar colores según el tipo de clima
fun getColorForClima(tipoClima: String): Color {
    return when (tipoClima) {
        "soleado" -> Color.Yellow
        "nublado" -> Color.Gray
        "nevado" -> Color.White
        "lluvioso" -> Color.Blue
        "tormentoso" -> Color.DarkGray
        "niebla" -> Color.LightGray
        else -> Color.Transparent
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HorasDedicadasA(data: List<Formulario>) {
    // Obtener los últimos 7 días
    val today = LocalDate.now()
    val last7Days = (0 until 7).map { today.minusDays(it.toLong()) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Creamos una lista para almacenar los valores de cada actividad
    val valoresActividades = mutableListOf<List<Int>>()

    // Llenamos la lista con los valores de cada actividad para cada uno de los últimos 7 días
    last7Days.forEach { date ->
        val formattedDate = date.format(formatter)
        val formData = data.find { it.fecha == formattedDate }
        if (formData != null) {
            valoresActividades.add(
                listOf(
                    formData.descanso,  // Descanso
                    formData.ejercicio, // Ejercicio
                    formData.social,    // Social
                    formData.hobbies    // Hobbies
                )
            )
        } else {
            // Si no hay datos para ese día, agregamos valores por defecto (0)
            valoresActividades.add(listOf(0, 0, 0, 0))
        }
    }

    // Definimos el máximo de 10 en el eje vertical
    val maxY = 10
    val maxX = 7

    // Establecer un tamaño máximo de la gráfica
    val maxChartWidth = 300.dp
    val pointSize = 8.dp

    // Diseño de la columna
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Horas dedicadas en los últimos 7 días",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Gráfico de puntos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Eje vertical con los valores del 0 a 10
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (0..maxY).reversed().forEach { yValue ->
                    Text(
                        text = "$yValue",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

            // Eje horizontal con los días
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // Mostrar los puntos correspondientes a cada actividad para los últimos 7 días
                valoresActividades.forEachIndexed { dayIndex, valores ->

                    // Mostrar el conjunto de puntos para ese día
                    Column(
                        modifier = Modifier
                            .width((maxChartWidth / maxX)) // Ajustar el ancho de cada día
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        valores.forEachIndexed { activityIndex, value ->
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .offset(y = ((maxY - value) * 20).dp) // Ajustamos la altura del punto
                                    .size(pointSize)
                                    .background(
                                        color = getColorForActividad(activityIndex),
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }
        }

        // Espacio entre la leyenda y los días
        Spacer(modifier = Modifier.height(16.dp))

        // Eje de fechas en el gráfico (debajo de cada columna de puntos)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Mostrar las fechas debajo de cada columna
            last7Days.forEach { date ->
                val formattedDate = date.format(formatter)
                Text(
                    text = formattedDate.substring(5),  // Mostrar solo mes y día
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .width((maxChartWidth / maxX))
                        .align(Alignment.CenterVertically)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Leyenda de los colores
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leyenda para descanso
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Gray, shape = CircleShape)
                )
                Text(
                    text = "Descanso",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            // Leyenda para ejercicio
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Green, shape = CircleShape)
                )
                Text(
                    text = "Ejercicio",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            // Leyenda para social
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Blue, shape = CircleShape)
                )
                Text(
                    text = "Social",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            // Leyenda para hobbies
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color.Magenta, shape = CircleShape)
                )
                Text(
                    text = "Hobbies",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

// Función para asignar colores a cada tipo de actividad
fun getColorForActividad(index: Int): Color {
    return when (index) {
        0 -> Color.Gray    // Descanso
        1 -> Color.Green   // Ejercicio
        2 -> Color.Blue    // Social
        else -> Color.Magenta // Hobbies
    }
}
