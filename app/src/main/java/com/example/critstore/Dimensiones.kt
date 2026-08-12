package com.example.critstore

import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*

class DimensionesApp(
    val paddingPantalla: Dp,
    val espacio: Dp,
    val alturaBoton: Dp,
    val texto: TextUnit,
    val titulo: TextUnit,
    val logo: Dp,
    val alturaBotonInterno:Dp,
    val esTablet: Boolean,
    val pesoProducto: Float
)

@Composable
fun obtenerDimensiones(): DimensionesApp {
   val configuracion =
        LocalConfiguration.current
   val ancho =
        configuracion.screenWidthDp
   val alto =
        configuracion.screenHeightDp
   return when {
       ancho >= 900 -> {
           DimensionesApp(
               paddingPantalla = 40.dp,
               espacio = 30.dp,
               alturaBoton = 75.dp,
                alturaBotonInterno = 60.dp,
               texto = 22.sp,
               titulo = 32.sp,
               logo = 250.dp,
                esTablet = true,
                pesoProducto = 3f
           )
       }
       ancho >= 600 -> {
           DimensionesApp(
               paddingPantalla = 30.dp,
               espacio = 20.dp,
               alturaBoton = 65.dp,
                alturaBotonInterno = 60.dp,
                texto = 20.sp,
                titulo = 28.sp,
                logo = 220.dp ,
                esTablet = true,
                pesoProducto = 3f
            )
       }
       alto >= 800 -> {
           DimensionesApp(
               paddingPantalla = 18.dp,
               espacio = 16.dp,
               alturaBoton = 65.dp,
                alturaBotonInterno = 60.dp,
                texto = 17.sp,
               titulo = 24.sp,
               logo = 160.dp,
                esTablet = true,
                pesoProducto = 3f
           )
       }
       else -> {
           DimensionesApp(
               paddingPantalla = 12.dp,
               espacio = 10.dp,
               alturaBoton = 50.dp,
                alturaBotonInterno = 60.dp,
               texto = 15.sp,
               titulo = 21.sp,
               logo = 120.dp,
                esTablet = true,
                pesoProducto = 3f
           )
       }
   }
}