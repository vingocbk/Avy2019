package com.app.avy.module

class Coordinates(var x: Float, var y: Float) : Comparable<Coordinates> {
    override fun compareTo(other: Coordinates): Int {
        return if (x > other.x) {
            1
        } else if (x < other.x) {
            -1
        } else {
            0
        }
    }

}