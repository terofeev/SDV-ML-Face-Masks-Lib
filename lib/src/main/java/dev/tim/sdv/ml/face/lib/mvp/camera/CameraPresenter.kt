package dev.tim.sdv.ml.face.lib.mvp.camera

class CameraPresenter : dev.tim.sdv.ml.face.lib.mvp.base.presenter.BaseMasksPresenter<CameraContract.View>(),
    CameraContract.Presenter {

    override val logTag: String = "CameraPresenter"
}