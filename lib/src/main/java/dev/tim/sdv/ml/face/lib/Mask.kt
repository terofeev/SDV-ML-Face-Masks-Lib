package dev.tim.sdv.ml.face.lib

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class Mask(
    val id: Int,
    @DrawableRes
    val drawable: Int,
    @StringRes
    val title: Int,
    @StringRes
    val author: Int
) {
    EMPTY( // Empty items helps for right positioning in mask previews.
        id = 0,
        drawable = -1,
        title = -1,
        author = -1
    ),
    DEER_FACE(
        id = 1,
        drawable = R.drawable.deer_face,
        R.string.mask_deer_face,
        author = R.string.mask_author
    ),
    SHOWER_HAT(
        id = 2,
        drawable = R.drawable.shower_hat,
        R.string.mask_shower_hat,
        author = R.string.mask_author
    ),
    DEER_HORNS(
        id = 3,
        drawable = R.drawable.deer_horns,
        R.string.mask_deer_horns,
        author = R.string.mask_author
    ),
    CARNIVAL_MASK(
        id = 4,
        drawable = R.drawable.carnival_mask,
        R.string.mask_carnival_mask,
        author = R.string.mask_author
    ),
    BEAR_NOSE_EARS(
        id = 5,
        drawable = R.drawable.bear_nose_ears,
        R.string.mask_bear_nose_ears,
        author = R.string.mask_author
    ),
    IRISH_HAT(
        id = 6,
        drawable = R.drawable.irish_hat,
        R.string.mask_irish_hat,
        author = R.string.mask_author
    )
}