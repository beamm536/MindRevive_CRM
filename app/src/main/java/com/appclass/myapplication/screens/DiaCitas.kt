package com.appclass.myapplication.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appclass.myapplication.R
import com.appclass.myapplication.ui.theme.MarronBtns
import com.appclass.myapplication.ui.theme.MoradoTextFields
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaCitas(dia: Int) {
    val db = FirebaseFirestore.getInstance()
    val coleccion = "citas"
    val auth = FirebaseAuth.getInstance()
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
            .padding(16.dp)
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Día $dia",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MarronBtns,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campos de texto para ingresar los detalles de la cita
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Tipo de Cita") },
            shape = RoundedCornerShape(16.dp),
            colors = outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = MoradoTextFields,
                cursorColor = MoradoTextFields
            )
        )

        OutlinedTextField(
            value = medico,
            onValueChange = { medico = it },
            label = { Text("Nombre del Médico") },
            shape = RoundedCornerShape(16.dp),
            colors = outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = MoradoTextFields,
                cursorColor = MoradoTextFields
            )
        )

        OutlinedTextField(
            value = hora,
            onValueChange = { hora = it },
            label = { Text("Hora (HH:mm)") },
            shape = RoundedCornerShape(16.dp),
            colors = outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = MoradoTextFields,
                cursorColor = MoradoTextFields
            )
        )

        Spacer(modifier = Modifier.size(5.dp))

        // Botón para añadir o editar cita
        Button(
            onClick = {
                if (nombre.isBlank() || medico.isBlank() || hora.isBlank()) {
                    mensaje = "Todos los campos deben estar completos."
                    return@Button
                }

                if (!hora.matches(Regex("\\d{2}:\\d{2}"))) {
                    mensaje = "Formato de hora inválido. Usa HH:mm."
                    return@Button
                }

                val horaNueva = try {
                    hora.split(":").let { parts ->
                        parts[0].toInt() * 60 + parts[1].toInt()
                    }
                } catch (e: Exception) {
                    mensaje = "Formato de hora no válido. Usa HH:mm."
                    return@Button
                }

                val conflicto = citas.any { cita ->
                    val horaExistente = cita.hora.split(":").let { parts ->
                        parts[0].toInt() * 60 + parts[1].toInt()
                    }
                    val diferencia = kotlin.math.abs(horaNueva - horaExistente)
                    diferencia < 60
                }

                if (conflicto) {
                    mensaje = "Conflicto de horarios: debe haber al menos una hora entre citas."
                    return@Button
                }

                val userUid = auth.currentUser?.uid
                if (userUid == null) {
                    mensaje = "Usuario no autenticado. No se puede guardar la cita."
                    return@Button
                }

                val datos = hashMapOf(
                    "nombre" to nombre,
                    "medico" to medico,
                    "dia" to String.format("%02d", dia),
                    "hora" to hora,
                    "usuario" to userUid
                )

                if (editada && !editandoCitaId.isNullOrEmpty()) {
                    // Modo edición: actualizar cita existente
                    db.collection(coleccion)
                        .document(editandoCitaId!!)
                        .update(datos as Map<String, Any>)
                        .addOnSuccessListener {
                            mensaje = "Cita actualizada correctamente"
                            limpiarFormulario()
                            cargarCitas(dia, db, coleccion) { listaCitas ->
                                citas = listaCitas
                            }
                        }
                        .addOnFailureListener { e ->
                            mensaje = "Error al actualizar la cita: ${e.localizedMessage}"
                            Log.e("FirebaseError", "Error al actualizar cita", e)
                        }
                } else {
                    // Modo agregar: crear nueva cita
                    // Después de añadir una cita correctamente
                    db.collection(coleccion)
                        .document()
                        .set(datos)
                        .addOnSuccessListener {
                            mensaje = "Cita guardada correctamente"
                            limpiarFormulario()
                            cargarCitas(dia, db, coleccion) { listaCitas ->
                                citas = listaCitas
                            }
                        }
                        .addOnFailureListener { e ->
                            mensaje = "Error al guardar la cita: ${e.localizedMessage}"
                            Log.e("FirebaseError", "Error al guardar cita", e)
                        }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MoradoTextFields),
            modifier = Modifier
                //.fillMaxWidth()
                .width(180.dp)
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = if (editada) "Actualizar Cita"
                       else "Añadir Cita",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = mensaje,
            color = Color.Red
            //fontWeight = FontWeight.Bold
        )

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
                    Text(
                        text = "Tipo de Cita: ${cita.nombre}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Médico: ${cita.medico}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Hora: ${cita.hora}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
        ImgDiaIndividuales(modifier = Modifier)
    }
}

