package dev.tim.sdv.ml.face.lib.mvp.base.presenter

interface PresenterFactory<V : dev.tim.sdv.ml.face.lib.mvp.base.view.IView, P : IPresenter<V>> {

    fun create(): P
}