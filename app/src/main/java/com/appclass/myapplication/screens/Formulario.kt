package com.appclass.myapplication.screens

import com.google.firebase.firestore.IgnoreExtraProperties

// La anotación @IgnoreExtraProperties es opcional, pero puede ser útil para que Firestore ignore campos extra
@IgnoreExtraProperties
data class Formulario(
    val fecha: String,
    val estadoAnimo: Int,
    val motivacion: Int,
    val trabajo: Int,
    val descanso: Int,
    val ejercicio: Int,
    val social: Int,
    val hobbies: Int,
    val tiempoClima: String,
    val logros: String,
    val cuidadoPersonal: String,
    val emocionesPredominantes: List<String>,
    val pensamientosNegativos: String,
    val nivelAnsiedad: Int,
    val calidadSueno: String,
    val agradecimientos: String,
    val intensidadAutocritica: Int,
    val expectativaManana: String,
    val otrosComentarios: String,
    val notaGlobal: Int
) {
    // Constructor vacío requerido por Firebase Firestore
    constructor() : this(
        fecha = "",
        estadoAnimo = 0,
        motivacion = 0,
        trabajo = 0,
        descanso = 0,
        ejercicio = 0,
        social = 0,
        hobbies = 0,
        tiempoClima = "",
        logros = "",
        cuidadoPersonal = "",
        emocionesPredominantes = emptyList(),
        pensamientosNegativos = "",
        nivelAnsiedad = 0,
        calidadSueno = "",
        agradecimientos = "",
        intensidadAutocritica = 0,
        expectativaManana = "",
        otrosComentarios = "",
        notaGlobal = 0
    )
}
