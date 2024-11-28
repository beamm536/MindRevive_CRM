package com.appclass.myapplication.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.appclass.myapplication.R
import com.appclass.myapplication.componentes.BottomNavigationBarComponent
import com.appclass.myapplication.ui.theme.GrisDisabled
import com.appclass.myapplication.ui.theme.MoradoTextFields
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(navController: NavHostController){

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

    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") } // Solo para mostrar, no editable
    val contexto = LocalContext.current



    //estados para guardar los datos del usuario
    val (datosUser, setdatosUser) = remember { mutableStateOf<Map<String, Any>?>(null) }
    val (error, setError) = remember { mutableStateOf<String?>(null) }

    var isFocused by remember { mutableStateOf(false) }

    //----------------------------PARTE DE LA LOGICA PARA MOSTRAR-----------------------------------
    //solo ejecuta la consulta si el usuario está autenticado - por lo tanto muestra un estado de cargando
    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            db.collection("usuariosCRM").document(uid).get()
                .addOnSuccessListener { document ->

                    /*si pilla el usuario, y la operacion ha tenido exito, que muestre los datos guardados en la base de datos,
                    --> la parte de las variables dentro del if, anteriormente las tenia cada una de ellas en su input correspondiente,

                     */

                    if (document.exists()) {
                        setdatosUser(document.data) // Guardamos los datos en el estado
                        nombre = document.getString("nombre") ?: ""
                        apellidos = document.getString("apellidos") ?: ""
                        edad = document.get("edad")?.toString() ?: ""
                        email = document.getString("email") ?: ""

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

            /*Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier

            ){*/

                /*Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Usuario",
                    tint = Color.Gray,
                    modifier = Modifier.size(130.dp)
                )*/
            Image(
                painter = painterResource(id = R.drawable.profile_user),
                contentDescription = "",
                modifier = Modifier
                    .size(110.dp)
            )

                //He añadido para q quede mejor visualmente, q la primera se ponga en mayusculas
                Text(
                    text = datosUser["nombre"]?.toString()?.replaceFirstChar { it.uppercaseChar() } ?: "Usuario",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )

           // }

            //mostramos los datos del usuario en los inputs
            OutlinedTextField(
                value = nombre/*datosUser["nombre"]?.toString() ?: "Usuario"*/,
                onValueChange = { nombre = it}, // No hacemos nada ya que es solo para mostrar
                label = { Text("Nombre") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Update",
                    )
                }
            )
            OutlinedTextField(
                value = apellidos/*datosUser["apellidos"]?.toString() ?: "No disponible"*/,
                onValueChange = { apellidos = it }, // No hacemos nada ya que es solo para mostrar
                label = { Text("Apellidos") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                )
            )
            OutlinedTextField(
                value = edad/*datosUser["edad"]?.toString() ?: "No disponible"*/,
                onValueChange = { edad }, // No hacemos nada ya que es solo para mostrar
                label = { Text("Edad") },
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
        Button(
            onClick = {
                val uid = currentUser?.uid
                if (uid != null) {
                    ModificarDatosUsuario(nombre, apellidos, edad, uid, db, contexto)
                } else {
                    Toast.makeText(contexto, "No se puede guardar: Usuario no autenticado.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Confirmar Cambios", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }



}

@Composable
fun IconoPantallaPerfil(modifier: Modifier){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.img_mobile_app_profile),
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
        )
    }
}



fun ModificarDatosUsuario(
    nombre: String,
    apellidos: String,
    edad: String,
    uid: String?,
    db: FirebaseFirestore,
    contexto: Context //esto es uno de los parametros q necesita el toast :)
){

    if (uid == null) {
        Toast.makeText(contexto, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        return
    }

    val datosActualizados = mapOf(
        "nombre" to nombre,
        "apellidos" to apellidos,
        "edad" to edad
    )

    db.collection("usuariosCRM").document(uid).update(datosActualizados)
        .addOnSuccessListener {
            Toast.makeText(contexto, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { exception ->
            Toast.makeText(
                contexto,
                "Error al actualizar datos: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
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
        //ElevatedCardExample()
        IconoPantallaPerfil(modifier)
    }

}


