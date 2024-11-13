package com.appclass.myapplication.screens

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
                    IconButton(onClick = { /* todo */ }) {
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
fun CamposRegistroUsuario(modifier: Modifier = Modifier){

    //DECLARACION DE LAS VARIABLES
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //variable pra la contraseña visible
    var passVisible by remember { mutableStateOf(false) } //q no vea al principio
    var passVisible2 by remember { mutableStateOf(false) }

    //DECLARACION DE LAS BD A USAR
    var auth = FirebaseAuth.getInstance()
    var dbFirestore = FirebaseFirestore.getInstance()



    //CAMPOS FORMULARIO
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        // EMAIL
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            shape = RoundedCornerShape(16.dp),
            colors = outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = MoradoTextFields,
                cursorColor = MoradoTextFields
            )
        )

        // CONTRASEÑA
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
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
                val iconoVisibilidad = if (passVisible) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                IconButton(onClick = { passVisible = !passVisible }) {
                    Icon(imageVector = iconoVisibilidad, contentDescription = if (passVisible) "Ocultar contraseña" else "Mostrar contraseña")
                }
            }
        )

        // CONFIRMAR CONTRASEÑA
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
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
                val iconoVisibilidad = if (passVisible2) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                IconButton(onClick = { passVisible2 = !passVisible2 }) {
                    Icon(imageVector = iconoVisibilidad, contentDescription = if (passVisible2) "Ocultar contraseña" else "Mostrar contraseña")
                }
            }
        )


        //para q funcione y no de error el txt dentro del btn
        var errorMessage by remember { mutableStateOf<String?>(null) }

        // BTN CREAR CUENTA
        Button(
            onClick = {
                OnclickBtnRegistrar(
                    nombre = nombre,
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
}


fun OnclickBtnRegistrar(
    nombre: String,
    email: String,
    password: String,
    confirmPassword: String,
    onError: (String) -> Unit
){

    //variable pra la contraseña visible
    //var passVisible by remember { mutableStateOf(false) } //q no vea al principio
    //var passVisible2 by remember { mutableStateOf(false) }

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
                        val datosUser = User(uid = it.uid, nombre = nombre, email = email)

                        dbFirestore.collection("usuariosCRM").document(it.uid).set(datosUser)
                            .addOnSuccessListener {
                                // Éxito al guardar en Firestore
                            }
                            .addOnFailureListener {
                                // Manejo de errores al guardar en Firestore
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
fun Final(navController: NavController, modifier: Modifier = Modifier){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.size(100.dp))
        CamposRegistroUsuario(modifier)
    }
}
