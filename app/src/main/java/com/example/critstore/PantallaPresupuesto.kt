package com.example.critstore

import java.util.Locale
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PantallaPresupuesto(
    volver: () -> Unit
) {
   val dim = obtenerDimensiones()
   var dolarTexto by remember {
        mutableStateOf("")
    }
   var costoPlaTexto by remember {
        mutableStateOf("0.027")
    }
   var costoKwTexto by remember {
        mutableStateOf("0.254")
    }
   var pesoTexto by remember {
        mutableStateOf("")
    }
   var horasTexto by remember {
        mutableStateOf("")
    }
   var otros by remember {
        mutableStateOf(false)
    }
   var costoPintadoTexto by remember {
        mutableStateOf("")
    }
   var costoArmadoTexto by remember {
        mutableStateOf("")
    }
    // CONVERSIÓN DE VALORES
   val dolar =
        dolarTexto
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0
   val costoPla =
        costoPlaTexto
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.027
   val costoKw =
        costoKwTexto
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.254
   val peso =
        pesoTexto
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0
   val horas =
        horasTexto
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0
   val costoPintado =
        costoPintadoTexto
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0
   val costoArmado =
        costoArmadoTexto
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0
    // PRESUPUESTO
   val presupuesto =
        Presupuesto(
            dolar = dolar,
            costoPlaGr = costoPla,
            costoKw = costoKw,
            pesoGramos = peso,
            horasImpresion = horas,
            otros = otros,
            costoPintado = costoPintado,
            costoArmado = costoArmado
        )
   // FORMATO DE NÚMEROS
  fun formatoNumero(
        valor: Double
    ): String {
       return if (
            valor % 1.0 == 0.0
        ) {
           valor
                .toInt()
                .toString()
       } else {
           String.format(
                Locale.US,
                "%.2f",
                valor
            )
        }
    }
    // PANTALLA
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dim.paddingPantalla * 2,
                start = dim.paddingPantalla,
                end = dim.paddingPantalla,
                bottom = dim.paddingPantalla)
    )
     {
      Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = dim.espacio
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
           Text(
                text = "💰",
                fontSize = dim.titulo
            )
           Spacer(
                modifier = Modifier.width(
                    8.dp
                )
            )
           Text(
                text = "Presupuesto",
                fontSize = dim.titulo,
                style =
                    MaterialTheme.typography.headlineLarge
            )
            Spacer(
                modifier = Modifier.height(
                    dim.espacio
                )
            )
        }
      if (dim.esTablet) {
           Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        dim.espacio
                    )
            ) {
               CampoPresupuesto(
                    modifier = Modifier.weight(1f),
                    valor = dolarTexto,
                    onValueChange = {
                        dolarTexto = it
                    },
                    label = "Dólar",
                    altura = dim.alturaBotonInterno
                )
               CampoPresupuesto(
                    modifier = Modifier.weight(1f),
                    valor = costoPlaTexto,
                    onValueChange = {
                        costoPlaTexto = it
                    },
                    label = "Costo PLA/gr",
                    altura = dim.alturaBotonInterno
                )
            }
       } else {
           CampoPresupuesto(
                modifier = Modifier.fillMaxWidth(),
                valor = dolarTexto,
                onValueChange = {
                    dolarTexto = it
                },
                label = "Dólar",
                altura = dim.alturaBotonInterno
            )
           Spacer(
                modifier = Modifier.height(
                    dim.espacio
                )
            )
           CampoPresupuesto(
                modifier = Modifier.fillMaxWidth(),
                valor = costoPlaTexto,
                onValueChange = {
                    costoPlaTexto = it
                },
                label = "Costo PLA/gr",
                altura = dim.alturaBotonInterno
            )
        }
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
      if (dim.esTablet) {
           Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        dim.espacio
                    )
            ) {
               CampoPresupuesto(
                    modifier = Modifier.weight(1f),
                    valor = costoKwTexto,
                    onValueChange = {
                        costoKwTexto = it
                    },
                    label = "Costo Kw/h",
                    altura = dim.alturaBotonInterno
                )
               CampoPresupuesto(
                    modifier = Modifier.weight(1f),
                    valor = pesoTexto,
                    onValueChange = {
                        pesoTexto = it
                    },
                    label = "Peso (gr)",
                    altura = dim.alturaBotonInterno
                )
            }
       } else {
           CampoPresupuesto(
                modifier = Modifier.fillMaxWidth(),
                valor = costoKwTexto,
                onValueChange = {
                    costoKwTexto = it
                },
                label = "Costo Kw/h",
                altura = dim.alturaBotonInterno
            )
           Spacer(
                modifier = Modifier.height(
                    dim.espacio
                )
            )
           CampoPresupuesto(
                modifier = Modifier.fillMaxWidth(),
                valor = pesoTexto,
                onValueChange = {
                    pesoTexto = it
                },
                label = "Peso (gr)",
                altura = dim.alturaBotonInterno
            )
        }
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
      CampoPresupuesto(
            modifier =
                if (dim.esTablet) {
                   Modifier.width(
                        320.dp
                    )
               } else {
                   Modifier.fillMaxWidth()
                },
            valor = horasTexto,
            onValueChange = {
                horasTexto = it
            },
            label = "Horas impresión",
            altura = dim.alturaBotonInterno
        )
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
     Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    otros = !otros
                }
                .padding(
                    vertical = 4.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {
           Checkbox(
                checked = otros,
                onCheckedChange = {
                    otros = it
                }
            )
           Text(
                text = "Otros",
                fontSize = dim.texto
            )
        }
     if (otros) {
           Spacer(
                modifier = Modifier.height(
                    dim.espacio
                )
            )
           if (dim.esTablet) {
               Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            dim.espacio
                        )
                ) {
                   CampoPresupuesto(
                        modifier = Modifier.weight(1f),
                        valor = costoPintadoTexto,
                        onValueChange = {
                            costoPintadoTexto = it
                        },
                        label = "Costo Pintado",
                        altura = dim.alturaBotonInterno
                    )
                   CampoPresupuesto(
                        modifier = Modifier.weight(1f),
                        valor = costoArmadoTexto,
                        onValueChange = {
                            costoArmadoTexto = it
                        },
                        label = "Costo Armado",
                        altura = dim.alturaBotonInterno
                    )
                }
           } else {
               CampoPresupuesto(
                    modifier = Modifier.fillMaxWidth(),
                    valor = costoPintadoTexto,
                    onValueChange = {
                        costoPintadoTexto = it
                    },
                    label = "Costo Pintado",
                    altura = dim.alturaBotonInterno
                )
               Spacer(
                    modifier = Modifier.height(
                        dim.espacio
                    )
                )
               CampoPresupuesto(
                    modifier = Modifier.fillMaxWidth(),
                    valor = costoArmadoTexto,
                    onValueChange = {
                        costoArmadoTexto = it
                    },
                    label = "Costo Armado",
                    altura = dim.alturaBotonInterno
                )
            }
        }
     Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
       HorizontalDivider()
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
     Text(
            text =
                "Costo fabricación USD:  U\$S  ${
                    formatoNumero(
                        presupuesto.costoTotalDolares
                    )
                }",
            fontSize = dim.texto,
            fontWeight = FontWeight.Bold
        )
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
     Text(
            text =
                "Costo fabricación pesos: \$ ${
                    formatoNumero(
                        presupuesto.costoTotalPesos
                    )
                }",
            fontSize = dim.texto,
            fontWeight = FontWeight.Bold
        )
            HorizontalDivider()
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
      Text(
            text =
                "💵 Precio venta pesos: \$ ${
                   formatoNumero(
                        presupuesto.precioVentaPesos
                    )
               }",
            fontSize = dim.texto,
            fontWeight = FontWeight.Bold
        )
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
      Text(
            text =
                "💲 Precio venta USD: U\$S ${
                       formatoNumero(
                            presupuesto.precioVentaDolares
                        )
               }",
            fontSize = dim.texto,
            fontWeight = FontWeight.Bold
        )
       Spacer(
            modifier = Modifier.height(
                dim.espacio
            )
        )
      Spacer( modifier = Modifier.height(dim.espacio) )
         Button(
             onClick = volver,
             modifier =Modifier
                 .widthIn(dim.alturaBotonInterno)
                 .fillMaxWidth()
                 .align(Alignment.CenterHorizontally)
         ) {
             Text(
                 text = "⬅ Volver",
                 fontSize = dim.texto
             )
         }
         Spacer( modifier = Modifier.height(dim.espacio) )
   }
}
@Composable
private fun CampoPresupuesto(
    modifier: Modifier = Modifier,
    valor: String,
    onValueChange: (String) -> Unit,
    label: String,
    altura: Dp = 64.dp
) {
   OutlinedTextField(
        value = valor,
       onValueChange = { nuevoValor ->
         val limpio =
                nuevoValor
                    .replace(",", ".")
           if (
                limpio.isEmpty() ||
                limpio.matches(
                    Regex(
                        "^\\d*(\\.\\d*)?$"
                    )
                )
            ) {
               onValueChange(
                    limpio
                )
            }
        },
       label = {
            Text(
                text = label
            )
        },
       modifier = modifier
            .height(altura),
       singleLine = true,
       keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    KeyboardType.Decimal
            )
    )
}
