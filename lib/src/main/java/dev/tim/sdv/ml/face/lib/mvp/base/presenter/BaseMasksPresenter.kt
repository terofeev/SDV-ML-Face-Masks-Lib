package dev.tim.sdv.ml.face.lib.mvp.base.presenter

import android.util.Log
import dev.tim.sdv.ml.face.lib.utils.next
import dev.tim.sdv.ml.face.lib.utils.previous

abstract class BaseMasksPresenter<V : dev.tim.sdv.ml.face.lib.mvp.base.view.IBaseMasksView> : BasePresenter<V>(),
    IBaseMasksPresenter<V> {

    private var currentMaskPosition = DEFAULT_MASK_POSITION
    private var selectedMask = DEFAULT_MASK

    override fun onViewAttached(view: V) {
        super.onViewAttached(view)
        setCarousel()
        updateMaskData()
    }

    final override fun onMaskSelectedPosition(position: Int) {
        Log.i(logTag, "onMaskSelectedPosition=$position")
        if (position > currentMaskPosition) {
            selectedMask = selectedMask.next()
            updateMaskData()
        } else if (position < currentMaskPosition) {
            selectedMask = selectedMask.previous()
            updateMaskData()
        }
        currentMaskPosition = position
    }

    private fun setCarousel() {
        view?.setCarousel(CAROUSEL_MASKS)
    }

    private fun updateMaskData() {
        view?.setMaskToOverlayView(selectedMask)
        view?.setMaskTitle(selectedMask.title)
        view?.setMaskAuthor(selectedMask.author)
    }

    companion object {
        private const val DEFAULT_MASK_POSITION = 1
        private val DEFAULT_MASK = dev.tim.sdv.ml.face.lib.Mask.DEER_FACE
        private val CAROUSEL_MASKS = listOf(
            dev.tim.sdv.ml.face.lib.Mask.EMPTY,
            dev.tim.sdv.ml.face.lib.Mask.DEER_FACE,
            dev.tim.sdv.ml.face.lib.Mask.SHOWER_HAT,
            dev.tim.sdv.ml.face.lib.Mask.DEER_HORNS,
            dev.tim.sdv.ml.face.lib.Mask.CARNIVAL_MASK,
            dev.tim.sdv.ml.face.lib.Mask.BEAR_NOSE_EARS,
            dev.tim.sdv.ml.face.lib.Mask.IRISH_HAT,
            dev.tim.sdv.ml.face.lib.Mask.EMPTY
        )
    }
}