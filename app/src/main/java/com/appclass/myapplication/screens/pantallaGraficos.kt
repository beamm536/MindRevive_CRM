package com.appclass.myapplication.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

// PantallaGraficos como parte de NavigationWrapper
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PantallaGraficos(navHostController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pantalla de Gráficos") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "Gráficos del mes", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Muestra el calendario con datos ficticios
            val exampleData = listOf(
                Formulario(
                    fecha = "2024-11-01",
                    estadoAnimo = 3,
                    motivacion = 4,
                    trabajo = 8,
                    descanso = 7,
                    ejercicio = 1,
                    social = 2,
                    hobbies = 1,
                    tiempoClima = "soleado",
                    logros = "Completé un proyecto importante",
                    cuidadoPersonal = "Meditación matutina",
                    emocionesPredominantes = listOf("felicidad", "tranquilidad"),
                    pensamientosNegativos = "Me preocupé por los plazos",
                    nivelAnsiedad = 2,
                    calidadSueno = "Buena",
                    agradecimientos = "Gracias por un buen día",
                    intensidadAutocritica = 3,
                    expectativaManana = "Completar tareas pendientes",
                    otrosComentarios = "Día productivo",
                    notaGlobal = 8
                ),
                Formulario(
                    fecha = "2024-11-02",
                    estadoAnimo = 1,
                    motivacion = 2,
                    trabajo = 5,
                    descanso = 6,
                    ejercicio = 0,
                    social = 0,
                    hobbies = 3,
                    tiempoClima = "lluvioso",
                    logros = "Leí un libro",
                    cuidadoPersonal = "Mascarilla facial",
                    emocionesPredominantes = listOf("aburrimiento"),
                    pensamientosNegativos = "Pensé que podría haber hecho más",
                    nivelAnsiedad = 3,
                    calidadSueno = "Regular",
                    agradecimientos = "Tiempo para descansar",
                    intensidadAutocritica = 4,
                    expectativaManana = "Estar más activo",
                    otrosComentarios = "Día tranquilo",
                    notaGlobal = 6
                ),
                Formulario(
                    fecha = "2024-11-03",
                    estadoAnimo = 5,
                    motivacion = 5,
                    trabajo = 0,
                    descanso = 8,
                    ejercicio = 2,
                    social = 3,
                    hobbies = 4,
                    tiempoClima = "nublado",
                    logros = "Pasé tiempo con mi familia",
                    cuidadoPersonal = "Baño relajante",
                    emocionesPredominantes = listOf("alegría", "gratitud"),
                    pensamientosNegativos = "Ninguno",
                    nivelAnsiedad = 1,
                    calidadSueno = "Excelente",
                    agradecimientos = "Tiempo con seres queridos",
                    intensidadAutocritica = 1,
                    expectativaManana = "Seguir con esta energía",
                    otrosComentarios = "Día maravilloso",
                    notaGlobal = 10
                ),
                Formulario(
                    fecha = "2024-11-04",
                    estadoAnimo = 2,
                    motivacion = 3,
                    trabajo = 6,
                    descanso = 6,
                    ejercicio = 1,
                    social = 1,
                    hobbies = 0,
                    tiempoClima = "soleado",
                    logros = "Terminé tareas de trabajo",
                    cuidadoPersonal = "Ejercicio ligero",
                    emocionesPredominantes = listOf("cansancio"),
                    pensamientosNegativos = "Me sentí desorganizado",
                    nivelAnsiedad = 4,
                    calidadSueno = "Mala",
                    agradecimientos = "Un café delicioso",
                    intensidadAutocritica = 5,
                    expectativaManana = "Organizar mejor mi día",
                    otrosComentarios = "Día estresante",
                    notaGlobal = 5
                ),
                Formulario(
                    fecha = "2024-11-05",
                    estadoAnimo = 4,
                    motivacion = 5,
                    trabajo = 7,
                    descanso = 5,
                    ejercicio = 3,
                    social = 2,
                    hobbies = 2,
                    tiempoClima = "nublado",
                    logros = "Completé un curso online",
                    cuidadoPersonal = "Spa en casa",
                    emocionesPredominantes = listOf("orgullo"),
                    pensamientosNegativos = "",
                    nivelAnsiedad = 2,
                    calidadSueno = "Buena",
                    agradecimientos = "Aprender algo nuevo",
                    intensidadAutocritica = 2,
                    expectativaManana = "Seguir aprendiendo",
                    otrosComentarios = "Día satisfactorio",
                    notaGlobal = 9
                )
            )

            MonthlyCalendar(currentMonth = LocalDate.now(), data = exampleData)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MonthlyCalendar(currentMonth: LocalDate, data: List<Formulario>) {
    val daysInMonth = YearMonth.from(currentMonth).lengthOfMonth()
    val firstDayOfMonth = currentMonth.withDayOfMonth(1)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        // Días vacíos para ajustar el inicio del mes
        val emptyDays = firstDayOfMonth.dayOfWeek.value % 7
        repeat(emptyDays) {
            item {
                Box(modifier = Modifier.size(40.dp))
            }
        }

        // Días del mes
        for (day in 1..daysInMonth) {
            val date = currentMonth.withDayOfMonth(day).format(formatter)
            val formData = data.find { it.fecha == date }
            val color = getColorForEstadoAnimo(formData?.estadoAnimo ?: 0)

            item {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Mostrar el número del día
                    Text(text = day.toString(), color = Color.Black)
                }
            }
        }
    }
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
