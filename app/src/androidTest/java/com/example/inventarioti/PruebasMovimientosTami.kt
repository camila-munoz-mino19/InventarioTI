package com.example.inventarioti

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventarioti.viewmodel.InventarioViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PruebasMovimientosTami {

    @get:Rule
    val regla = createAndroidComposeRule<MainActivity>()

    @Test
    fun entradaAumentaStock() {
        regla.runOnUiThread {
            val modelo = ViewModelProvider(regla.activity)[InventarioViewModel::class.java]
            assertEquals("", modelo.validarLogin("admin", "1234"))

            val producto = modelo.productos.firstOrNull()
                ?: error("No existen productos para probar")
            val cantidadInicial = producto.cantidad
            val movimientosIniciales = modelo.movimientos.size

            val resultado = modelo.guardarMovimiento(producto.sku, "Entrada", 2)
            val actualizado = modelo.productos.first { it.sku == producto.sku }

            assertEquals("Movimiento registrado", resultado)
            assertEquals(cantidadInicial + 2, actualizado.cantidad)
            assertEquals(movimientosIniciales + 1, modelo.movimientos.size)
        }
    }

    @Test
    fun salidaDisminuyeStock() {
        regla.runOnUiThread {
            val modelo = ViewModelProvider(regla.activity)[InventarioViewModel::class.java]
            assertEquals("", modelo.validarLogin("admin", "1234"))

            val producto = modelo.productos.firstOrNull { it.cantidad > 0 }
                ?: error("No hay productos con stock para probar una salida")
            val cantidadInicial = producto.cantidad
            val movimientosIniciales = modelo.movimientos.size

            val resultado = modelo.guardarMovimiento(producto.sku, "Salida", 1)
            val actualizado = modelo.productos.first { it.sku == producto.sku }

            assertEquals("Movimiento registrado", resultado)
            assertEquals(cantidadInicial - 1, actualizado.cantidad)
            assertEquals(movimientosIniciales + 1, modelo.movimientos.size)
        }
    }

    @Test
    fun salidaConStockInsuficienteSeRechaza() {
        regla.runOnUiThread {
            val modelo = ViewModelProvider(regla.activity)[InventarioViewModel::class.java]
            assertEquals("", modelo.validarLogin("admin", "1234"))

            val producto = modelo.productos.firstOrNull()
                ?: error("No existen productos para probar")
            val cantidadInicial = producto.cantidad
            val movimientosIniciales = modelo.movimientos.size
            val cantidadSolicitada = cantidadInicial + 10

            val resultado = modelo.guardarMovimiento(
                producto.sku,
                "Salida",
                cantidadSolicitada
            )
            val actualizado = modelo.productos.first { it.sku == producto.sku }

            assertNotEquals("Movimiento registrado", resultado)
            assertTrue("La aplicación debe explicar el rechazo", resultado.isNotBlank())
            assertEquals(cantidadInicial, actualizado.cantidad)
            assertEquals(movimientosIniciales, modelo.movimientos.size)
        }
    }
}
