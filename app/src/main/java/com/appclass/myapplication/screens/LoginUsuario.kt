package com.appclass.myapplication.screens
/*
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appclass.myapplication.R
import com.appclass.myapplication.ui.theme.MoradoTextFields
//import com.appclass.pruebasautentificacion.R
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

@Composable
fun LoginUsuario(navController: NavController){
    Scaffold (
        modifier = Modifier.fillMaxSize()) { innerPadding ->

        LlamadaFinal(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}


//ESTO ES PARA CUANDO YA TENEMOS UNA CUENTA, Y ACCEDEMOS A LA APP
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    var passVisible by remember { mutableStateOf(false) } //q no vea al principio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(100.dp))

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
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            shape = RoundedCornerShape(16.dp),
            colors = outlinedTextFieldColors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = MoradoTextFields,
                cursorColor = MoradoTextFields
            ),
            visualTransformation = if (passVisible) VisualTransformation.None
            else PasswordVisualTransformation(),

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

        )
        Spacer(modifier = Modifier.height(16.dp))

        //BOTON PARA USUARIOS CON UNA CUENTA YA CREADA
        Button(
            onClick = {
                loginWithEmailAndPassword(email, password, navController,onLoginSuccess) { errorMsg ->
                    error = errorMsg
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Text("Login")
        }
        error?.let {
            Text(it, color = Color.Red)
        }

        //BOTON PARA EL REGISTRO DE UN USUARIO NUEVO
        Button(
            onClick = {
                registerWithEmailAndPassword(email, password,
                    {
                        println("Registration successful")
                    }){
                        errorMsg ->
                    error = errorMsg
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Registrar")
        }

    }
}

/*esta funcion va a manejar el inicio de sesion usando firebaseAuth,
es la que realmente va a implementar el Metodo Autentificacion*/
fun loginWithEmailAndPassword(
    email: String,
    password: String,
    navController: NavController,
    onSuccess: () -> Unit,
    onError: (String)->Unit
) {

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener{task->
            if (task.isSuccessful){
                Log.d("Login", "Inicio de sesión exitoso")
                // Aquí podrías navegar directamente o confiar en onSuccess
                navController.navigate("pantallaInicio")
                onSuccess()
            }else{
                val errorMessage = when (task.exception) {
                    is FirebaseAuthInvalidUserException -> "Usuario no registrado."
                    is FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta."
                    else -> task.exception?.message ?: "Error desconocido."
                }
                Log.e("LoginError", errorMessage, task.exception)
                onError(errorMessage)
            }
        }

}

/*funcion para crear un nuevo usuario y guardarlo en la BD*/
fun registerWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit){

    //aÃ±adimos la logica de -> si no estan vacios los campos, q me cree la cuentaa nueva
    if(email.isNotEmpty() && password.isNotEmpty()){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task-> //con esto sabemos si la cuenta se ha creado bien
                if (task.isSuccessful){
                    onSuccess()
                }else{
                    onError(task.exception?.message?:"Registration failed")
                }
            }
    }else{
        println("complete todos los campos")
    }


}



@Composable
fun MainScreen(navController: NavController){
    val auth = FirebaseAuth.getInstance()
    val isUserLoggedIn = auth.currentUser != null

    if(isUserLoggedIn){
        //HomeScreen()
        println("the user has been logged successfuly")
    }else{
        LoginScreen(navController, onLoginSuccess = {/*redirige al Homescreen*/})
        println("estas en la segunda parte del if de la funcion mainScreen")
    }
}


@Composable
fun LlamadaFinal(navController: NavController, modifier: Modifier = Modifier){
    //llamada :)
    LoginScreen(navController = navController,onLoginSuccess = {//solo llamamos a esta funcion q es la q tiene algo q mostrar al usuario
        //redirigir al usuario a otra pantalla
        navController.navigate("pantallaInicio")
    })
}*/