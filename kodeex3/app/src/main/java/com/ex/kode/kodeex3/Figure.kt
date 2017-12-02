package com.ex.kode.kodeex3

abstract class Figure {
    fun calc(): String {
        var p = this.get_perimeter()
        var a = this.get_area()

//        return "%s: %s\n%s: %s".format(R.string.perimeter, p, R.string.area, a)
        return "%s: %s\n%s: %s".format("Perimeter", p, "Area", a)
    }

    abstract fun get_info(): String
    abstract fun get_perimeter(): String
    abstract fun get_area(): String
}