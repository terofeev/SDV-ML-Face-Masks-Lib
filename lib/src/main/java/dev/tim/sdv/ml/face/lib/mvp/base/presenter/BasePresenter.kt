package dev.tim.sdv.ml.face.lib.mvp.base.presenter

import android.util.Log

abstract class BasePresenter<V : dev.tim.sdv.ml.face.lib.mvp.base.view.IView> : IPresenter<V> {

    protected abstract val logTag: String

    /**
     * The view, will be null if the presenter isn't attached to a view
     */
    protected var view: V? = null

    override fun onViewAttached(view: V) {
        Log.i(logTag, "onViewAttached")
        this.view = view
    }

    override fun onStart(viewCreated: Boolean) {
        Log.i(logTag, "onStart viewCreated=$viewCreated")
    }

    override fun onStop() {
        Log.i(logTag, "onStop")
    }

    override fun onViewDetached() {
        Log.i(logTag, "onViewDetached")
        view = null
    }

    override fun onFinish() {
        Log.i(logTag, "onFinish")
    }
}