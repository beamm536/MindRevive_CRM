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
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.YearMonth

import java.time.format.DateTimeFormatter


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
            Text(text = "Gráficos de los últimos 28 días", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))


            // Mostrar cuadrícula de colores
            val exampleData = listOf(
                com.appclass.myapplication.screens.Formulario(
                    fecha = "2024-11-19",
                    estadoAnimo = 3,
                    motivacion = 4,
                    trabajo = 7,
                    descanso = 8,
                    ejercicio = 1,
                    social = 2,
                    hobbies = 3,
                    tiempoClima = "soleado",
                    logros = "Gran progreso en el trabajo",
                    cuidadoPersonal = "Meditación",
                    emocionesPredominantes = listOf("felicidad"),
                    pensamientosNegativos = "",
                    nivelAnsiedad = 1,
                    calidadSueno = "Buena",
                    agradecimientos = "Tiempo con amigos",
                    intensidadAutocritica = 2,
                    expectativaManana = "Continuar con buen ritmo",
                    otrosComentarios = "",
                    notaGlobal = 9
                )
            )

            Last30DaysCalendar(data = exampleData)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Last30DaysCalendar(data: List<Formulario>) {
    val db = FirebaseFirestore.getInstance()

    //la colección se llama formularioDiario

    val today = LocalDate.now()
    val last30Days = (0 until 28).map { today.minusDays(it.toLong()) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        last30Days.forEach { date ->
            val formattedDate = date.format(formatter)
            val formData = data.find { it.fecha == formattedDate }
            val color = getColorForEstadoAnimo(formData?.estadoAnimo ?: 0)
            item {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color, shape = CircleShape),

                )
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
