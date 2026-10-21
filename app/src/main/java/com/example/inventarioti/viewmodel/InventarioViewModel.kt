
package com.example.inventarioti.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import com.example.inventarioti.data.BaseDatos
import com.example.inventarioti.model.Producto
import com.example.inventarioti.model.Movimiento

class InventarioViewModel(application: Application) : AndroidViewModel(application) {

    private val baseDatos = BaseDatos(application)

    val productos = mutableStateListOf<Producto>()

    val movimientos = mutableStateListOf<Movimiento>()

    var usuarioActual by mutableStateOf("")
        private set

    var rolActual by mutableStateOf("")
        private set

    init {
        productos.addAll(baseDatos.obtenerProductos())
        movimientos.addAll(baseDatos.obtenerMovimientos())
    }

    fun validarLogin(usuario: String, clave: String): String {

        if (usuario.isBlank() || clave.isBlank()) {
            return "Completa todos los campos"
        }

        if (clave != "1234") {
            return "Usuario o contraseña incorrectos"
        }

        val nombre = usuario.trim().lowercase()

        val rol = when (nombre) {
            "admin" -> "Administrador"
            "bodega" -> "Bodega"
            "ventas" -> "Ventas"
            "supervisor" -> "Supervisor"
            else -> ""
        }

        if (rol.isEmpty()) {
            return "Usuario o contraseña incorrectos"
        }

        usuarioActual = nombre
        rolActual = rol

        return ""
    }

    fun puedeRegistrar(): Boolean {
        return rolActual == "Administrador" || rolActual == "Bodega"
    }

    fun puedeVerHistorial(): Boolean {
        return rolActual != "Ventas" && rolActual.isNotEmpty()
    }

    fun puedeVerAlertas(): Boolean {
        return rolActual != "Ventas" && rolActual.isNotEmpty()
    }

    fun cerrarSesion() {
        usuarioActual = ""
        rolActual = ""
    }

    fun guardarMovimiento(
        sku: String,
        tipo: String,
        cantidad: Int
    ): String {

        if (!puedeRegistrar()) {
            return "No tienes permiso para registrar movimientos"
        }

        val resultado = baseDatos.guardarMovimiento(sku, tipo, cantidad)

        if (resultado == "Movimiento registrado") {

            productos.clear()
            productos.addAll(baseDatos.obtenerProductos())

            movimientos.clear()
            movimientos.addAll(baseDatos.obtenerMovimientos())
        }

        return resultado
    }
}
