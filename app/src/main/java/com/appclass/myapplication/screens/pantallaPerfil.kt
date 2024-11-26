package com.appclass.myapplication.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.ui.theme.GrisDisabled
import com.appclass.myapplication.ui.theme.MoradoTextFields
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(navController: NavController){

    //variable del toast
    val contextoLogOut = LocalContext.current

    Scaffold (

        topBar = {
            CenterAlignedTopAppBar(
                /*colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor =  Color(0xFF7ab9b4),
                    titleContentColor = Color(0xFF563d23),/*0xFF003B3B*/
                ),*/
                title ={
                    Text(
                        text = "Perfil",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                },
                actions = {
                    // Botón de logout
                    IconButton(onClick = {
                        val auth = FirebaseAuth.getInstance()
                        auth.signOut()

                        Toast.makeText(contextoLogOut, "Has cerrado sesión", Toast.LENGTH_SHORT).show()

                        navController.navigate("inicioAppCRM") {
                            popUpTo("inicioAppCRM") { inclusive = true }
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }

            )
        },
        bottomBar = {
            BottomNavigationBarComponent(navController = navController)
        },
        modifier = Modifier.fillMaxSize()) { innerPadding ->
        LlamadaFunciones3(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostrarDatosUsuario(){
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()

    //estados para guardar los datos del usuario
    val (datosUser, setdatosUser) = remember { mutableStateOf<Map<String, Any>?>(null) }
    val (error, setError) = remember { mutableStateOf<String?>(null) }

    //----------------------------PARTE DE LA LOGICA PARA MOSTRAR-----------------------------------
    //solo ejecuta la consulta si el usuario está autenticado - por lo tanto muestra un estado de cargando
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            db.collection("usuariosCRM").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        setdatosUser(document.data) // Guardamos los datos en el estado
                    } else {
                        setError("No se encontraron datos para el usuario.")
                    }
                }
                .addOnFailureListener { exception ->
                    setError("Error al obtener los datos: ${exception.message}")
                }
        }
    }



    //-------------------------PARTE VISUAL DE LOS DATOS--------------------------------------------
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (datosUser != null) { //SIEMPRE Y CUANDO HAYA DATOS - SE MOSTRARÁN

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier

            ){
                /*Text(
                    text = "Perfil",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )*/

            }

            //mostramos los datos del usuario en los inputs
            OutlinedTextField(
                value = datosUser["nombre"]?.toString() ?: "Usuario",
                onValueChange = {}, // No hacemos nada ya que es solo para mostrar
                label = { Text("Nombre") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                )
            )
            OutlinedTextField(
                value = datosUser["apellidos"]?.toString() ?: "No disponible",
                onValueChange = {}, // No hacemos nada ya que es solo para mostrar
                label = { Text("Apellidos") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                )
            )
            OutlinedTextField(
                value = datosUser["email"]?.toString() ?: "No disponible", //en caso de q no haya ningun valor q salga no disponible
                onValueChange = {},
                label = { Text("Email") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields,
                    disabledLabelColor = GrisDisabled, // Color de la etiqueta cuando está deshabilitado
                    disabledBorderColor = GrisDisabled
                ),
                readOnly = true,
                enabled = false
            )

        } else if (error != null) {
            // Mostrar un mensaje de error si falla la consulta
            Text("Error: $error", color = Color.Red, fontSize = 16.sp)
        } else {
            //mostrador de carga de los datos mientras esperamos, es una funcion propia de kotlin
            CircularProgressIndicator()
        }
    }

}




@Composable
fun LlamadaFunciones3(navController: NavController, modifier: Modifier){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.size(100.dp))
        MostrarDatosUsuario()
    }

}


