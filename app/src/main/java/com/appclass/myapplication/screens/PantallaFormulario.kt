package com.appclass.myapplication.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp




@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Questionario(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val today = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) // Get today's date in ISO format (yyyy-MM-dd)

    Log.e("Que busca", "$today devuelve el parametro de hoy")
    // Estado para manejar si ya se encontró el formulario o no
    var formExists by remember { mutableStateOf(false) }

    // Comprobar si el formulario existe
    LaunchedEffect(userId, today) {
        val query = db.collection("usuariosCRM")
            .document(userId ?: "")
            .collection("formulariosDiarios")
            .whereEqualTo("fecha", today)

        query.get().addOnSuccessListener { querySnapshot ->
            formExists = !querySnapshot.isEmpty
        }
        Log.e("EXISTE EL FORM?", "Solucion: $formExists")
    }

    // Mostrar contenido dependiendo de si el formulario existe
    if (formExists) {
        // Si ya existe el formulario, navega a la pantalla de gráficos
        navController.navigate("pantallaGraficos")
    } else {
        // Si no existe, muestra el formulario
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Cuestionario",
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color(0xFF0277BD)
                    )
                )
            },
            bottomBar = {
                BottomNavigationBarComponent(navController = navController)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(30.dp))
                FormularioInput(navController = navController)
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioInput(navController: NavController) {
    // State variables for form fields
    val currentUser = FirebaseAuth.getInstance().currentUser

    val formEnviadoHoy=  remember { mutableStateOf(false) }

    val estadoAnimo = remember { mutableStateOf(3) } // 0-5
    val motivacion = remember { mutableStateOf(3) }
    val trabajo = remember { mutableStateOf(8) } // default value between 0-24
    val descanso = remember { mutableStateOf(8) }
    val ejercicio = remember { mutableStateOf(1) }
    val social = remember { mutableStateOf(2) }
    val hobbies = remember { mutableStateOf(3) }
    val selectedWeather = remember { mutableStateOf("") }
    val logros = remember { mutableStateOf("") }
    val cuidadoPersonal = remember { mutableStateOf("") }
    val emociones = remember { mutableStateListOf<String>() }
    val pensamientosNegativos = remember { mutableStateOf("") }
    val nivelAnsiedad = remember { mutableStateOf(3) } // 0-5
    val selectedSueno = remember { mutableStateOf("") }
    val agradecimientos = remember { mutableStateOf("") }
    val intensidadAutocritica = remember { mutableStateOf(4) } // 0-5
    val expectativaManana = remember { mutableStateOf("") }
    val otrosComentarios = remember { mutableStateOf("") }
    val notaGlobal = remember { mutableStateOf(4) } // 0-10


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            EstadoAnimoSelector(estadoAnimo) { estadoAnimo.value = it }
        }

        item {
            SliderWithLabel(label = "Motivación", value = motivacion.value, onValueChange = { motivacion.value = it })
        }

        item {
            SliderWithLabel3(label = "Trabajo(h dedicadas)", value = trabajo.value, onValueChange = { trabajo.value = it })
        }

        item {
            SliderWithLabel3(label = "Descanso(h dedicadas)", value = descanso.value, onValueChange = { descanso.value = it })
        }

        item {
            SliderWithLabel3(label = "Ejercicio(h dedicadas)", value = ejercicio.value, onValueChange = { ejercicio.value = it })
        }

        item {
            SliderWithLabel3(label = "Social(h dedicadas)", value = social.value, onValueChange = { social.value = it })
        }

        item {
            SliderWithLabel3(label = "Hobbies(h dedicadas)", value = hobbies.value, onValueChange = { hobbies.value = it })
        }

        item {
            WeatherSelection(selectedWeather)
        }

        item {
            TextInputField("Logros", logros.value) { logros.value = it }
        }

        item {
            TextInputField("Cuidado personal", cuidadoPersonal.value) { cuidadoPersonal.value = it }
        }

        item {
            EmocionesSelection(emociones)
        }

        item {
            TextInputField("Pensamientos negativos", pensamientosNegativos.value) { pensamientosNegativos.value = it }
        }

        item {
            SliderWithLabel(label = "Nivel de ansiedad", value = nivelAnsiedad.value, onValueChange = { nivelAnsiedad.value = it })
        }

        item {
            SuenoSelection(selectedSueno = selectedSueno)
        }

        item {
            TextInputField("Agradecimientos", agradecimientos.value) { agradecimientos.value = it }
        }

        item {
            SliderWithLabel(label = "Intensidad de autocritica", value = intensidadAutocritica.value, onValueChange = { intensidadAutocritica.value = it })
        }

        item {
            TextInputField("Expectativa para mañana", expectativaManana.value) { expectativaManana.value = it }
        }

        item {
            TextInputField("Otros comentarios", otrosComentarios.value) { otrosComentarios.value = it }
        }

        item {
            SliderWithLabel2(label = "Nota global", value = notaGlobal.value, onValueChange = { notaGlobal.value = it })
        }

        item {
            EnviarFormularioButton(

                navHostController = navController,
                formEnviadoHoy= formEnviadoHoy.value,
                estadoAnimo = estadoAnimo.value, // .value para acceder al valor de MutableState
                motivacion = motivacion.value,
                trabajo = trabajo.value,
                descanso = descanso.value,
                ejercicio = ejercicio.value,
                social = social.value,
                hobbies = hobbies.value,
                selectedWeather = selectedWeather.value, // Cambiar a .value si es MutableState
                logros = logros.value,
                cuidadoPersonal = cuidadoPersonal.value,
                emociones = emociones,
                pensamientosNegativos = pensamientosNegativos.value,
                nivelAnsiedad = nivelAnsiedad.value,
                selectedSueno = selectedSueno.value,
                agradecimientos = agradecimientos.value,
                intensidadAutocritica = intensidadAutocritica.value,
                expectativaManana = expectativaManana.value,
                otrosComentarios = otrosComentarios.value,
                notaGlobal = notaGlobal.value
            )
        }


    }
}
@Composable
fun EstadoAnimoSelector(estadoAnimo: MutableState<Int>, onEstadoAnimoChange: (Int) -> Unit) {
    val coloresEstadoAnimo = listOf(
        Color.Red, // 1 -> Rojo
        Color(0xFFFF6F00), // 2 -> Naranja
        Color.Yellow, // 3 -> Amarillo
        Color.Green, // 4 -> Verde
        Color(0xFF4CAF50)  // 5 -> Verde oscuro
    )

    Text("Estado de ánimo: ${estadoAnimo.value}")
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        (1..5).forEach { index ->
            Button(
                onClick = { onEstadoAnimoChange(index) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = coloresEstadoAnimo[index - 1],
                    contentColor = Color.White
                ),
                shape = CircleShape,
                modifier = Modifier
                    .size(70.dp)
                    .padding(8.dp)
            ) {
                Text(
                    text = "$index",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

//@Composable
//fun SliderWithLabel(label: String, value: Int, onValueChange: (Int) -> Unit) {
//    // Display the label and current value of the slider
//    Text("$label: $value")
//
//    // Log when the slider value changes
//    Slider(
//        value = value.toFloat(),
//        onValueChange = { newValue ->
//            // Log the change to Logcat
//            Log.d("Formulario", "$label cambiado a: $newValue")
//            onValueChange(newValue.toInt())
//        },
//        valueRange = 0f..5f,
//        steps = 23
//    )
//}
@Composable
fun SliderWithLabel(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { newValue ->
                Log.d("Formulario", "$label cambiado a: $newValue")
                onValueChange(newValue.toInt())
            },
            valueRange = 0f..5f,
            steps = 4, // Reducir a 4 pasos para simplificar
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF6F67FA),
                activeTrackColor = Color(0xFF6F67FA)
            ),
            modifier = Modifier.width(200.dp) // Controlar el tamaño para ajustarlo al diseño
        )
    }
}


