package dev.tim.sdv.ml.face.lib.mvp.photo

object PhotoPresenterFactory :
    dev.tim.sdv.ml.face.lib.mvp.base.presenter.PresenterFactory<PhotoContract.View, PhotoContract.Presenter> {

    override fun create(): PhotoContract.Presenter {
        return PhotoPresenter()
    }
}