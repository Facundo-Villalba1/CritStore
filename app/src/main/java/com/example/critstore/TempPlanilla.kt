package com.example.critstore

object TempPlanilla {
    var nombreEvento: String = ""
    var fechaDesde: String = ""
    var fechaHasta: String = ""
    var cantidadesVenta: MutableMap<String, String> = mutableMapOf()
    fun limpiar() {
        nombreEvento = ""
        fechaDesde = ""
        fechaHasta = ""
        cantidadesVenta =
            mutableMapOf<String, String>()
    }
}