package dev.tim.sdv.ml.face.lib.mvp.photo

interface PhotoContract {

    interface View : dev.tim.sdv.ml.face.lib.mvp.base.view.IBaseMasksView

    interface Presenter : dev.tim.sdv.ml.face.lib.mvp.base.presenter.IBaseMasksPresenter<View>
}