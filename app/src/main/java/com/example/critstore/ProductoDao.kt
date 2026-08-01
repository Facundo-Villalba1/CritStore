package com.example.critstore

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: Producto)
    @Query("SELECT * FROM productos")
    suspend fun obtenerProductos(): List<Producto>
    @Query("SELECT DISTINCT Tipo FROM productos ORDER BY Tipo")
    suspend fun obtenerTipos(): List<String>
    @Query("UPDATE productos SET Cantidad = :cantidad WHERE uuid = :uuid")
    suspend fun actualizarCantidad(
        uuid: String,
        cantidad: Int
    )
    @Query("UPDATE productos SET Cantidad = Cantidad - :cantidadVendida WHERE uuid = :uuidProducto")
    suspend fun descontarStock(
        uuidProducto: String,
        cantidadVendida: Int
    )
    @Delete
    suspend fun eliminarProducto(
        producto: Producto
    )
    @Query("DELETE FROM productos")
    suspend fun borrarProductos()
}