//@Composable
//fun SliderWithLabel2(label: String, value: Int, onValueChange: (Int) -> Unit) {
//    // Display the label and current value of the slider
//    Text("$label: $value")
//
//    // Log when the slider value changes
//    Slider(
//        value = value.toFloat(),
//        onValueChange = { newValue ->
//            // Log the change to Logcat
//            Log.d("Formulario", "$label cambiado a: $newValue")
//            onValueChange(newValue.toInt())
//        },
//        valueRange = 0f..10f,
//        steps = 23
//    )
//}

@Composable
fun SliderWithLabel2(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { newValue ->
                Log.d("Formulario", "$label cambiado a: $newValue")
                onValueChange(newValue.toInt())
            },
            valueRange = 0f..10f,
            steps = 9, // Ajustado para reflejar pasos iguales (0 a 10)
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF6F67FA),
                activeTrackColor = Color(0xFF6F67FA)
            ),
            modifier = Modifier.width(200.dp) // Tamaño controlado para mantener proporciones
        )
    }
}

//@Composable
//fun SliderWithLabel3(label: String, value: Int, onValueChange: (Int) -> Unit) {
//    // Display the label and current value of the slider
//    Text("$label: $value")
//
//    // Log when the slider value changes
//    Slider(
//        value = value.toFloat(),
//        onValueChange = { newValue ->
//            // Log the change to Logcat
//            Log.d("Formulario", "$label cambiado a: $newValue")
//            onValueChange(newValue.toInt())
//        },
//        valueRange = 0f..24f,
//        steps = 23
//    )
//}

