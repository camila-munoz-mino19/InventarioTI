
package com.example.inventarioti

import com.example.inventarioti.screens.PantallaLogin
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.inventarioti.components.PantallaSinPermiso
import com.example.inventarioti.data.Notificaciones
import com.example.inventarioti.screens.PantallaAlertas
import com.example.inventarioti.screens.PantallaApiProductos
import com.example.inventarioti.screens.PantallaCamara
import com.example.inventarioti.screens.PantallaHistorial
import com.example.inventarioti.screens.PantallaMenu
import com.example.inventarioti.screens.PantallaMovimiento
import com.example.inventarioti.screens.PantallaProductos
import com.example.inventarioti.viewmodel.InventarioViewModel

@Composable
fun InventarioApp(modelo: InventarioViewModel) {

    var pantalla by rememberSaveable {
        mutableStateOf("login")
    }

    var mensajePermiso by remember {
        mutableStateOf("")
    }

    val contexto = LocalContext.current

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
                        mensajePermiso = "Notificaciones ya permitidas"
                    } else {
                        pedirPermiso.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    }
                } else {
                    mensajePermiso = "Notificaciones disponibles"
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

            Box(modifier = Modifier.weight(1f)) {
                PantallaProductos(modelo.productos)
            }
        }

        "movimientos" -> {
            if (modelo.puedeRegistrar()) {
                PantallaMovimiento(
                    productos = modelo.productos,
                    guardarMovimiento = { sku, tipo, cantidad ->
                        val resultado = modelo.guardarMovimiento(
                            sku,
                            tipo,
                            cantidad
                        )

                        if (
                            resultado == "Movimiento registrado" &&
                            tipo == "Salida"
                        ) {
                            val producto = modelo.productos.find {
                                it.sku == sku
                            }

                            if (producto != null) {
                                Notificaciones.mostrarStockBajo(
                                    contexto,
                                    producto
                                )
                            }
                        }

                        resultado
                    },
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

                    Box(modifier = Modifier.weight(1f)) {
                        PantallaHistorial(modelo.movimientos)
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

                    Box(modifier = Modifier.weight(1f)) {
                        PantallaAlertas(modelo.productos)
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
