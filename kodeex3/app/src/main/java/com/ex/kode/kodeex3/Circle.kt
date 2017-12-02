package com.ex.kode.kodeex3

import android.provider.Settings.Global.getString

class Circle(val r: Int) : Figure() {
    override fun get_perimeter(): String {
        return "%.2f".format(r*2*Math.PI)
    }
    override fun get_area(): String {
        return "%.2f".format(2*Math.PI*r*r)
    }
    override fun get_info(): String {
//        return "%s %s".format(getString(R.string.radius), r)
        return "%s %s".format("Radius:", r)
    }
}