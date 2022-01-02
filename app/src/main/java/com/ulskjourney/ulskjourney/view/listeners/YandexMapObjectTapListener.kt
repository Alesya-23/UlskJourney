package com.ulskjourney.ulskjourney.view.listeners

import android.content.Context
import android.widget.Toast
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener

class YandexMapObjectTapListener() : MapObjectTapListener {
    var context: Context? = null
    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
        Toast.makeText(context, "Метка", Toast.LENGTH_LONG).show()
        //получение айдишника и открытие детального экрана
        return true
    }

    fun getContext(Context: Context) {
        context = Context
    }
}