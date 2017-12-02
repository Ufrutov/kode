package com.ex.kode.kodeex3

class Triangle(val s1: Int, val s2: Int, val s3: Int) : Figure() {
    override fun get_perimeter(): String {
        return "%s".format(s1+s2+s3)
    }
    override fun get_area(): String {
        var p: Double = ((s1+s2+s3)/2).toDouble()
        var a: Double = Math.sqrt(p*(p-s1)*(p-s2)*(p-s3))
        return "%.2f".format(a)
    }
    override fun get_info(): String {
        return "%s %s\n%s %s\n%s %s".format("Side 1:", s1, "Side 2:", s2, "Side 3:", s3)
    }
}