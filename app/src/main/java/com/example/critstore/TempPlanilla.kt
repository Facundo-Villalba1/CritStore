package com.example.critstore

object TempPlanilla {
    var nombreEvento: String = ""
    var fechaDesde: String = ""
    var fechaHasta: String = ""
    var cantidadesVenta: MutableMap<Int, String> =
        mutableMapOf()
    fun limpiar() {
        nombreEvento = ""
        fechaDesde = ""
        fechaHasta = ""
        cantidadesVenta.clear()
    }
}