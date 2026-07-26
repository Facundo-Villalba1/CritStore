package com.example.critstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.room.Room

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val db = remember {
                Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    "critstore.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            var pantalla by remember {
                mutableStateOf("inicio")
            }
            var planillaSeleccionada by remember {
                mutableStateOf(0)
            }
            var planillaActual by remember {
                mutableStateOf<PlanillaVenta?>(null)
            }
            when (pantalla) {
                "inicio" -> PantallaPrincipal(
                    productos = {
                        pantalla = "productos"
                    },
                    ventas = {
                        pantalla = "ventas"
                    }
                )
                "productos" -> PantallaProductos(
                    volver = {
                        pantalla = "inicio"
                    },
                    ingresarProducto = {
                        pantalla = "ingresarProducto"
                    },
                    verProductos = {
                        pantalla = "verProductos"
                    },
                    actualizarStock = {
                        pantalla = "actualizarStock"
                    }
                )
                "ingresarProducto" -> IngresarProducto(
                    productoDao = db.productoDao(),
                    volver = {
                        pantalla = "productos"
                    }
                )
                "verProductos" -> VerProductos(
                    productoDao = db.productoDao(),
                    volver = {
                        pantalla = "productos"
                    }
                )
                "actualizarStock" -> ActualizarStock(
                    productoDao = db.productoDao(),
                    volver = {
                        pantalla = "productos"
                    }
                )
                "ventas" -> PantallaVentas(
                    volver = {
                        pantalla = "inicio"
                    },
                    generarPlanilla = {
                        pantalla = "generarPlanilla"
                    },
                    reporteVentas = {
                        pantalla = "reporteVentas"
                    }
                )
                "generarPlanilla" -> GenerarPlanilla(
                    productoDao = db.productoDao(),
                    planillaDao = db.planillaDao(),
                    volver = {
                        pantalla = "ventas"
                    }
                )
                "reporteVentas" -> ReporteVentas(
                    planillaDao = db.planillaDao(),
                    verDetalle = { planilla ->
                        planillaSeleccionada = planilla.id
                        planillaActual = planilla
                        pantalla = "detalleVenta"
                    },
                    volver = {
                        pantalla = "ventas"
                    }
                )
                "detalleVenta" -> DetalleVenta(
                    planillaId = planillaSeleccionada,
                    planillaDao = db.planillaDao(),
                    planilla = planillaActual!!,
                    volver = {
                        pantalla = "reporteVentas"
                    }
                )
            }
        }
    }
}
@Composable
fun PantallaPrincipal(
    productos: () -> Unit,
    ventas: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.logo_critstore
            ),
            contentDescription = "Logo CritStore",
            modifier = Modifier
                .size(180.dp)
        )
        Spacer(
            modifier = Modifier.height(20.dp)
        )
        Text(
            text = "CritStore",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Button(
            onClick = productos,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text("📦 Productos")
        }
        Button(
            onClick = ventas,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text("💰 Ventas")
        }
    }
}