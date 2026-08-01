package com.example.critstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
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
            when(pantalla) {
                // PANTALLA PRINCIPAL
                "inicio" -> PantallaCelular(
                    productos = {
                        pantalla = "productos"
                    },
                    ventas = {
                        pantalla = "ventas"
                    },
                    Reportes = {
                        pantalla = "reportes"
                    }
                )
                // MENU PRODUCTOS
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
                // INGRESAR PRODUCTO
                "ingresarProducto" -> IngresarProducto(
                    productoDao = db.productoDao(),
                    volver = {
                        pantalla = "productos"
                    }
                )
                // CONSULTAR STOCK
                "verProductos" -> VerProductos(
                    productoDao = db.productoDao(),
                    volver = {
                        pantalla = "productos"
                    }
                )
                // ACTUALIZAR STOCK
                "actualizarStock" -> ActualizarStock(
                    productoDao = db.productoDao(),
                    volver = {
                        pantalla = "productos"
                    }
                )
                // MENU VENTAS
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
                // GENERAR PLANILLA
                "generarPlanilla" -> GenerarPlanilla(
                    productoDao = db.productoDao(),
                    planillaDao = db.planillaDao(),
                    volver = {
                        pantalla = "ventas"
                    }
                )
                // REPORTE VENTAS
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
                // DETALLE VENTA
                "detalleVenta" -> {
                    planillaActual?.let { planilla ->
                        DetalleVenta(
                            planillaId = planillaSeleccionada,
                            planillaDao = db.planillaDao(),
                            planilla = planilla,
                            editarPlanilla = {
                                planillaActual = it
                                pantalla = "editarPlanilla"
                            },
                            volver = {
                                pantalla = "reporteVentas"
                            }
                        )
                    }
                }
                // EDITAR PLANILLA
                "editarPlanilla" -> {
                    planillaActual?.let { planilla ->
                        EditarPlanilla(
                            planilla = planilla,
                            planillaDao = db.planillaDao(),
                            productoDao = db.productoDao(),
                            volver = {
                                pantalla = "detalleVenta"
                            }
                        )
                    }
                }
                    // MENU REPORTES
                "reportes" -> Reportes(
                    volver = {
                        pantalla = "inicio"
                    },
                    mayorVentaEvento = {
                        pantalla = "mayorVentaEvento"
                    },
                                   mayorVendidoFecha = {
                        pantalla = "vendidoPorFecha"
                    }
                )
                // MAYOR VENTA POR EVENTO
                "mayorVentaEvento" -> MayorVentaEvento(
                    volver = {
                        pantalla = "reportes"
                    }
                )
                // VENDIDO POR FECHA
                "vendidoPorFecha" -> VendidoPorFecha(
                    volver = {
                        pantalla = "reportes"
                    }
                )
                         }
        }
    }
}