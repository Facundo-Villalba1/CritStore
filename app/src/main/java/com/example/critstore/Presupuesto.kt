package com.example.critstore

data class Presupuesto(
    val dolar: Double = 0.0,
    val costoPlaGr: Double = 0.027,
    val costoKw: Double = 0.254,
    val pesoGramos: Double = 0.0,
    val horasImpresion: Double = 0.0,
    val otros: Boolean = false,
    val costoPintado: Double = 0.0,
    val costoArmado: Double = 0.0,
    val multiplicadorGanancia: Double = 3.0
) {

    // COSTO DEL MATERIAL
    val costoMaterial: Double
        get() = pesoGramos * costoPlaGr

    // DESGASTE
    val desgaste: Double
        get() = when {
            horasImpresion <= 2 -> 1.0
            horasImpresion <= 4 -> 2.0
            horasImpresion <= 6 -> 6.0
            horasImpresion <= 8 -> 8.0
            horasImpresion <= 10 -> 10.0
            else -> horasImpresion * 1.5
        }

    // COSTO DE ENERGÍA
   val costoEnergia: Double
        get() =
            horasImpresion * costoKw

    // COSTO DE OTROS
   val costoOtros: Double
        get() =
            if (otros) {
                costoPintado + costoArmado
            } else {
                0.0
            }

    // COSTO TOTAL EN DÓLARES
   val costoTotalDolares: Double
        get() =
            redondearDecimal(
                costoMaterial +
                        costoEnergia +
                        desgaste +
                        costoOtros
            )

    // COSTO TOTAL EN PESOS
   val costoTotalPesos: Double
        get() =
            if (dolar > 0) {
                redondearDecimal(
                    costoTotalDolares * dolar
                )
            } else {
                0.0
            }

    // PRECIO DE VENTA EN PESOS
   val precioVentaPesos: Double
        get() =
            redondearPrecio(
                costoTotalPesos * multiplicadorGanancia
            )

    // PRECIO DE VENTA EN DÓLARES
   val precioVentaDolares: Double
        get() =
            if (dolar > 0) {
                redondearDecimal(
                    precioVentaPesos / dolar
                )
            } else {
                0.0
            }

    // REDONDEO DECIMALES
   private fun redondearDecimal(
        valor: Double
    ): Double {
       return kotlin.math.round(
            valor * 100
        ) / 100
    }

    // REDONDEO A CENTENAS
   private fun redondearPrecio(
        valor: Double
    ): Double {
       return kotlin.math.round(
            valor / 100
        ) * 100
    }
}