
package com.example.inventarioti

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.inventarioti.components.PantallaSinPermiso
import com.example.inventarioti.data.ApiProductos
import com.example.inventarioti.data.Notificaciones
import com.example.inventarioti.screens.PantallaAlertas
import com.example.inventarioti.screens.PantallaApiProductos
import com.example.inventarioti.screens.PantallaCamara
import com.example.inventarioti.screens.PantallaHistorial
import com.example.inventarioti.screens.PantallaLogin
import com.example.inventarioti.screens.PantallaMenu
import com.example.inventarioti.screens.PantallaMovimiento
import com.example.inventarioti.screens.PantallaProductos
import com.example.inventarioti.viewmodel.InventarioViewModel
import org.json.JSONObject
import java.util.UUID

private data class MovimientoPendiente(
    val idMovimiento: String,
    val sku: String,
    val tipo: String,
    val cantidad: Int,
    val stockEsperado: Int
)

private fun leerMovimientoPendiente(
    preferencias: SharedPreferences
): MovimientoPendiente? {

    val contenido = preferencias.getString(
        "movimiento_pendiente",
        null
    ) ?: return null

    return try {

        val datos = JSONObject(contenido)

        MovimientoPendiente(
            idMovimiento = datos.getString("idMovimiento"),
            sku = datos.getString("sku"),
            tipo = datos.getString("tipo"),
            cantidad = datos.getInt("cantidad"),
            stockEsperado = datos.getInt("stockEsperado")
        )

    } catch (e: Exception) {
        null
    }
}

private fun guardarMovimientoPendiente(
    preferencias: SharedPreferences,
    movimiento: MovimientoPendiente
): Boolean {

    val datos = JSONObject()

    datos.put(
        "idMovimiento",
        movimiento.idMovimiento
    )

    datos.put("sku", movimiento.sku)
    datos.put("tipo", movimiento.tipo)
    datos.put("cantidad", movimiento.cantidad)
    datos.put(
        "stockEsperado",
        movimiento.stockEsperado
    )

    return preferencias.edit()
        .putString(
            "movimiento_pendiente",
            datos.toString()
        )
        .commit()
}

private fun eliminarMovimientoPendiente(
    preferencias: SharedPreferences
): Boolean {

    return preferencias.edit()
        .remove("movimiento_pendiente")
        .commit()
}

