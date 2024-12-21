package com.appclass.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.appclass.myapplication.R
import com.appclass.myapplication.ui.theme.MarronBtns
import com.appclass.myapplication.ui.theme.NegroLetras

@Composable
fun InicioAppCRM(navController: NavController){
    Scaffold (
        modifier = Modifier.fillMaxSize()) { innerPadding ->


        LlamadaFunciones2(
            title =  stringResource(R.string.createAccount),
            navController = navController,
            modifier = Modifier.padding(innerPadding)

        )
    }
}

@Composable
fun TituloInicioAPP(modifier: Modifier = Modifier){
    Column(
        modifier = Modifier
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {

        Text(
            text = "Bienvenido!",
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            color = NegroLetras
        )
    }
}
@Composable
fun ImgInicioApp(modifier: Modifier){
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.padding(top = 48.dp))
        Image(
            painter = painterResource(id = R.drawable.iconoincio),
            contentDescription = "",
            modifier = Modifier
                .size(300.dp)
        )
    }
}

@Composable
fun BtnsInicio(modifier: Modifier = Modifier, title: String){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .border(2.dp, MarronBtns, CircleShape)
            .background(MarronBtns, shape = CircleShape)
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LlamadaFunciones2(title:String, navController: NavController, modifier: Modifier = Modifier){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TituloInicioAPP(modifier = Modifier)
        ImgInicioApp(modifier = Modifier)
        Spacer(modifier = Modifier.padding(top = 32.dp))
        BtnsInicio(
            modifier = Modifier.clickable {
                navController.navigate("registroUsuario")
            },
            title = stringResource(R.string.createAccount)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        BtnsInicio(
            modifier = Modifier.clickable {
                navController.navigate("loginUsuario")
            },
            title = stringResource(R.string.login)
        )
    }
}