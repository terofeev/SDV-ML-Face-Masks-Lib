package dev.tim.sdv.ml.face.lib.mvp.base.presenter

interface IBaseMasksPresenter<V : dev.tim.sdv.ml.face.lib.mvp.base.view.IBaseMasksView>:
    IPresenter<V> {
    fun onMaskSelectedPosition(position: Int)
}