@Composable
fun SliderWithLabel3(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { newValue ->
                Log.d("Formulario", "$label cambiado a: $newValue")
                onValueChange(newValue.toInt())
            },
            valueRange = 0f..24f,
            steps = 23, // Manteniendo los pasos especificados
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF6F67FA),
                activeTrackColor = Color(0xFF6F67FA)
            ),
            modifier = Modifier.width(200.dp) // Controla el ancho para mantener un diseño limpio
        )
    }
}


//@Composable
//fun WeatherSelection(selectedWeather: MutableState<String>) {
//    Log.d("Formulario", "Opción de clima seleccionada: ${selectedWeather.value}")
//
//    Text(
//        "Selecciona las condiciones meteorológicas:",
//        style = MaterialTheme.typography.bodyMedium
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        // Primera fila con tres botones
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            WeatherButton(weatherType = "Soleado", selectedWeather = selectedWeather)
//            WeatherButton(weatherType = "Nublado", selectedWeather = selectedWeather)
//            WeatherButton(weatherType = "Lluvia", selectedWeather = selectedWeather)
//        }
//
//        // Segunda fila con tres botones
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            WeatherButton(weatherType = "Tormenta", selectedWeather = selectedWeather)
//            WeatherButton(weatherType = "Niebla", selectedWeather = selectedWeather)
//            WeatherButton(weatherType = "Nieve", selectedWeather = selectedWeather)
//        }
//    }
//}

