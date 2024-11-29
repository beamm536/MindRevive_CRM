package com.appclass.myapplication.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.foundation.Image as Image1

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

    var opcionSeleccionada by remember { mutableStateOf("Hombre") }//para los radio button


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
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .fillMaxHeight(0.7f) ,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (datosUser != null) { //SIEMPRE Y CUANDO HAYA DATOS - SE MOSTRARÁN


            ////AQUI ES DND QUIERO CAMBIAR LA IMG
                item {

                    MostrarImagenPerfil(opcionSeleccionada)

                /*Image1(
                    painter = painterResource(id = R.drawable.profile_user),
                    contentDescription = "",
                    modifier = Modifier
                        .size(110.dp)
                )*/




                //He añadido para q quede mejor visualmente, q la primera se ponga en mayusculas
                Text(
                    text = datosUser["nombre"]?.toString()?.replaceFirstChar { it.uppercaseChar() } ?: "Usuario",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                }

                item {
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
                }
                item {
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
                }
                item {
                OutlinedTextField(
                    value = edad/*datosUser["edad"]?.toString() ?: "No disponible"*/,
                    onValueChange = { edad = it }, // No hacemos nada ya que es solo para mostrar
                    label = { Text("Edad") },
                    shape = RoundedCornerShape(16.dp),
                    colors = outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = MoradoTextFields,
                        cursorColor = MoradoTextFields
                    )
                )
                }
                item {
                OutlinedTextField(
                    value = datosUser["email"]?.toString() ?: "No disponible", //en caso de q no haya ningun valor q salga no disponible
                    onValueChange = {},
                    label = { Text("Email") },
                    shape = RoundedCornerShape(16.dp),
                    colors = outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = MoradoTextFields,
                        cursorColor = MoradoTextFields,
                        disabledLabelColor = GrisDisabled,
                        disabledBorderColor = GrisDisabled
                    ),
                    readOnly = true,
                    enabled = false
                )
                }
            item{
                RadioButtonGenero{seleccion ->
                    opcionSeleccionada = seleccion
                }
            }

//he quitado el drop down al final :/    ---> seria la llamada a los radio button
//                item {
//                DropDownGenero()
//                }

        } else if (error != null) {
            item {
            // Mostrar un mensaje de error si falla la consulta
                Text("Error: $error", color = Color.Red, fontSize = 16.sp)
            }
        } else {
            item {
            //mostrador de carga de los datos mientras esperamos, es una funcion propia de kotlin
            CircularProgressIndicator()
            }
        }

            item {
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
    Icon(
        imageVector = Icons.Default.ArrowDropDown, // Flecha hacia abajo predeterminada
        contentDescription = "Flecha hacia abajo",
        tint = Color.Black, // Cambia el color si es necesario
        modifier = Modifier.size(24.dp)
    )

}


//----------FALTA LA ADAPTACION Y PONERLO EN UN ROW Y AÑADIRLE BIEN LA LOGICA 

@Composable
fun RadioButtonGenero(seleccion: (String) -> Unit) {

    var opcionSeleccionada by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Selecciona una opción")


        Column(modifier = Modifier.padding(top = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                        selected = opcionSeleccionada == "Hombre",
                    onClick = {
                        opcionSeleccionada = "Hombre"
                        seleccion(opcionSeleccionada)
                    }
                )
                Text(text = "Hombre", modifier = Modifier.padding(start = 8.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = opcionSeleccionada == "Mujer",
                    onClick = {
                        opcionSeleccionada = "Mujer"
                        seleccion(opcionSeleccionada)
                    }
                )
                Text(text = "Mujer", modifier = Modifier.padding(start = 8.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = opcionSeleccionada == "Otro",
                    onClick = {
                        opcionSeleccionada = "Otro"
                        seleccion(opcionSeleccionada)
                    }
                )
                Text(text = "Otro", modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        //segun la opcion seleccionada q salga una imagen u otraa ---> esta parte de la logica la tengo en otra funcion
    }
}

@Composable
fun MostrarImagenPerfil(opcion: String) {
    when (opcion) {
        "Hombre" -> Image1(
            painter = painterResource(id = R.drawable.img_male_avatar),
            contentDescription = "Imagen Hombre",
            modifier = Modifier.size(200.dp)
        )
        "Mujer" -> Image1(
            painter = painterResource(id = R.drawable.profile_user),
            contentDescription = "Imagen Mujer",
            modifier = Modifier.size(200.dp)
        )
        "Otro" -> Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Img3",
                tint = Color.Gray,
                modifier = Modifier.size(200.dp)
            )
    }
}*/


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
        //IconoPantallaPerfil(modifier)
    }

}


