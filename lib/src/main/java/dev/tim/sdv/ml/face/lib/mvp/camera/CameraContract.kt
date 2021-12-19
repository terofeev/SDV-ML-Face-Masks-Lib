package dev.tim.sdv.ml.face.lib.mvp.camera

interface CameraContract {

    interface View : dev.tim.sdv.ml.face.lib.mvp.base.view.IBaseMasksView

    interface Presenter : dev.tim.sdv.ml.face.lib.mvp.base.presenter.IBaseMasksPresenter<View>
}