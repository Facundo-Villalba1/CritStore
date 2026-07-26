package com.example.critstore

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductoDao {
    @Insert
    suspend fun insertarProducto(
        producto: Producto
    )
    @Query("SELECT * FROM productos")
    suspend fun obtenerProductos(): List<Producto>
    @Query("SELECT DISTINCT tipo FROM productos ORDER BY tipo")
    suspend fun obtenerTipos(): List<String>
    @Query("UPDATE productos SET Cantidad = :cantidad WHERE id = :id")
    suspend fun actualizarCantidad(
        id: Int,
        cantidad: Int
    )
    @Query("UPDATE productos SET Cantidad = Cantidad - :cantidadVendida WHERE id = :idProducto")
    suspend fun descontarStock(
        idProducto: Int,
        cantidadVendida: Int
    )
    @Delete
    suspend fun eliminarProducto(producto: Producto)
}