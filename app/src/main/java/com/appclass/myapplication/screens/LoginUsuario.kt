package com.appclass.pruebasautentificacion.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.appclass.myapplication.R
//import com.appclass.pruebasautentificacion.R
import com.google.firebase.Firebase

import com.google.firebase.auth.FirebaseAuth

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
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(100.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        //BOTON PARA USUARIOS CON UNA CUENTA YA CREADA
        Button(
            onClick = {
                loginWithEmailAndPassword(email, password, onLoginSuccess) { errorMsg ->
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

        //Línea horizontal, como el <hr> en html
        Divider(
            color = Color.Gray,  // Color de la línea
            thickness = 1.dp     // Grosor de la línea
        )
        Text(
            text = "OR"
        )

        //quedaria bonito poner el "OR" entre dos dividers :)


        //llamada al metodo del btn de google, está mas abajo
        BtnGoogle(modifier = Modifier)
    }
}

/*esta funcion va a manejar el inicio de sesion usando firebaseAuth,
es la que realmente va a implementar el Metodo Autentificacion*/
fun loginWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit, onError: (String)->Unit) {

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener{task->
            if (task.isSuccessful){
                onSuccess()
            }else{
                onError(task.exception?.message?:"Login failed")
            }
        }

}

/*funcion para crear un nuevo usuario y guardarlo en la BD*/
fun registerWithEmailAndPassword(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit){

    //añadimos la logica de -> si no estan vacios los campos, q me cree la cuentaa nueva
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
fun BtnGoogle(modifier: Modifier = Modifier){
    Button(
        onClick = {/*todo*/},
        modifier = Modifier
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = "logo google",
            modifier = Modifier
                .clip(CircleShape) //esto lo q hace es recortar las esquinas, en este caso en redondo :)

                //tamaño de la imagen
                .width(30.dp)
                .height(30.dp)

        )
        Text(
            text = "Iniciar Sesión con Google",
            color = Color.White,
            modifier = Modifier
                .padding(start = 15.dp)
        )
    }
}

@Composable
fun MainScreen(){
    val auth = FirebaseAuth.getInstance()
    val isUserLoggedIn = auth.currentUser != null

    if(isUserLoggedIn){
        //HomeScreen()
        println("the user has been logged successfuly")
    }else{
        LoginScreen(onLoginSuccess = {/*redirige al Homescreen*/})
        println("estas en la segunda parte del if de la funcion mainScreen")
    }
}


@Composable
fun LlamadaFinal(navController: NavController, modifier: Modifier = Modifier){
    //llamada :)
    LoginScreen(onLoginSuccess = {//solo llamamos a esta funcion q es la q tiene algo q mostrar al usuario
        //redirigir al usuario a otra pantalla
        navController.navigate("LoginSuccess")
    })
}
