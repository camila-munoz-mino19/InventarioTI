
package com.example.inventarioti.model

data class Movimiento(
    val sku: String,
    val producto: String,
    val tipo: String,
    val cantidad: Int,
    val fecha: String,
    val usuario: String
)
