
package com.example.inventarioti.model

data class Producto(
    val sku: String,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val marca: String,
    val cantidad: Int,
    val stockMinimo: Int
)
