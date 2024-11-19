package com.appclass.myapplication.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.util.UUID

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun ButtonAddForms(navHostController: NavHostController) {
    Column {
        // Botón para cargar formularios de ejemplo
        Button(onClick = {
            cargarFormulariosDeEjemplo(navHostController)
        }) {
            Text("Cargar Formularios de Ejemplo")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun cargarFormulariosDeEjemplo(navHostController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val hoy = LocalDate.now()
    val random = java.util.Random()

    // Generar 30 días de formularios de ejemplo
    val ejemplos = (0 until 30).map { diasAtras ->
        val fecha = hoy.minusDays(diasAtras.toLong()).toString()
        Formulario(
            fecha = fecha,
            estadoAnimo = random.nextInt(5) + 1, // Valores de 1 a 5
            motivacion = random.nextInt(5) + 1,
            trabajo = random.nextInt(9), // Horas de trabajo (0-8)
            descanso = random.nextInt(9), // Horas de descanso (0-8)
            ejercicio = random.nextInt(4), // Horas de ejercicio (0-3)
            social = random.nextInt(5), // Horas de socialización (0-4)
            hobbies = random.nextInt(5), // Horas de hobbies (0-4)
            tiempoClima = listOf("soleado", "lluvioso", "nublado", "nevado").random(),
            logros = "Logro ejemplo ${random.nextInt(100)}",
            cuidadoPersonal = "Cuidado personal ejemplo ${random.nextInt(100)}",
            emocionesPredominantes = listOf("felicidad", "estrés", "tranquilidad", "aburrimiento").shuffled().take(random.nextInt(3) + 1),
            pensamientosNegativos = "Pensamiento negativo ejemplo ${random.nextInt(100)}",
            nivelAnsiedad = random.nextInt(5) + 1,
            calidadSueno = listOf("Mala", "Regular", "Buena", "Excelente").random(),
            agradecimientos = "Agradecimiento ejemplo ${random.nextInt(100)}",
            intensidadAutocritica = random.nextInt(5) + 1,
            expectativaManana = "Expectativa para mañana ejemplo ${random.nextInt(100)}",
            otrosComentarios = "Otros comentarios ejemplo ${random.nextInt(100)}",
            notaGlobal = random.nextInt(10) + 1
        )
    }

    // Subir cada formulario a Firestore
    ejemplos.forEach { formulario ->
        val formularioId = UUID.randomUUID().toString()
        db.collection("formularioDiario")
            .document(formularioId)
            .set(formulario)
            .addOnSuccessListener {
                Log.d("CargarEjemplos", "Formulario para ${formulario.fecha} guardado con éxito!")
            }
            .addOnFailureListener { e ->
                Log.e("CargarEjemplos", "Error al guardar el formulario para ${formulario.fecha}", e)
            }
    }
}