package com.appclass.myapplication.screens

import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Visibility
import androidx.navigation.NavController
import com.appclass.myapplication.ui.theme.MarronBtns
import com.appclass.myapplication.ui.theme.MoradoTextFields

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class) //api experimental de compos
@Composable
fun RegistroUsuario(navController: NavController){

    Scaffold (

        topBar = {
            CenterAlignedTopAppBar(
                /*colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor =  Color(0xFF7ab9b4),
                    titleContentColor = Color(0xFF563d23),/*0xFF003B3B*/
                ),*/
                title ={ /*titulo vacio*/ },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("InicioAppCRM")
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor =  Color(0xFF506d75),
                contentColor = Color.White,
                modifier = Modifier
                    .height(40.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "aqui poner alguna decoracion o algo",
                )
            }
        },
        modifier = Modifier.fillMaxSize()) { innerPadding ->
        Final(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CamposRegistroUsuario(navController: NavController ,modifier: Modifier = Modifier){

    //DECLARACION DE LAS VARIABLES
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }//NO PONERLO CON COMILLAS SI ES UN INTTTT
    var email by remember { mutableStateOf("") }
    var emailInvalido by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //para q funcione y no de error el txt dentro del btn
    var errorMessage by remember { mutableStateOf<String?>(null) }

    //variable pra la contraseña visible   ||  y para mostrar un error en caso de q la la constraseña sea menor a 6 caracteres  || y para q sean iguales
    var passVisible by remember { mutableStateOf(false) } //q no vea al principio
    var passVisible2 by remember { mutableStateOf(false) }
    var mostrarErrorNumCaracteres by remember { mutableStateOf(false) }
    var mostrarErrorNoCoinciden by remember { mutableStateOf(false) }



    //variable Toast
    val contextoApp = LocalContext.current

    //DECLARACION DE LAS BD A USAR
    var auth = FirebaseAuth.getInstance()
    var dbFirestore = FirebaseFirestore.getInstance()


    //CAMPOS FORMULARIO
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // NOMBRE
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                )
            )
        }

        item {
            // APELLIDOS
            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
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
            // EDAD
            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it },
                label = { Text("Edad") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                )
            )