@Composable
fun InventarioApp(modelo: InventarioViewModel) {

    var pantalla by rememberSaveable {
        mutableStateOf("login")
    }

    var mensajePermiso by remember {
        mutableStateOf("")
    }

    val contexto = LocalContext.current

    val preferencias = remember(contexto) {
        contexto.getSharedPreferences(
            "inventarioti_pendientes",
            Context.MODE_PRIVATE
        )
    }

    var movimientoPendiente by remember(preferencias) {
        mutableStateOf(
            leerMovimientoPendiente(preferencias)
        )
    }

    val pedirPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { permitido ->

        mensajePermiso = if (permitido) {
            "Notificaciones activadas"
        } else {
            "Permiso de notificaciones rechazado"
        }
    }

    when (pantalla) {

        "login" -> PantallaLogin(
            modelo = modelo,
            alIngresar = {
                pantalla = "menu"
            }
        )

        "menu" -> PantallaMenu(
            rol = modelo.rolActual,
            puedeRegistrar = modelo.puedeRegistrar(),
            puedeVerHistorial = modelo.puedeVerHistorial(),
            puedeVerAlertas = modelo.puedeVerAlertas(),
            mensajePermiso = mensajePermiso,
            verProductos = {
                pantalla = "productos"
            },
            verApiProductos = {
                pantalla = "api"
            },
            verMovimientos = {
                pantalla = "movimientos"
            },
            verHistorial = {
                pantalla = "historial"
            },
            verAlertas = {
                pantalla = "alertas"
            },
            verCamara = {
                pantalla = "camara"
            },
            activarNotificaciones = {

                if (Build.VERSION.SDK_INT >= 33) {

                    if (
                        contexto.checkSelfPermission(
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {

                        mensajePermiso =
                            "Notificaciones ya permitidas"

                    } else {

                        pedirPermiso.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }

                } else {

                    mensajePermiso =
                        "Notificaciones disponibles"
                }
            },
            cerrarSesion = {
                modelo.cerrarSesion()
                mensajePermiso = ""
                pantalla = "login"
            }
        )

        "api" -> PantallaApiProductos(
            onVolver = {
                pantalla = "menu"
            }
        )

        "productos" -> Column(
            modifier = Modifier.fillMaxSize()
        ) {

            TextButton(
                onClick = {
                    pantalla = "menu"
                }
            ) {
                Text("Volver al menú")
            }

            Box(
                modifier = Modifier.weight(1f)
            ) {
                PantallaProductos(modelo.productos)
            }
        }

        "movimientos" -> {

            if (modelo.puedeRegistrar()) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val pendiente = movimientoPendiente

                    if (pendiente != null) {

                        Text(
                            text = "Movimiento pendiente: ${pendiente.tipo} de ${pendiente.cantidad} unidad(es) del producto ${pendiente.sku}. Para recuperarlo, selecciona el mismo producto, tipo y cantidad.",
                            modifier = Modifier.padding(
                                start = 20.dp,
                                end = 20.dp,
                                top = 12.dp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {

                        PantallaMovimiento(
                            productos = modelo.productos,
                            guardarMovimiento = { _, _, _ ->
                                "Debes confirmar el movimiento con AWS"
                            },
                            guardarMovimientoAws = {
                                    sku,
                                    tipo,
                                    cantidad,
                                    clave,
                                    finalizar ->

                                val producto = modelo.productos.find {
                                    it.sku == sku
                                }

                                val existente = leerMovimientoPendiente(
                                    preferencias
                                )

                                if (producto == null) {

                                    finalizar(
                                        "Producto no encontrado"
                                    )

                                } else if (
                                    existente != null &&
                                    (
                                            existente.sku != sku ||
                                                    existente.tipo != tipo ||
                                                    existente.cantidad != cantidad
                                            )
                                ) {

                                    finalizar(
                                        "Ya tienes un movimiento pendiente. Primero debes recuperar la operación ${existente.tipo} de ${existente.cantidad} unidad(es) del producto ${existente.sku}"
                                    )

                                } else {

                                    val operacion = existente
                                        ?: MovimientoPendiente(
                                            idMovimiento = UUID
                                                .randomUUID()
                                                .toString(),
                                            sku = sku,
                                            tipo = tipo,
                                            cantidad = cantidad,
                                            stockEsperado = producto.cantidad
                                        )

                                    val guardado = if (existente != null) {
                                        true
                                    } else {
                                        guardarMovimientoPendiente(
                                            preferencias,
                                            operacion
                                        )
                                    }

                                    if (!guardado) {

                                        finalizar(
                                            "No se pudo guardar la operación pendiente. No se enviará a AWS"
                                        )

                                    } else {

                                        movimientoPendiente = operacion

                                        ApiProductos.registrarMovimiento(
                                            sku = operacion.sku,
                                            tipo = operacion.tipo,
                                            cantidad = operacion.cantidad,
                                            stockEsperado = operacion.stockEsperado,
                                            clave = clave,
                                            idMovimiento = operacion.idMovimiento
                                        ) { stockNuevo, error ->

                                            if (error != null) {

                                                finalizar(
                                                    "$error. La operación quedó pendiente. No cambies el producto ni la cantidad antes de recuperarla"
                                                )

                                            } else if (stockNuevo == null) {

                                                finalizar(
                                                    "AWS no devolvió el stock. La operación quedó pendiente"
                                                )

                                            } else {

                                                val stockCalculado =
                                                    if (
                                                        operacion.tipo == "Entrada"
                                                    ) {

                                                        operacion.stockEsperado.toLong() +
                                                                operacion.cantidad

                                                    } else {

                                                        operacion.stockEsperado.toLong() -
                                                                operacion.cantidad
                                                    }

                                                if (
                                                    stockNuevo.toLong() !=
                                                    stockCalculado
                                                ) {

                                                    finalizar(
                                                        "AWS devolvió una cantidad inesperada. No repitas el movimiento"
                                                    )

                                                } else {

                                                    val resultado =
                                                        modelo.guardarMovimientoConfirmadoAws(
                                                            sku = operacion.sku,
                                                            tipo = operacion.tipo,
                                                            cantidad = operacion.cantidad,
                                                            stockAnterior = operacion.stockEsperado,
                                                            stockNuevo = stockNuevo
                                                        )

                                                    if (
                                                        resultado ==
                                                        "Movimiento registrado"
                                                    ) {

                                                        val eliminado =
                                                            eliminarMovimientoPendiente(
                                                                preferencias
                                                            )

                                                        if (eliminado) {

                                                            movimientoPendiente = null

                                                            if (
                                                                operacion.tipo ==
                                                                "Salida"
                                                            ) {

                                                                val actualizado =
                                                                    modelo.productos.find {
                                                                        it.sku ==
                                                                                operacion.sku
                                                                    }

                                                                if (
                                                                    actualizado != null
                                                                ) {

                                                                    Notificaciones.mostrarStockBajo(
                                                                        contexto,
                                                                        actualizado
                                                                    )
                                                                }
                                                            }

                                                            finalizar(
                                                                "Movimiento registrado"
                                                            )

                                                        } else {

                                                            finalizar(
                                                                "El movimiento se guardó en ambas bases, pero no se pudo limpiar el registro pendiente. No lo repitas"
                                                            )
                                                        }

                                                    } else {

                                                        finalizar(
                                                            "AWS confirmó el stock $stockNuevo, pero SQLite no pudo guardar: $resultado. La operación quedó pendiente. No registres otro movimiento"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            },
                            volverMenu = {
                                pantalla = "menu"
                            }
                        )
                    }
                }

            } else {

                PantallaSinPermiso {
                    pantalla = "menu"
                }
            }
        }

        "historial" -> {

            if (modelo.puedeVerHistorial()) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    TextButton(
                        onClick = {
                            pantalla = "menu"
                        }
                    ) {
                        Text("Volver al menú")
                    }

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        PantallaHistorial(
                            modelo.movimientos
                        )
                    }
                }

            } else {

                PantallaSinPermiso {
                    pantalla = "menu"
                }
            }
        }

        "alertas" -> {

            if (modelo.puedeVerAlertas()) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    TextButton(
                        onClick = {
                            pantalla = "menu"
                        }
                    ) {
                        Text("Volver al menú")
                    }

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        PantallaAlertas(
                            modelo.productos
                        )
                    }
                }

            } else {

                PantallaSinPermiso {
                    pantalla = "menu"
                }
            }
        }

        "camara" -> {

            if (modelo.puedeRegistrar()) {

                PantallaCamara(
                    volverMenu = {
                        pantalla = "menu"
                    }
                )

            } else {

                PantallaSinPermiso {
                    pantalla = "menu"
                }
            }
        }
    }
}
