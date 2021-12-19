package dev.tim.sdv.ml.face.lib.mvp.camera

object CameraPresenterFactory :
    dev.tim.sdv.ml.face.lib.mvp.base.presenter.PresenterFactory<CameraContract.View, CameraContract.Presenter> {

    override fun create(): CameraContract.Presenter {
        return CameraPresenter()
    }
}