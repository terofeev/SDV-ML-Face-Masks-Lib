package dev.tim.sdv.ml.face.lib.utils

import dev.tim.sdv.ml.face.lib.Mask

fun dev.tim.sdv.ml.face.lib.Mask.next(): dev.tim.sdv.ml.face.lib.Mask {
    return when (id) {
        1 -> dev.tim.sdv.ml.face.lib.Mask.SHOWER_HAT
        2 -> dev.tim.sdv.ml.face.lib.Mask.DEER_HORNS
        3 -> dev.tim.sdv.ml.face.lib.Mask.CARNIVAL_MASK
        4 -> dev.tim.sdv.ml.face.lib.Mask.BEAR_NOSE_EARS
        5 -> dev.tim.sdv.ml.face.lib.Mask.IRISH_HAT
        6 -> dev.tim.sdv.ml.face.lib.Mask.DEER_FACE
        else -> dev.tim.sdv.ml.face.lib.Mask.DEER_FACE
    }
}

fun dev.tim.sdv.ml.face.lib.Mask.previous(): dev.tim.sdv.ml.face.lib.Mask {
    return when (id) {
        1 -> dev.tim.sdv.ml.face.lib.Mask.IRISH_HAT
        2 -> dev.tim.sdv.ml.face.lib.Mask.DEER_FACE
        3 -> dev.tim.sdv.ml.face.lib.Mask.SHOWER_HAT
        4 -> dev.tim.sdv.ml.face.lib.Mask.DEER_HORNS
        5 -> dev.tim.sdv.ml.face.lib.Mask.CARNIVAL_MASK
        6 -> dev.tim.sdv.ml.face.lib.Mask.BEAR_NOSE_EARS
        else -> dev.tim.sdv.ml.face.lib.Mask.DEER_FACE
    }
}