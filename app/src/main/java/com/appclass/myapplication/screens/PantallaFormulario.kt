package com.appclass.myapplication.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Questionario(navController: NavController) {

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FormularioInput(navController: NavController) {
    // State variables for form fields
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
            SliderWithLabel2(label = "Motivación", value = motivacion.value, onValueChange = { motivacion.value = it })
        }

        item {
            SliderWithLabel(label = "Trabajo", value = trabajo.value, onValueChange = { trabajo.value = it })
        }

        item {
            SliderWithLabel(label = "Descanso", value = descanso.value, onValueChange = { descanso.value = it })
        }

        item {
            SliderWithLabel(label = "Ejercicio", value = ejercicio.value, onValueChange = { ejercicio.value = it })
        }

        item {
            SliderWithLabel(label = "Social", value = social.value, onValueChange = { social.value = it })
        }

        item {
            SliderWithLabel(label = "Hobbies", value = hobbies.value, onValueChange = { hobbies.value = it })
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
            SliderWithLabel2(label = "Nivel de ansiedad", value = nivelAnsiedad.value, onValueChange = { nivelAnsiedad.value = it })
        }

        item {
            SuenoSelection(selectedSueno = selectedSueno)
        }

        item {
            TextInputField("Agradecimientos", agradecimientos.value) { agradecimientos.value = it }
        }

        item {
            SliderWithLabel2(label = "Intensidad de autocritica", value = intensidadAutocritica.value, onValueChange = { intensidadAutocritica.value = it })
        }

        item {
            TextInputField("Expectativa para mañana", expectativaManana.value) { expectativaManana.value = it }
        }

        item {
            TextInputField("Otros comentarios", otrosComentarios.value) { otrosComentarios.value = it }
        }

        item {
            SliderWithLabel3(label = "Nota global", value = notaGlobal.value, onValueChange = { notaGlobal.value = it })
        }

        item {
            EnviarFormularioButton(
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



        /*
                        item {
                            Button(onClick = {
                                EnviarFormulario(
                                    logros, cuidadoPersonal, emociones, pensamientosNegativos, nivelAnsiedad,
                                    selectedSueno, agradecimientos, intensidadAutocritica, expectativaManana,
                                    otrosComentarios, notaGlobal
                                )
                            }) {
                                Text("Enviar formulario")
                            }
                        }
        item {
            Button(onClick = {
                EnviarFormulario(
                    trabajo, descanso, ejercicio, social, hobbies
                )
            }) {
                Text("Enviar formulario")
            }
        }*/

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
                modifier = Modifier.size(70.dp).padding(8.dp)
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

@Composable
fun SliderWithLabel(label: String, value: Int, onValueChange: (Int) -> Unit) {
    // Display the label and current value of the slider
    Text("$label: $value")

    // Log when the slider value changes
    Slider(
        value = value.toFloat(),
        onValueChange = { newValue ->
            // Log the change to Logcat
            Log.d("Formulario", "$label cambiado a: $newValue")
            onValueChange(newValue.toInt())
        },
        valueRange = 0f..5f,
        steps = 23
    )
}

@Composable
fun SliderWithLabel2(label: String, value: Int, onValueChange: (Int) -> Unit) {
    // Display the label and current value of the slider
    Text("$label: $value")

    // Log when the slider value changes
    Slider(
        value = value.toFloat(),
        onValueChange = { newValue ->
            // Log the change to Logcat
            Log.d("Formulario", "$label cambiado a: $newValue")
            onValueChange(newValue.toInt())
        },
        valueRange = 0f..10f,
        steps = 23
    )
}
@Composable
fun SliderWithLabel3(label: String, value: Int, onValueChange: (Int) -> Unit) {
    // Display the label and current value of the slider
    Text("$label: $value")

    // Log when the slider value changes
    Slider(
        value = value.toFloat(),
        onValueChange = { newValue ->
            // Log the change to Logcat
            Log.d("Formulario", "$label cambiado a: $newValue")
            onValueChange(newValue.toInt())
        },
        valueRange = 0f..24f,
        steps = 23
    )
}

@Composable
fun WeatherSelection(selectedWeather: MutableState<String>) {
    Log.d("Formulario", "Opción de clima seleccionada: ${selectedWeather.value}")

    Text("Selecciona las condiciones meteorológicas:", style = MaterialTheme.typography.bodyMedium)

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Button for Sunny weather
        WeatherButton(
            weatherType = "Soleado",
            selectedWeather = selectedWeather
        )

        // Button for Cloudy weather
        WeatherButton(
            weatherType = "Nublado",
            selectedWeather = selectedWeather
        )

        // Button for Rainy weather
        WeatherButton(
            weatherType = "Lluvia",
            selectedWeather = selectedWeather
        )

        // Button for Stormy weather
        WeatherButton(
            weatherType = "Tormenta",
            selectedWeather = selectedWeather
        )

        // Button for Foggy weather
        WeatherButton(
            weatherType = "Niebla",
            selectedWeather = selectedWeather
        )

        // Button for Snowy weather
        WeatherButton(
            weatherType = "Nieve",
            selectedWeather = selectedWeather
        )
    }
}

@Composable
fun WeatherButton(weatherType: String, selectedWeather: MutableState<String>) {
    Button(
        onClick = {
            selectedWeather.value = weatherType
            Log.d("Formulario", "Valor de clima actualizado: ${selectedWeather.value}")
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (selectedWeather.value == weatherType) Color.Blue else Color.Gray,
                shape = RectangleShape
            ),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(weatherType, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun EmocionesSelection(emociones: MutableList<String>) {
    Log.d("Formulario", "Emociones seleccionadas: $emociones")

    Text("Selecciona tus emociones actuales:", style = MaterialTheme.typography.bodyMedium)

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val emocionesList = listOf(
            "Alegría", "Esperanza", "Satisfacción", "Gratitud", "Tristeza", "Ira", "Miedo", "Vergüenza",
            "Frustración", "Culpa", "Celos", "Confusión", "Ansiedad", "Alivio", "Empatía"
        )

        emocionesList.forEach { emocion ->
            EmocionButton(emocion = emocion, emociones = emociones)
        }
    }
}

@Composable
fun EmocionButton(emocion: String, emociones: MutableList<String>) {
    Button(
        onClick = {
            if (emociones.contains(emocion)) {
                emociones.remove(emocion)
            } else {
                emociones.add(emocion)
            }
            Log.d("Formulario", "Emociones actualizadas: $emociones")
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = if (emociones.contains(emocion)) Color.Blue else Color.Gray,
                shape = RectangleShape
            ),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(emocion, style = MaterialTheme.typography.bodySmall)
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
    Button(onClick = {
        Log.d(
            "Formulario",
            "Datos enviados: estadoAnimo=$estadoAnimo, motivacion=$motivacion, trabajo=$trabajo, descanso=$descanso, ejercicio=$ejercicio, social=$social, hobbies=$hobbies, tiempoClima=${selectedWeather.toString()}, logros=$logros, cuidadoPersonal=$cuidadoPersonal, emocionesPredominantes=$emociones, pensamientosNegativos=$pensamientosNegativos, nivelAnsiedad=$nivelAnsiedad, calidadSueno=$selectedSueno, agradecimientos=$agradecimientos, intensidadAutocritica=$intensidadAutocritica, expectativaManana=$expectativaManana, otrosComentarios=$otrosComentarios, notaGlobal=$notaGlobal"
        )
        val diaYHora: String = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        Log.d("Formulario", "dia y hora: $diaYHora")

        val formularioId = UUID.randomUUID().toString()
        Log.d("Formulario", "ID generado: $formularioId")


        val formulario = Formulario(
            fecha = diaYHora,
            estadoAnimo = estadoAnimo,
            motivacion = motivacion,
            trabajo = trabajo,
            descanso = descanso,
            ejercicio = ejercicio,
            social = social,
            hobbies = hobbies,
            tiempoClima = selectedWeather.toString(),
            logros = logros,
            cuidadoPersonal = cuidadoPersonal,
            emocionesPredominantes = emociones,
            pensamientosNegativos = pensamientosNegativos,
            nivelAnsiedad = nivelAnsiedad,
            calidadSueno = selectedSueno,
            agradecimientos = agradecimientos,  // sale automático
            intensidadAutocritica = intensidadAutocritica,  // sale automático
            expectativaManana = expectativaManana,  // sale automático
            otrosComentarios = otrosComentarios,  // sale automático
            notaGlobal = notaGlobal  // sale automático
        )

        // Aquí lógica para enviar los datos a Firestore
        val db = FirebaseFirestore.getInstance()

        db.collection("formularioDiario")
            .document(formularioId)
            .set(formulario)
            .addOnSuccessListener {
                Log.d("Formulario", "Formulario guardado con éxito!")
            }
            .addOnFailureListener { e ->
                Log.e("Formulario", "Error al guardar el formulario", e)
            }
    }) {
        Text("Enviar")
    }
}
