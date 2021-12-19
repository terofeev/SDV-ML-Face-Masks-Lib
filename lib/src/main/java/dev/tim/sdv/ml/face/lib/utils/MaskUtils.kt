package dev.tim.sdv.ml.face.lib.utils

import dev.tim.sdv.ml.face.lib.Mask

fun Mask.next(): Mask {
    return when (id) {
        1 -> Mask.SHOWER_HAT
        2 -> Mask.DEER_HORNS
        3 -> Mask.CARNIVAL_MASK
        4 -> Mask.BEAR_NOSE_EARS
        5 -> Mask.IRISH_HAT
        6 -> Mask.DEER_FACE
        else -> Mask.DEER_FACE
    }
}

fun Mask.previous(): Mask {
    return when (id) {
        1 -> Mask.IRISH_HAT
        2 -> Mask.DEER_FACE
        3 -> Mask.SHOWER_HAT
        4 -> Mask.DEER_HORNS
        5 -> Mask.CARNIVAL_MASK
        6 -> Mask.BEAR_NOSE_EARS
        else -> Mask.DEER_FACE
    }
}