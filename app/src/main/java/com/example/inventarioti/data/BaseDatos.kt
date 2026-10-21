
package com.example.inventarioti.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.inventarioti.model.Producto
import com.example.inventarioti.model.Movimiento
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BaseDatos(context: Context) :
    SQLiteOpenHelper(context, "inventario.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE productos (
                sku TEXT PRIMARY KEY,
                nombre TEXT,
                descripcion TEXT,
                categoria TEXT,
                marca TEXT,
                cantidad INTEGER,
                stock_minimo INTEGER
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE movimientos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                sku TEXT,
                producto TEXT,
                tipo TEXT,
                cantidad INTEGER,
                fecha TEXT,
                usuario TEXT
            )
            """.trimIndent()
        )

        ProductosPrueba.productos.forEach { producto ->

            val datos = ContentValues()

            datos.put("sku", producto.sku)
            datos.put("nombre", producto.nombre)
            datos.put("descripcion", producto.descripcion)
            datos.put("categoria", producto.categoria)
            datos.put("marca", producto.marca)
            datos.put("cantidad", producto.cantidad)
            datos.put("stock_minimo", producto.stockMinimo)

            db.insert("productos", null, datos)
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        versionAnterior: Int,
        versionNueva: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS movimientos")
        db.execSQL("DROP TABLE IF EXISTS productos")

        onCreate(db)
    }

    fun obtenerProductos(): List<Producto> {

        val lista = mutableListOf<Producto>()

        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM productos",
            null
        )

        cursor.use {

            while (it.moveToNext()) {

                val producto = Producto(
                    sku = it.getString(0),
                    nombre = it.getString(1),
                    descripcion = it.getString(2),
                    categoria = it.getString(3),
                    marca = it.getString(4),
                    cantidad = it.getInt(5),
                    stockMinimo = it.getInt(6)
                )

                lista.add(producto)
            }
        }

        return lista
    }

    fun obtenerMovimientos(): List<Movimiento> {

        val lista = mutableListOf<Movimiento>()

        val cursor = readableDatabase.rawQuery(
            "SELECT sku, producto, tipo, cantidad, fecha, usuario FROM movimientos ORDER BY id",
            null
        )

        cursor.use {

            while (it.moveToNext()) {

                val movimiento = Movimiento(
                    sku = it.getString(0),
                    producto = it.getString(1),
                    tipo = it.getString(2),
                    cantidad = it.getInt(3),
                    fecha = it.getString(4),
                    usuario = it.getString(5)
                )

                lista.add(movimiento)
            }
        }

        return lista
    }

    fun guardarMovimiento(
        sku: String,
        tipo: String,
        cantidad: Int
    ): String {

        if (cantidad <= 0) {
            return "La cantidad debe ser mayor que cero"
        }

        if (tipo != "Entrada" && tipo != "Salida") {
            return "Tipo de movimiento incorrecto"
        }

        val db = writableDatabase

        val cursor = db.rawQuery(
            "SELECT nombre, cantidad FROM productos WHERE sku = ?",
            arrayOf(sku)
        )

        var nombre = ""
        var stock = 0
        var encontrado = false

        cursor.use {

            if (it.moveToFirst()) {
                nombre = it.getString(0)
                stock = it.getInt(1)
                encontrado = true
            }
        }

        if (!encontrado) {
            return "Producto no encontrado"
        }

        if (tipo == "Salida" && cantidad > stock) {
            return "Stock insuficiente"
        }

        if (tipo == "Entrada" && cantidad > Int.MAX_VALUE - stock) {
            return "Cantidad demasiado grande"
        }

        val nuevoStock = if (tipo == "Entrada") {
            stock + cantidad
        } else {
            stock - cantidad
        }

        val fecha = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())

        val datosProducto = ContentValues()
        datosProducto.put("cantidad", nuevoStock)

        val datosMovimiento = ContentValues()
        datosMovimiento.put("sku", sku)
        datosMovimiento.put("producto", nombre)
        datosMovimiento.put("tipo", tipo)
        datosMovimiento.put("cantidad", cantidad)
        datosMovimiento.put("fecha", fecha)
        datosMovimiento.put("usuario", "admin")

        db.beginTransaction()

        try {

            val actualizado = db.update(
                "productos",
                datosProducto,
                "sku = ?",
                arrayOf(sku)
            )

            if (actualizado != 1) {
                return "Error al actualizar el producto"
            }

            val guardado = db.insert(
                "movimientos",
                null,
                datosMovimiento
            )

            if (guardado == -1L) {
                return "Error al guardar el movimiento"
            }

            db.setTransactionSuccessful()

        } catch (e: Exception) {

            return "Error al guardar los datos"

        } finally {

            db.endTransaction()
        }

        return "Movimiento registrado"
    }
}
