
package com.example.inventarioti.data

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.example.inventarioti.model.Producto

object Notificaciones {

    fun mostrarStockBajo(contexto: Context, producto: Producto) {

        if (producto.cantidad > producto.stockMinimo) {
            return
        }

        if (Build.VERSION.SDK_INT >= 33 &&
            contexto.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val administrador = contexto.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        val canal = NotificationChannel(
            "stock_bajo",
            "Alertas de inventario",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        administrador.createNotificationChannel(canal)

        val notificacion = Notification.Builder(
            contexto,
            "stock_bajo"
        )
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Stock bajo")
            .setContentText(
                "${producto.nombre}: ${producto.cantidad} unidades disponibles"
            )
            .setAutoCancel(true)
            .build()

        administrador.notify(
            producto.sku.hashCode(),
            notificacion
        )
    }
}
