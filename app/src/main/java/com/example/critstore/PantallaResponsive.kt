package com.example.critstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaCelular(
    productos: () -> Unit,
    ventas: () -> Unit,
    Reportes: () -> Unit
) {
    val configuracion =
        LocalConfiguration.current
    val esTablet =
        configuracion.screenWidthDp >= 600
    val padding =
        if(esTablet)
            40.dp
        else
            20.dp
    val tamañoLogo =
        if(esTablet)
            220.dp
        else
            140.dp
    val alturaBoton =
        if(esTablet)
            70.dp
        else
            55.dp
    val tamañoTexto =
        if(esTablet)
            22.sp
        else
            18.sp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.logo_critstore
            ),
            contentDescription =
                "Logo CritStore",
            modifier =
                Modifier.size(
                    tamañoLogo
                )
        )
        Spacer(
            Modifier.height(20.dp)
        )
        Text(
            text = "CritStore",
            style =
                MaterialTheme.typography.headlineLarge
        )
        Spacer(
            Modifier.height(40.dp)
        )
        Button(
            onClick = productos,
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    alturaBoton
                )
        ){
            Text(
                "📦 Productos",
                fontSize =
                    tamañoTexto
            )
        }
        Spacer(
            Modifier.height(20.dp)
        )
        Button(
            onClick = ventas,
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    alturaBoton
                )
        ){
            Text(
                "🛒 Ventas",
                fontSize =
                    tamañoTexto
            )
        }
        Spacer(
            Modifier.height(20.dp)
        )
        Button(
            onClick =Reportes,
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    alturaBoton
                )
        ){
            Text(
                text = "📊  Reportes",
                fontSize = tamañoTexto
            )
        }
    }
}