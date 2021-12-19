package dev.tim.sdv.ml.face.lib.mvp.base.presenter

interface IPresenter<V : dev.tim.sdv.ml.face.lib.mvp.base.view.IView> {

    /**
     * Called when the view is attached to the presenter. Presenters should normally not use this
     * method since it's only used to link the view to the presenter which is done by the base impl.
     */
    fun onViewAttached(view: V)

    /**
     * Called every time the view starts, the view is guarantee to be not null starting at this
     * method, until {@link #onStop()} is called.
     *
     * @param viewCreated true if it's the has been just created, false if its just restarting after a stop
     */
    fun onStart(viewCreated: Boolean)

    /**
     * Called every time the view stops. After this method, the view will be null.
     */
    fun onStop()

    /**
     * Called when the view is detached from the presenter. Presenters should normally not use this
     * method since it's only used to unlink the view from the presenter which is done by the base impl.
     */
    fun onViewDetached()

    /**
     * Called when the presenter is definitely destroyed, you should use this method only to release
     * any resource used by the presenter (cancel HTTP requests, close database connection...)
     */
    fun onFinish()
}