//@Composable
//fun WeatherSelection(selectedWeather: MutableState<String>) {
//    Log.d("Formulario", "Opción de clima seleccionada: ${selectedWeather.value}")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            "Selecciona las condiciones meteorológicas:",
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color(0xFF6F67FA)
//        )
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp) // Altura fija para evitar restricciones infinitas
//        ) {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(8.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                val weatherOptions = listOf("Soleado", "Nublado", "Lluvia", "Tormenta", "Niebla", "Nieve")
//                items(weatherOptions.size) { index ->
//                    WeatherButton(
//                        weatherType = weatherOptions[index],
//                        selectedWeather = selectedWeather
//                    )
//                }
//            }
//        }
//    }
//}
//@Composable
//fun WeatherSelection(selectedWeather: MutableState<String>) {
//    Log.d("Formulario", "Opción de clima seleccionada: ${selectedWeather.value}")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            "Selecciona las condiciones meteorológicas:",
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color(0xFF6F67FA)
//        )
//
//        // Caja con tamaño controlado para el LazyVerticalGrid
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(200.dp) // Limita la altura de la caja
//        ) {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(3),
//                modifier = Modifier.fillMaxSize(),
//                contentPadding = PaddingValues(8.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                val weatherOptions = listOf("Soleado", "Nublado", "Lluvia", "Tormenta", "Niebla", "Nieve")
//                items(weatherOptions.size) { index ->
//                    WeatherButton(
//                        weatherType = weatherOptions[index],
//                        selectedWeather = selectedWeather,
//                        modifier = Modifier.height(40.dp) // Ajuste de tamaño de los botones
//                    )
//                }
//            }
//        }
//    }
//}
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun WeatherSelection(selectedWeather: MutableState<String>) {
//    Log.d("Formulario", "Opción de clima seleccionada: ${selectedWeather.value}")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            "Selecciona las condiciones meteorológicas:",
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color(0xFF6F67FA)
//        )
//
//        // Usa FlowRow sin mainAxisSpacing y crossAxisSpacing
//        FlowRow(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            val weatherOptions = listOf("Soleado", "Nublado", "Lluvia", "Tormenta", "Niebla", "Nieve")
//            weatherOptions.forEach { weatherType ->
//                WeatherButton(
//                    weatherType = weatherType,
//                    selectedWeather = selectedWeather,
//                    modifier = Modifier.padding(8.dp) // Espaciado entre los botones
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun WeatherButton(weatherType: String, selectedWeather: MutableState<String>, modifier: Modifier = Modifier) {
//    Button(
//        onClick = { selectedWeather.value = weatherType },
//        modifier = modifier
//    ) {
//        Text(text = weatherType)
//    }
//}

//ULTIMO
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun WeatherSelection(selectedWeather: MutableState<String>) {
//    Log.d("Formulario", "Opción de clima seleccionada: ${selectedWeather.value}")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            "Selecciona las condiciones meteorológicas:",
//            style = MaterialTheme.typography.bodyMedium,
//            color = Color(0xFF6F67FA)
//        )
//
//        // Usa FlowRow sin mainAxisSpacing y crossAxisSpacing
//        FlowRow(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            val weatherOptions = listOf("Soleado", "Nublado", "Lluvia", "Tormenta", "Niebla", "Nieve")
//            weatherOptions.forEach { weatherType ->
//                WeatherButton(
//                    weatherType = weatherType,
//                    selectedWeather = selectedWeather,
//                    modifier = Modifier.padding(4.dp) // Espaciado entre los botones
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun WeatherButton(weatherType: String, selectedWeather: MutableState<String>, modifier: Modifier = Modifier) {
//    val isSelected = selectedWeather.value == weatherType
//
//    Button(
//        onClick = { selectedWeather.value = weatherType },
//        modifier = modifier
//            .height(50.dp) // Altura fija para los botones
//            .width(100.dp), // Ancho fijo para los botones
//        colors = ButtonDefaults.buttonColors(
//            containerColor = if (isSelected) Color(0xFF6F67FA) else Color.LightGray
//        ),
//        shape = MaterialTheme.shapes.small // Rectangular
//    ) {
//        Text(
//            text = weatherType,
//            fontWeight = FontWeight.Bold,
//            color = Color.White,
//            style = MaterialTheme.typography.bodySmall
//        )
//    }
//}

//ULTIMO 2
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun WeatherSelection(selectedWeather: MutableState<String>) {
//    Log.d("Formulario", "Opción de clima seleccionada: ${selectedWeather.value}")
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            "Selecciona las condiciones meteorológicas:",
//            style = MaterialTheme.typography.bodyMedium, // Mismo estilo que el texto de emociones
//            color = MaterialTheme.colorScheme.primary // Usar el color primario del tema
//        )
//
//        FlowRow(
//            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
//            horizontalArrangement = Arrangement.Center // Centra horizontalmente los botones
//        ) {
//            val weatherOptions = listOf("Soleado", "Nublado", "Lluvia", "Tormenta", "Niebla", "Nieve")
//            weatherOptions.forEach { weatherType ->
//                WeatherButton(
//                    weatherType = weatherType,
//                    selectedWeather = selectedWeather,
//                    modifier = Modifier.padding(8.dp) // Espaciado uniforme entre los botones
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun WeatherButton(weatherType: String, selectedWeather: MutableState<String>, modifier: Modifier = Modifier) {
//    val isSelected = selectedWeather.value == weatherType
//
//    Button(
//        onClick = { selectedWeather.value = weatherType },
//        modifier = modifier
//            .height(50.dp) // Altura fija para los botones
//            .width(120.dp), // Botones más anchos
//        colors = ButtonDefaults.buttonColors(
//            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
//        ),
//        shape = MaterialTheme.shapes.small // Rectangular
//    ) {
//        Text(
//            text = weatherType,
//            fontWeight = FontWeight.Bold,
//            color = Color.White,
//            style = MaterialTheme.typography.bodySmall
//        )
//    }
//}
@Composable
fun WeatherSelection(selectedWeather: MutableState<String>) {
    Log.d("Formulario", "Opción de clima seleccionada: ${selectedWeather.value}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Selecciona las condiciones meteorológicas:",
            style = MaterialTheme.typography.bodyMedium, // Mismo estilo que el texto de emociones
            color = MaterialTheme.colorScheme.primary // Usar el color primario del tema
        )

        // Dividir las opciones en grupos de 3 para formar filas
        val weatherOptions = listOf("Soleado", "Nublado", "Lluvia", "Tormenta", "Niebla", "Nieve")
        val chunkedWeatherOptions = weatherOptions.chunked(3)

        // Contenedor más ancho para reducir el espaciado relativo
        Column(
            modifier = Modifier.fillMaxWidth(0.9f), // Hace que el contenedor ocupe el 90% del ancho
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chunkedWeatherOptions.forEach { rowWeatherOptions ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Menor espacio entre columnas
                ) {
                    rowWeatherOptions.forEach { weatherType ->
                        WeatherButton(
                            weatherType = weatherType,
                            selectedWeather = selectedWeather
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherButton(weatherType: String, selectedWeather: MutableState<String>, modifier: Modifier = Modifier) {
    val isSelected = selectedWeather.value == weatherType

    Button(
        onClick = { selectedWeather.value = weatherType },
        modifier = modifier
            .height(50.dp) // Altura fija para los botones
            .width(105.dp), // Botones más estrechos
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
        ),
        shape = MaterialTheme.shapes.small // Rectangular
    ) {
        Text(
            text = weatherType,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}





//@Composable
//fun WeatherButton(weatherType: String, selectedWeather: MutableState<String>, modifier: Modifier = Modifier) {
//    val isSelected = selectedWeather.value == weatherType
//    Button(
//        onClick = { selectedWeather.value = weatherType },
//        colors = ButtonDefaults.buttonColors(
//            containerColor = if (isSelected) Color(0xFF6F67FA) else Color(0xFFF0F0F0),
//            contentColor = if (isSelected) Color.White else Color.Black
//        ),
//        shape = RoundedCornerShape(12.dp),
//        modifier = Modifier
//            .fillMaxWidth()
//            .aspectRatio(1f) // Hace que los botones sean cuadrados
//    ) {
//        Text(
//            text = weatherType,
//            style = MaterialTheme.typography.bodySmall // Ajusta el tamaño del texto
//        )
//    }
//}



//@Composable
//fun WeatherButton(weatherType: String, selectedWeather: MutableState<String>) {
//    Button(
//        onClick = {
//            selectedWeather.value = weatherType
//            Log.d("Formulario", "Valor de clima actualizado: ${selectedWeather.value}")
//        },
//        modifier = Modifier
//
//            .border(
//                width = 2.dp,
//                color = if (selectedWeather.value == weatherType) Color.Blue else Color.Gray,
//                shape = RectangleShape
//            ),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        Text(weatherType, style = MaterialTheme.typography.bodySmall)
//    }
//}

//@Composable
//fun EmocionesSelection(emociones: MutableList<String>) {
//    Log.d("Formulario", "Emociones seleccionadas: $emociones")
//
//    Text("Selecciona tus emociones actuales:", style = MaterialTheme.typography.bodyMedium)
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        val emocionesList = listOf(
//            "Alegría", "Esperanza", "Satisfacción", "Gratitud", "Tristeza", "Ira", "Miedo", "Vergüenza",
//            "Frustración", "Culpa", "Celos", "Confusión", "Ansiedad", "Alivio", "Empatía"
//        )
//
//        emocionesList.forEach { emocion ->
//            EmocionButton(emocion = emocion, emociones = emociones)
//        }
//    }
//}
//
//@Composable
//fun EmocionButton(emocion: String, emociones: MutableList<String>) {
//    Button(
//        onClick = {
//            if (emociones.contains(emocion)) {
//                emociones.remove(emocion)
//            } else {
//                emociones.add(emocion)
//            }
//            Log.d("Formulario", "Emociones actualizadas: $emociones")
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .border(
//                width = 2.dp,
//                color = if (emociones.contains(emocion)) Color.Blue else Color.Gray,
//                shape = RectangleShape
//            ),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        Text(emocion, style = MaterialTheme.typography.bodySmall)
//    }
//}

//@Composable
//fun EmocionesSelection(emociones: MutableList<String>) {
//    Log.d("Formulario", "Emociones seleccionadas: $emociones")
//
//    Text("Selecciona tus emociones actuales:", style = MaterialTheme.typography.bodyMedium)
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(top = 8.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp), // Espacio entre filas
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        val emocionesList = listOf(
//            "Alegría", "Esperanza", "Satisfacción", "Alivio", "Tristeza", "Ira", "Miedo", "Vergüenza",
//            "Frustración", "Culpa", "Celos", "Confusión", "Ansiedad", "Gratitud", "Empatía"
//        )
//
//        // Creamos filas manualmente para controlar mejor el espaciado
//        val chunkedEmociones = emocionesList.chunked(4) // Divide en filas de 4 botones
//
//        chunkedEmociones.forEach { rowEmotions ->
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(2.dp), // Espacio entre los botones en cada fila
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                rowEmotions.forEach { emocion ->
//                    EmocionButton(emocion = emocion, emociones = emociones)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EmocionButton(emocion: String, emociones: MutableList<String>) {
//    val isSelected = emociones.contains(emocion)
//
//    Button(
//        onClick = {
//            if (isSelected) {
//                emociones.remove(emocion)
//            } else {
//                emociones.add(emocion)
//            }
//            Log.d("Formulario", "Emociones actualizadas: $emociones")
//        },
//        modifier = Modifier
//            .height(50.dp) // Altura fija
//            .width(120.dp), // Ancho fijo
//        colors = ButtonDefaults.buttonColors(
//            containerColor = if (isSelected) Color(0xFF6F67FA) else Color.LightGray
//        ),
//        shape = MaterialTheme.shapes.small, // Forma rectangular
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        Text(
//            text = emocion,
//            fontWeight = FontWeight.Bold,
//            color = Color.White,
//            style = MaterialTheme.typography.bodySmall
//        )
//    }
//}

//

@Composable
fun EmocionesSelection(emociones: MutableList<String>) {
    Log.d("Formulario", "Emociones seleccionadas: $emociones")

    Text("Selecciona tus emociones actuales:", style = MaterialTheme.typography.bodyMedium)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Espacio uniforme entre columnas
    ) {
        val emocionesList = listOf(
            "Alegría", "Esperanza", "Satisfacción", "Alivio", "Tristeza",
            "Ira", "Miedo", "Vergüenza", "Frustración", "Culpa",
            "Celos", "Confusión", "Ansiedad", "Gratitud", "Empatía"
        )

        // Dividimos las emociones en 3 columnas de 5 elementos cada una
        val chunkedEmociones = emocionesList.chunked(5)

        chunkedEmociones.forEach { columnEmotions ->
            Column(
                modifier = Modifier
                    .weight(1f) // Cada columna ocupa el mismo ancho
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre botones en la columna
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                columnEmotions.forEach { emocion ->
                    EmocionButton(emocion = emocion, emociones = emociones)
                }
            }
        }
    }
}

@Composable
fun EmocionButton(emocion: String, emociones: MutableList<String>) {
    val isSelected = emociones.contains(emocion)

    Box(
        modifier = Modifier.size(width = 120.dp, height = 50.dp) // Tamaño fijo
    ) {
        Button(
            onClick = {
                if (isSelected) {
                    emociones.remove(emocion)
                } else {
                    emociones.add(emocion)
                }
                Log.d("Formulario", "Emociones actualizadas: $emociones")
            },
            modifier = Modifier.fillMaxSize(), // El botón ocupa todo el Box
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSelected) Color(0xFF6F67FA) else Color.LightGray
            ),
            shape = MaterialTheme.shapes.small,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = emocion,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}







@Composable
fun SuenoSelection(selectedSueno: MutableState<String>) {
    Log.d("Formulario", "Opción seleccionada para sueño: ${selectedSueno.value}")

    Text("Selecciona cómo te sientes sobre tu sueño:", style = MaterialTheme.typography.bodyMedium)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val suenoOptions = listOf("Muy mal", "Mal", "Regular", "Bien", "Muy bien")

        suenoOptions.forEach { option ->
            SuenoButton(option = option, selectedSueno = selectedSueno)
        }
    }
}

@Composable
fun SuenoButton(option: String, selectedSueno: MutableState<String>) {
    Button(
        onClick = {
            selectedSueno.value = if (selectedSueno.value == option) "" else option
            Log.d("Formulario", "Opción seleccionada para sueño: ${selectedSueno.value}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (selectedSueno.value == option) Color.Blue else Color.Gray,
                shape = RectangleShape
            ),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(option, style = MaterialTheme.typography.bodySmall)
    }
}



@Composable
fun TextInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Text(label)
    TextField(value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth())
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EnviarFormularioButton(
    navHostController: NavController,
    formEnviadoHoy: Boolean,
    estadoAnimo: Int,
    motivacion: Int,
    trabajo: Int,
    descanso: Int,
    ejercicio: Int,
    social: Int,
    hobbies: Int,
    selectedWeather: String,
    logros: String,
    cuidadoPersonal: String,
    emociones: List<String>,
    pensamientosNegativos: String,
    nivelAnsiedad: Int,
    selectedSueno: String,
    agradecimientos: String,
    intensidadAutocritica: Int,
    expectativaManana: String,
    otrosComentarios: String,
    notaGlobal: Int
) {
    val formEnviadoHoyState = remember { mutableStateOf(formEnviadoHoy) }
    Button(onClick = {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid ?: "Usuario desconocido"
            val diaYHora = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            val formularioId = UUID.randomUUID().toString()

            // Log de datos básicos
            Log.d("Formulario", "Usuario: $userId, Fecha: $diaYHora, ID: $formularioId")

            val formulario = Formulario(
                fecha = diaYHora,
                estadoAnimo = estadoAnimo,
                motivacion = motivacion,
                trabajo = trabajo,
                descanso = descanso,
                ejercicio = ejercicio,
                social = social,
                hobbies = hobbies,
                tiempoClima = selectedWeather,
                logros = logros,
                cuidadoPersonal = cuidadoPersonal,
                emocionesPredominantes = emociones,
                pensamientosNegativos = pensamientosNegativos,
                nivelAnsiedad = nivelAnsiedad,
                calidadSueno = selectedSueno,
                agradecimientos = agradecimientos,
                intensidadAutocritica = intensidadAutocritica,
                expectativaManana = expectativaManana,
                otrosComentarios = otrosComentarios,
                notaGlobal = notaGlobal
            )

            // Envío del formulario a Firestore
            val db = FirebaseFirestore.getInstance()
            db.collection("usuariosCRM")
                .document(userId)
                .collection("formulariosDiarios")
                .document(formularioId)
                .set(formulario)
                .addOnSuccessListener {
                    Log.d("Formulario", "Formulario guardado con éxito para el usuario $userId")
                    formEnviadoHoyState.value = true
                }
                .addOnFailureListener { e ->
                    Log.e("Formulario", "Error al guardar el formulario", e)
                }
        } else {
            // Manejo del caso donde no hay usuario autenticado
            Log.e("Formulario", "No hay usuario autenticado")
        }
        navHostController.navigate("pantallaGraficos")
    }) {
        Text("Enviar")
    }
    val formEnviadoFinal = formEnviadoHoyState.value
    Log.e("Valor de formEnviado pasa pag", "hola $formEnviadoFinal")


}


