
package com.example.inventarioti

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.inventarioti.data.Notificaciones
import com.example.inventarioti.screens.PantallaProductos
import com.example.inventarioti.screens.PantallaMovimiento
import com.example.inventarioti.screens.PantallaHistorial
import com.example.inventarioti.screens.PantallaAlertas
import com.example.inventarioti.screens.PantallaCamara
import com.example.inventarioti.ui.theme.InventarioTITheme
import com.example.inventarioti.viewmodel.InventarioViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val modelo = ViewModelProvider(this)[InventarioViewModel::class.java]

        setContent {
            InventarioTITheme {
                InventarioApp(modelo)
            }
        }
    }
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

                    if (contexto.checkSelfPermission(
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

        "productos" -> Column(
            modifier = Modifier.fillMaxSize()
        ) {

            TextButton(
                onClick = { pantalla = "menu" }
            ) {
                Text("Volver al menu")
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

                        if (resultado == "Movimiento registrado" &&
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
                        onClick = { pantalla = "menu" }
                    ) {
                        Text("Volver al menu")
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
                        onClick = { pantalla = "menu" }
                    ) {
                        Text("Volver al menu")
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

@Composable
fun PantallaMenu(
    rol: String,
    puedeRegistrar: Boolean,
    puedeVerHistorial: Boolean,
    puedeVerAlertas: Boolean,
    mensajePermiso: String,
    verProductos: () -> Unit,
    verMovimientos: () -> Unit,
    verHistorial: () -> Unit,
    verAlertas: () -> Unit,
    verCamara: () -> Unit,
    activarNotificaciones: () -> Unit,
    cerrarSesion: () -> Unit
) {

    val fondo = Color(0xFFFAF7FF)
    val morado = Color(0xFF63398F)
    val lavanda = Color(0xFFEEE6F7)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = lavanda
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Inventario TI",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    color = morado
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Control de productos tecnologicos",
                    fontSize = 14.sp,
                    color = Color(0xFF5C4A6E)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rol: $rol",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = morado
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Panel de control",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = morado
        )

        Spacer(modifier = Modifier.height(20.dp))

        BotonMenu(
            texto = "Ver productos",
            onClick = verProductos,
            destacado = true
        )

        if (puedeRegistrar) {

            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Registrar movimiento",
                onClick = verMovimientos
            )
        }

        if (puedeVerHistorial) {

            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Ver historial",
                onClick = verHistorial
            )
        }

        if (puedeVerAlertas) {

            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Ver alertas",
                onClick = verAlertas
            )

            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Activar notificaciones",
                onClick = activarNotificaciones
            )
        }

        if (puedeRegistrar) {

            Spacer(modifier = Modifier.height(12.dp))

            BotonMenu(
                texto = "Camara de productos",
                onClick = verCamara
            )
        }

        if (mensajePermiso.isNotEmpty()) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = mensajePermiso,
                color = morado,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = cerrarSesion,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFC6B0E1)),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = morado
            )
        ) {
            Text("Cerrar sesion")
        }
    }
}

@Composable
fun BotonMenu(
    texto: String,
    onClick: () -> Unit,
    destacado: Boolean = false
) {

    val colorBoton = if (destacado) {
        Color(0xFF8159B2)
    } else {
        Color(0xFFE3D6F2)
    }

    val colorTexto = if (destacado) {
        Color.White
    } else {
        Color(0xFF42265D)
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorBoton,
            contentColor = colorTexto
        )
    ) {

        Text(
            text = texto,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PantallaSinPermiso(
    volverMenu: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF7FF))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("No tienes permiso para acceder")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = volverMenu) {
            Text("Volver al menu")
        }
    }
}

@Composable
fun PantallaLogin(
    modelo: InventarioViewModel,
    alIngresar: () -> Unit
) {

    var usuario by rememberSaveable {
        mutableStateOf("")
    }

    var clave by rememberSaveable {
        mutableStateOf("")
    }

    var mensajeError by rememberSaveable {
        mutableStateOf("")
    }

    val morado = Color(0xFF63398F)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF7FF))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Inventario TI",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            color = morado
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Bienvenido al sistema de inventario",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
                mensajeError = ""
            },
            label = { Text("Usuario") },
            isError = mensajeError.isNotEmpty() && usuario.isBlank(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = morado,
                focusedLabelColor = morado
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = {
                clave = it
                mensajeError = ""
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = mensajeError.isNotEmpty() && clave.isBlank(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = morado,
                focusedLabelColor = morado
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeError.isNotEmpty()) {

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                mensajeError = modelo.validarLogin(
                    usuario,
                    clave
                )

                if (mensajeError.isEmpty()) {
                    alIngresar()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8159B2)
            )
        ) {
            Text("Iniciar sesion")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Usuarios de prueba")
        Text("admin, bodega, ventas, supervisor")
        Text("Contraseña: 1234")
    }
}
