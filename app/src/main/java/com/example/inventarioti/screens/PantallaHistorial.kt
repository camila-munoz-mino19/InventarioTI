
package com.example.inventarioti.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.inventarioti.model.Movimiento
import com.example.inventarioti.ui.theme.FondoClaro
import com.example.inventarioti.ui.theme.Lavanda
import com.example.inventarioti.ui.theme.LavandaSuave
import com.example.inventarioti.ui.theme.MoradoPrincipal
import com.example.inventarioti.ui.theme.TextoMorado

@Composable
fun PantallaHistorial(movimientos: List<Movimiento>) {

    var mostrarHistorial by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        mostrarHistorial = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoClaro)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Historial de movimientos",
            style = MaterialTheme.typography.headlineSmall,
            color = MoradoPrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Consulta las entradas y salidas del inventario",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoMorado
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Lavanda
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Total de movimientos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoMorado
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${movimientos.size}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MoradoPrincipal
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (movimientos.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = LavandaSuave
                )
            ) {
                Text(
                    text = "No hay movimientos registrados",
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MoradoPrincipal
                )
            }

        } else {

            AnimatedVisibility(
                visible = mostrarHistorial,
                enter = fadeIn(
                    animationSpec = tween(450)
                ) + slideInVertically(
                    initialOffsetY = { it / 6 },
                    animationSpec = tween(450)
                )
            ) {

                Column {

                    movimientos.asReversed().forEachIndexed { indice, movimiento ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 14.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {

                                Text(
                                    text = movimiento.producto,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MoradoPrincipal
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Movimiento ${indice + 1}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextoMorado
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = LavandaSuave
                                    )
                                ) {

                                    Column(
                                        modifier = Modifier.padding(14.dp)
                                    ) {

                                        Text(
                                            text = "Tipo: ${movimiento.tipo}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MoradoPrincipal
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "Cantidad: ${movimiento.cantidad}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MoradoPrincipal
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                HorizontalDivider(
                                    color = Lavanda
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "SKU: ${movimiento.sku}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextoMorado
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Fecha: ${movimiento.fecha}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextoMorado
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Usuario: ${movimiento.usuario}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextoMorado
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