// Función que carga las citas de un día específico desde Firestore
fun cargarCitas(dia: Int, db: FirebaseFirestore, coleccion: String, onCitasLoaded: (List<Citas>) -> Unit) {

    val uidUsuarioActual = FirebaseAuth.getInstance().currentUser?.uid

    db.collection(coleccion)
        .whereEqualTo("usuario", uidUsuarioActual)
        .whereEqualTo("dia", String.format("%02d", dia))
        .get()
        .addOnSuccessListener { documents ->
            val citas = documents.mapNotNull { doc ->
                doc.toObject(Citas::class.java).apply { id = doc.id }
            }
            onCitasLoaded(citas)
        }
        .addOnFailureListener { e ->
            Log.e("FirebaseError", "Error al cargar citas", e)
        }
}

// Función para limpiar los campos del formulario
fun limpiarFormulario() {
    var nombre = ""
    var medico = ""
    var hora = ""
    var editada = false
    var editandoCitaId = null
}


@Composable
fun ImgDiaIndividuales(modifier: Modifier){
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.padding(top = 48.dp))
        Image(
            painter = painterResource(id = R.drawable.imgdiacalendario),
            contentDescription = "",
            modifier = Modifier
                .size(300.dp)
        )
    }
}



//package com.appclass.myapplication.screens
//
//import android.util.Log
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//
//@Composable
//fun DiaCitas(dia: Int) {
//    val db = FirebaseFirestore.getInstance()
//    val coleccion = "citas"
//    val auth = FirebaseAuth.getInstance()
//    var nombre by remember { mutableStateOf("") }
//    var medico by remember { mutableStateOf("") }
//    var hora by remember { mutableStateOf("") }
//    var mensaje by remember { mutableStateOf("") }
//    var citas by remember { mutableStateOf<List<Citas>>(emptyList()) }
//    var editada by remember { mutableStateOf(false) }
//    var editandoCitaId by remember { mutableStateOf<String?>(null) }
//
//    // Recuperar las citas de Firestore cuando el día cambia
//    LaunchedEffect(dia) {
//        cargarCitas(dia, db, coleccion) { listaCitas ->
//            citas = listaCitas
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//    ) {
//
//        Text(text = "Día $dia", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Campos de texto para ingresar los detalles de la cita
//        OutlinedTextField(
//            value = nombre,
//            onValueChange = { nombre = it },
//            label = { Text("Tipo de Cita") }
//        )
//
//        OutlinedTextField(
//            value = medico,
//            onValueChange = { medico = it },
//            label = { Text("Nombre del Médico") }
//        )
//
//        OutlinedTextField(
//            value = hora,
//            onValueChange = { hora = it },
//            label = { Text("Hora (HH:mm)") }
//        )
//
//        Spacer(modifier = Modifier.size(5.dp))
//
//        // Botón para añadir o editar cita
//        Button(
//            onClick = {
//                if (nombre.isBlank() || medico.isBlank() || hora.isBlank()) {
//                    mensaje = "Todos los campos deben estar completos."
//                    return@Button
//                }
//
//                if (!hora.matches(Regex("\\d{2}:\\d{2}"))) {
//                    mensaje = "Formato de hora inválido. Usa HH:mm."
//                    return@Button
//                }
//
//                val horaNueva = try {
//                    hora.split(":").let { parts ->
//                        parts[0].toInt() * 60 + parts[1].toInt()
//                    }
//                } catch (e: Exception) {
//                    mensaje = "Formato de hora no válido. Usa HH:mm."
//                    return@Button
//                }
//
//                val conflicto = citas.any { cita ->
//                    val horaExistente = cita.hora.split(":").let { parts ->
//                        parts[0].toInt() * 60 + parts[1].toInt()
//                    }
//                    val diferencia = kotlin.math.abs(horaNueva - horaExistente)
//                    diferencia < 60
//                }
//
//                if (conflicto) {
//                    mensaje = "Conflicto de horarios: debe haber al menos una hora entre citas."
//                    return@Button
//                }
//
//                val userUid = auth.currentUser?.uid
//                if (userUid == null) {
//                    mensaje = "Usuario no autenticado. No se puede guardar la cita."
//                    return@Button
//                }
//
//                val datos = hashMapOf(
//                    "nombre" to nombre,
//                    "medico" to medico,
//                    "dia" to String.format("%02d", dia),
//                    "hora" to hora,
//                    "usuario" to userUid
//                )
//
//                if (editada && !editandoCitaId.isNullOrEmpty()) {
//                    // Modo edición: actualizar cita existente
//                    db.collection(coleccion)
//                        .document(editandoCitaId!!)
//                        .update(datos as Map<String, Any>)
//                        .addOnSuccessListener {
//                            mensaje = "Cita actualizada correctamente"
//                            limpiarFormulario()
//                            cargarCitas(dia, db, coleccion) { listaCitas ->
//                                citas = listaCitas
//                            }
//                        }
//                        .addOnFailureListener { e ->
//                            mensaje = "Error al actualizar la cita: ${e.localizedMessage}"
//                            Log.e("FirebaseError", "Error al actualizar cita", e)
//                        }
//                } else {
//                    // Modo agregar: crear nueva cita
//                    // Después de añadir una cita correctamente
//                    db.collection(coleccion)
//                        .document()
//                        .set(datos)
//                        .addOnSuccessListener {
//                            mensaje = "Cita guardada correctamente"
//                            limpiarFormulario()
//                            cargarCitas(dia, db, coleccion) { listaCitas ->
//                                citas = listaCitas
//                            }
//                        }
//                        .addOnFailureListener { e ->
//                            mensaje = "Error al guardar la cita: ${e.localizedMessage}"
//                            Log.e("FirebaseError", "Error al guardar cita", e)
//                        }
//                }
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
//            border = BorderStroke(1.dp, Color.Black)
//        ) {
//            Text(text = if (editada) "Actualizar Cita" else "Añadir Cita")
//        }
//
//        Spacer(modifier = Modifier.size(8.dp))
//        Text(text = mensaje)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Mostrar citas del día
//        citas.forEach { cita ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 4.dp)
//                    .clickable {
//                        editada = true
//                        editandoCitaId = cita.id
//                        nombre = cita.nombre
//                        medico = cita.medico
//                        hora = cita.hora
//                    },
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(text = "Tipo de Cita: ${cita.nombre}", style = MaterialTheme.typography.bodyLarge)
//                    Text(text = "Médico: ${cita.medico}", style = MaterialTheme.typography.bodyLarge)
//                    Text(text = "Hora: ${cita.hora}", style = MaterialTheme.typography.bodyLarge)
//                }
//            }
//        }
//    }
//}
//
//// Función que carga las citas de un día específico desde Firestore
//fun cargarCitas(dia: Int, db: FirebaseFirestore, coleccion: String, onCitasLoaded: (List<Citas>) -> Unit) {
//    db.collection(coleccion)
//        .whereEqualTo("dia", String.format("%02d", dia))
//        .get()
//        .addOnSuccessListener { documents ->
//            val citas = documents.mapNotNull { doc ->
//                doc.toObject(Citas::class.java).apply { id = doc.id }
//            }
//            onCitasLoaded(citas)
//        }
//        .addOnFailureListener { e ->
//            Log.e("FirebaseError", "Error al cargar citas", e)
//        }
//}
//
//// Función para limpiar los campos del formulario
//fun limpiarFormulario() {
//    var nombre = ""
//    var medico = ""
//    var hora = ""
//    var editada = false
//    var editandoCitaId = null
//}
