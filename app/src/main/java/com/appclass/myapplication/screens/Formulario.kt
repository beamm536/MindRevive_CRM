package com.appclass.myapplication.screens

data class Formulario(
    val fecha: String,
    val estadoAnimo: Int, // Botones de caras que representan(1 a 5)
    val motivacion: Int, // Barra deslizante o botones de selección (1 a 5)
    val trabajo: Int, // Horas de trabajo (0-24)
    val descanso: Int, // Horas de descanso (0-24)
    val ejercicio: Int, // Horas de ejercicio (0-24)
    val social: Int, // Horas de actividad social (0-24)
    val hobbies: Int, // Horas de hobbies (0-24)
    val tiempoClima: String, // Tipo de clima seleccionado (soleado, lluvioso, etc.) proveniente de array list
    val logros: String, // Campo de texto para logros del día
    val cuidadoPersonal: String, // Campo de texto para registrar actividades de autocuidado
    val emocionesPredominantes: List<String>, // Array de emociones predominantes con opciones
    val pensamientosNegativos: String, // Campo de texto para pensamientos negativos
    val nivelAnsiedad: Int, // Barra deslizante o botones de selección (1 a 5)
    val calidadSueno: String, // Barra deslizante o botones de selección (1 a 5)
    val agradecimientos: String, // Campo de texto para agradecimientos
    val intensidadAutocritica: Int, // Barra deslizante o botones de selección (1 a 5)
    val expectativaManana: String, // Campo de texto para expectativa para el día siguiente
    val otrosComentarios: String, // Campo de texto para comentarios adicionales
    val notaGlobal: Int // Barra deslizante o botones de selección (1 a 10)
)

