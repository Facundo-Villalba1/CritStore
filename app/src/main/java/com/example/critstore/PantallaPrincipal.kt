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

@Composable
fun PantallaPrincipal(
    productos: () -> Unit,
    ventas: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val anchoPantalla = configuration.screenWidthDp
    val esTablet = anchoPantalla >= 600
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(if (esTablet) 48.dp else 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_critstore),
            contentDescription = "Logo CritStore",
            modifier = Modifier
                .fillMaxWidth(if (esTablet) 0.25f else 0.50f)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(if (esTablet) 32.dp else 20.dp))
        Text(
            text = "CritStore",
            style = if (esTablet)
                MaterialTheme.typography.displaySmall
            else
                MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(30.dp))
        if (esTablet) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    onClick = productos,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("📦 Productos")
                }
                Button(
                    onClick = ventas,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("💰 Ventas")
                }
            }
        } else {
            Button(
                onClick = productos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("📦 Productos")
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = ventas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("💰 Ventas")
            }
        }
    }
}