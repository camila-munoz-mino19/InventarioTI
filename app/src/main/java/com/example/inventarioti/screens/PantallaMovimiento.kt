
package com.example.inventarioti.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.inventarioti.model.Producto
import com.example.inventarioti.ui.theme.BordeLavanda
import com.example.inventarioti.ui.theme.FondoClaro
import com.example.inventarioti.ui.theme.Lavanda
import com.example.inventarioti.ui.theme.LavandaSuave
import com.example.inventarioti.ui.theme.MoradoBoton
import com.example.inventarioti.ui.theme.MoradoPrincipal
import com.example.inventarioti.ui.theme.MoradoSuave
import com.example.inventarioti.ui.theme.TextoMorado

@Composable
fun PantallaMovimiento(
    productos: List<Producto>,
    guardarMovimiento: (String, String, Int) -> String,
    volverMenu: () -> Unit
) {
    var sku by remember {
        mutableStateOf(productos.firstOrNull()?.sku ?: "")
    }

    var tipo by remember {
        mutableStateOf("Entrada")
    }

    var cantidad by remember {
        mutableStateOf("")
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    var expandido by remember {
        mutableStateOf(false)
    }

    val productoSeleccionado = productos.find {
        it.sku == sku
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Registrar movimiento",
            style = MaterialTheme.typography.headlineSmall,
            color = MoradoPrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Administra las entradas y salidas de productos",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoMorado
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = LavandaSuave
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Producto",
                    style = MaterialTheme.typography.titleMedium,
                    color = MoradoPrincipal
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box {
                    OutlinedButton(
                        onClick = {
                            expandido = true
                        },
                        enabled = productos.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BordeLavanda),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = MoradoPrincipal
                        )
                    ) {
                        Text(
                            text = productoSeleccionado?.nombre
                                ?: "Seleccionar producto"
                        )
                    }

                    DropdownMenu(
                        expanded = expandido,
                        onDismissRequest = {
                            expandido = false
                        }
                    ) {
                        productos.forEach { producto ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = producto.nombre,
                                        color = TextoMorado
                                    )
                                },
                                onClick = {
                                    sku = producto.sku
                                    expandido = false
                                    mensaje = ""
                                }
                            )
                        }
                    }
                }

                if (productos.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "No hay productos disponibles",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                AnimatedVisibility(
                    visible = productoSeleccionado != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Stock disponible: ${productoSeleccionado?.cantidad ?: 0}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MoradoPrincipal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tipo de movimiento",
            style = MaterialTheme.typography.titleMedium,
            color = MoradoPrincipal
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = tipo == "Entrada",
                onClick = {
                    tipo = "Entrada"
                    mensaje = ""
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MoradoBoton,
                    unselectedColor = MoradoSuave
                )
            )

            Text(
                text = "Entrada",
                color = TextoMorado,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(24.dp))

            RadioButton(
                selected = tipo == "Salida",
                onClick = {
                    tipo = "Salida"
                    mensaje = ""
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MoradoBoton,
                    unselectedColor = MoradoSuave
                )
            )

            Text(
                text = "Salida",
                color = TextoMorado,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Cantidad",
            style = MaterialTheme.typography.titleMedium,
            color = MoradoPrincipal
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = {
                if (it.all { caracter -> caracter.isDigit() }) {
                    cantidad = it
                    mensaje = ""
                }
            },
            label = {
                Text("Ingresa la cantidad")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MoradoBoton,
                unfocusedBorderColor = BordeLavanda,
                focusedLabelColor = MoradoPrincipal,
                unfocusedLabelColor = MoradoPrincipal,
                cursorColor = MoradoBoton,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(
            visible = mensaje.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LavandaSuave
                )
            ) {
                Text(
                    text = mensaje,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (mensaje == "Movimiento registrado") {
                        MoradoPrincipal
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val numero = cantidad.toIntOrNull()

                if (productoSeleccionado == null) {
                    mensaje = "Selecciona un producto"
                } else if (numero == null || numero <= 0) {
                    mensaje = "Ingresa una cantidad válida mayor que cero"
                } else {
                    mensaje = guardarMovimiento(
                        sku,
                        tipo,
                        numero
                    )

                    if (mensaje == "Movimiento registrado") {
                        cantidad = ""
                    }
                }
            },
            enabled = productos.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MoradoBoton,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Guardar movimiento",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = volverMenu,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, BordeLavanda),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Lavanda,
                contentColor = MoradoPrincipal
            )
        ) {
            Text(
                text = "Volver al menú",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
