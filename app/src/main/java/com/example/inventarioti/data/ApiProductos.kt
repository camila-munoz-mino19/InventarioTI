
package com.example.inventarioti.data

import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class ProductoApi(
    val nombre: String,
    val stock: Int
)

object ApiProductos {

    fun consultarProductos(
        onResultado: (List<ProductoApi>, String?) -> Unit
    ) {
        Thread {
            var conexion: HttpURLConnection? = null

            try {
                val direccion = URL(
                    "https://dummyjson.com/products/search?q=laptop&limit=5"
                )

                conexion = direccion.openConnection() as HttpURLConnection
                conexion.requestMethod = "GET"
                conexion.connectTimeout = 10000
                conexion.readTimeout = 10000

                if (conexion.responseCode != 200) {
                    throw Exception("Error de conexion")
                }

                val respuesta = conexion.inputStream
                    .bufferedReader()
                    .use { it.readText() }

                val lista = JSONObject(respuesta)
                    .getJSONArray("products")

                val productos = mutableListOf<ProductoApi>()

                for (i in 0 until lista.length()) {
                    val producto = lista.getJSONObject(i)

                    productos.add(
                        ProductoApi(
                            nombre = producto.getString("title"),
                            stock = producto.getInt("stock")
                        )
                    )
                }

                Handler(Looper.getMainLooper()).post {
                    onResultado(productos, null)
                }

            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    onResultado(
                        emptyList(),
                        "No se pudo consultar la API"
                    )
                }
            } finally {
                conexion?.disconnect()
            }
        }.start()
    }
}


