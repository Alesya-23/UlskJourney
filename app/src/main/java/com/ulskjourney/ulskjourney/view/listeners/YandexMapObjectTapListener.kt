package com.ulskjourney.ulskjourney.view.listeners

import android.content.Context
import android.widget.Toast
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener

class YandexMapObjectTapListener() : MapObjectTapListener {
    var context: Context? = null
    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
        Toast.makeText(context, "ready", Toast.LENGTH_LONG).show()
        return true
    }

    fun getContext(Context: Context) {
        context = Context
    }
}