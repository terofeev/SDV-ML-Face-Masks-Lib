package dev.tim.sdv.ml.face.lib.mvp.photo

class PhotoPresenter : dev.tim.sdv.ml.face.lib.mvp.base.presenter.BaseMasksPresenter<PhotoContract.View>(),
    PhotoContract.Presenter {

    override val logTag: String = "PhotoPresenter"
}