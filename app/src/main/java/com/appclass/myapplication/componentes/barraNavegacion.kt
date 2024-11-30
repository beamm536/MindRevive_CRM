package com.appclass.myapplication.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBarComponent(navController: NavController) {
    // Define los elementos de navegación aquí para poder reutilizar el componente
    val items = listOf("home", "citas", "formulario", "perfil")
    val selectedRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = Color(0xFFE6E6FA)
    ) {
        items.forEach { item ->
            val isSelected = item == selectedRoute
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (item) {
                            "home" -> Icons.Default.Home
                            "citas" -> Icons.Default.Call
                            "form" -> Icons.Default.Edit
                            "perfil" -> Icons.Default.Person
                            else -> Icons.Default.Home
                        },
                        contentDescription = item,
                        modifier = Modifier.size(if (isSelected) 30.dp else 24.dp)
                    )
                },
                label = {
                    Text(
                        text = when (item) {
                            "home" -> "Home"
                            "citas" -> "Citas"
                            "formulario" -> "Formulario"
                            "perfil" -> "Perfil"
                            else -> "-"
                        },
                        fontSize = if (isSelected) 12.sp else 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = isSelected,
                onClick = {
                    if (selectedRoute != item) {
                        navController.navigate(item) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
/*
@Composable
fun BottomNavigationBarComponent(navController: NavController, formEnviadoHoyState: Boolean) {
    // Define los elementos de navegación aquí para poder reutilizar el componente
    val items = listOf("home", "citas", "formulario", "perfil")
    val selectedRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = Color(0xFFE6E6FA)
    ) {
        items.forEach { item ->
            val isSelected = item == selectedRoute
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (item) {
                            "home" -> Icons.Default.Home
                            "citas" -> Icons.Default.Call
                            "formulario" -> Icons.Default.Edit
                            "perfil" -> Icons.Default.Person
                            else -> Icons.Default.Home
                        },
                        contentDescription = item,
                        modifier = Modifier.size(if (isSelected) 30.dp else 24.dp)
                    )
                },
                label = {
                    Text(
                        text = when (item) {
                            "home" -> "Home"
                            "citas" -> "Citas"
                            "formulario" -> if (formEnviadoHoyState) "Gráficos" else "Formulario"
                            "perfil" -> "Perfil"
                            else -> ""
                        },
                        fontSize = if (isSelected) 12.sp else 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = isSelected,
                onClick = {
                    if (selectedRoute != item) {
                        navController.navigate(item) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}*/


