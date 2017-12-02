package com.ex.kode.kodeex3

class Rectangle(val s1: Int, val s2: Int) : Figure() {
    override fun get_perimeter(): String {
        return "%s".format((s1+s2)*2)
    }
    override fun get_area(): String {
        return "%s".format(s1*s2)
    }
    override fun get_info(): String {
        return "%s %s\n%s %s".format("Side 1:", s1, "Side 2:", s2)
    }
}