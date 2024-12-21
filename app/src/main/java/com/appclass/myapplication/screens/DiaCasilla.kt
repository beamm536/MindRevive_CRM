package com.appclass.myapplication.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController
import com.appclass.myapplication.ui.theme.MoradoTextFields

@Composable
fun DiaCasilla(dia: Int, diaActual: Boolean, tieneCita: Boolean, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(8.dp))

        // Contenedor principal de Día
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Caja que muestra el número del día. Se resalta si es el día actual.
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (diaActual) MoradoTextFields else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Texto con el número del día, blanco si es el día actual, negro si no lo es.
                Text(
                    text = String.format("%02d", dia),
                    color = if (diaActual) Color.White else Color.Black,
                    fontSize = 16.sp
                )
            }

            // Si el día tiene citas, se muestra un punto rojo debajo del número del día
            if (tieneCita) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
