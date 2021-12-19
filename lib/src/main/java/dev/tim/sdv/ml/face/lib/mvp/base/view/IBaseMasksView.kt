package dev.tim.sdv.ml.face.lib.mvp.base.view

import androidx.annotation.StringRes

interface IBaseMasksView : IView, dev.tim.sdv.ml.face.lib.utils.SnapOnScrollListener.OnSnapPositionChangeListener {
    fun setCarousel(masks: List<dev.tim.sdv.ml.face.lib.Mask>)
    fun setMaskToOverlayView(mask: dev.tim.sdv.ml.face.lib.Mask)
    fun setMaskTitle(@StringRes title: Int)
    fun setMaskAuthor(@StringRes author: Int)
}