//Patterns.EMAIL_ADDRESS.matcher(mail).matches()
        }

        item {
            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailInvalido = !ValidacionEmail(email)
                },
                label = { Text("Email") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                isError = emailInvalido
            )

            if (emailInvalido) {//mensajito en rojo por debajo del input
                Text(
                    text = "El email debe contener '@' y terminar en 'gmail.com'",
                    color = Color.Red,
                    fontSize = 12.sp
                )
            }
        }

        item {
            // CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = {
                                //comprobacion de que el parámetro contraseña tenga min 6 caract
                                  password = it
                                  mostrarErrorNumCaracteres = password.length < 6
                                  mostrarErrorNoCoinciden = password != confirmPassword
                                },
                label = { Text("Contraseña") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                ),
                visualTransformation = if (passVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                //visualTransformation = PasswordVisualTransformation() ---> en dudas/apuntesRegistro

                trailingIcon = {
                    val iconoVisibilidad =
                        if (passVisible) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                    IconButton(onClick = {
                        passVisible = !passVisible
                    }) { //esto es lo q hace q se vaya cambiando la visibilidad
                        Icon(
                            imageVector = iconoVisibilidad,
                            contentDescription = if (passVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                isError = mostrarErrorNumCaracteres  && mostrarErrorNoCoinciden
                //el campo del field lo va mostrar en rojo si no se cumple
                //y si las contraseñas no son iguales tmb en rojo
            )

            ErrorPasswordNumCaracteres(mostrarErrorNumCaracteres)
            ErrPasswordNoCoinciden(mostrarErrorNoCoinciden)

        }

        item {
            // CONFIRMAR CONTRASEÑA
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                                  confirmPassword = it
                                  mostrarErrorNumCaracteres = password.length < 6
                                  mostrarErrorNoCoinciden = password != confirmPassword
                                },
                label = { Text("Confirmar Contraseña") },
                shape = RoundedCornerShape(16.dp),
                colors = outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = MoradoTextFields,
                    cursorColor = MoradoTextFields
                ),
                visualTransformation = if (passVisible2) VisualTransformation.None
                else PasswordVisualTransformation(),

                trailingIcon = {
                    val iconoVisibilidad = if (passVisible2) Icons.Filled.Favorite
                    else Icons.Filled.FavoriteBorder

                    IconButton(onClick = { passVisible2 = !passVisible2 }) {
                        Icon(
                            imageVector = iconoVisibilidad,
                            contentDescription = if (passVisible2) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                isError = mostrarErrorNumCaracteres  && mostrarErrorNoCoinciden
                        //el campo del field lo va mostrar en rojo si no se cumple
                        //y si las contraseñas no son iguales tmb en rojo

            )

            ErrorPasswordNumCaracteres(mostrarErrorNumCaracteres)
            ErrPasswordNoCoinciden(mostrarErrorNoCoinciden)
        }


        item{
            // BTN CREAR CUENTA
            Button(
                onClick = {
                    //mensajito para el email invalido
                    if (emailInvalido) {
                        Toast.makeText(contextoApp, "Email inválido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    //TOAST PARA MOSTRAR MENSAJE DE Q EL USUARIO SE HA REGISTRADO --> explicado en apuntesRegistro
                    Toast.makeText(
                        contextoApp,
                        "El usuario se ha registrado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    //REDIRECCION DENTRO DE LA APP
                    navController.navigate("pantallaInicio")

                    OnclickBtnRegistrar(
                        nombre = nombre,
                        apellidos = apellidos,
                        edad = edad,
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                        onError = { message -> errorMessage = message }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MarronBtns)
            ) {
                Text("Create an account")
            }
        }

        item{
            Text(
                text = "Ya tengo cuenta, Log-in",
                color = Color.Blue,
                fontSize = 18.sp,
                modifier = Modifier.clickable {
                    navController.navigate("loginUsuario")
                }
            )
        }
    }
}



//ESTA FUNCION NO ES COMPOSABLE!!
fun OnclickBtnRegistrar(
    nombre: String,
    apellidos: String,
    edad: String,
    email: String,
    password: String,
    confirmPassword: String,
    onError: (String) -> Unit
){


    //DECLARACION DE LAS BD A USAR
    var auth = FirebaseAuth.getInstance()
    var dbFirestore = FirebaseFirestore.getInstance()


    if (password == confirmPassword && nombre.isNotEmpty() && email.isNotEmpty()) {
        auth.createUserWithEmailAndPassword(email, password)//crea al usuario y deberia guardarlo en firebaseAUTH



            //segun el resultado q nos ha dado el registro, es decir si se ha cumplido la condicion de los campos
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { //si ha sido exitoso

                    var user = auth.currentUser
                    user?.let {
                        //parametros ordenados
                        val datosUser = User(
                            uid = it.uid,
                            nombre = nombre,
                            apellidos = apellidos,
                            edad = edad,
                            email = email
                        )

                        dbFirestore.collection("usuariosCRM").document(it.uid).set(datosUser)
                            .addOnSuccessListener {
                                // Éxito al guardar en Firestore
                                Log.d("Registro", "Usuario guardado exitosamente en Firestore")
                            }
                            .addOnFailureListener {
                                // Manejo de errores al guardar en Firestore
                                Log.e("Registro", "Error al guardar usuario en Firestore: ${it.message}")
                            }
                    }


                } else {
                    // Manejo de errores al crear el usuario en Auth
                    onError ("usurio no autenticado")
                }
            }
    } else {
        // Mostrar mensaje de error si las contraseñas no coinciden o si los campos están vacíos
        onError ("Las contraseñas no coinciden o hay campos vacíos")
    }
}

@Composable
fun ErrorPasswordNumCaracteres(
    mostrarErrorNumCaracteres: Boolean
){

    if (mostrarErrorNumCaracteres) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = "La contraseña debe tener al menos 6 caracteres",
                color = Color.Red
            )
        }
    }
}

@Composable
fun ErrPasswordNoCoinciden(
    mostrarErrorNoCoinciden: Boolean
){
    if (mostrarErrorNoCoinciden) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = "Las contraseñas no coinciden",
                color = Color.Red
            )
        }
    }
}

//para q no podamos registrar un email como si fuera un nombre por ejemplo
fun ValidacionEmail(email:String): Boolean{ //va a devolver un boolean
    return email.contains("@") && email.endsWith("gmail.com")
}


@Composable
fun Final(navController: NavController, modifier: Modifier = Modifier){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.size(100.dp))
        CamposRegistroUsuario( navController,modifier)
    }
}
