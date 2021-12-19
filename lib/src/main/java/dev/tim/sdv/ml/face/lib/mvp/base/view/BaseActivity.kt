package dev.tim.sdv.ml.face.lib.mvp.base.view

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dev.tim.sdv.ml.face.lib.mvp.base.presenter.IPresenter
import dev.tim.sdv.ml.face.lib.mvp.base.presenter.PresenterFactory

abstract class BaseActivity<P : IPresenter<V>, V : IView> : AppCompatActivity() {

    protected abstract val logTag: String

    /**
     * Is this the first start of the activity (after onCreate)
     */
    private var firstStart: Boolean = false

    private var vibrator: Vibrator? = null

    /**
     * The presenter for this view
     */
    protected var presenter: P? = null

    /**
     * Get the presenter factory implementation for this view
     *
     * @return the presenter factory
     */
    protected abstract fun getPresenterFactory(): PresenterFactory<V, P>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstStart = true
        presenter = getPresenterFactory().create()
        vibrator = getSystemService(VIBRATOR_SERVICE) as? Vibrator
    }

    override fun onStart() {
        super.onStart()
        if (presenter != null) doStart()
    }

    override fun onStop() {
        if (presenter != null) doStop()
        super.onStop()
    }

    override fun onDestroy() {
        presenter = null
        super.onDestroy()
    }

    protected fun oneShotHapticFeedback() {
        val vib = vibrator ?: return
        if (vib.hasVibrator() && Build.VERSION.SDK_INT >= 26) {
            val effect = VibrationEffect.createOneShot(HAPTIC_FEEDBACK_DURATION, DEFAULT_AMPLITUDE)
            vib.vibrate(effect)
        }
    }

    /**
     * Call the presenter callbacks for onStart
     */
    private fun doStart() {
        Log.i(logTag, "doStart firstStart=$firstStart")
        presenter?.onViewAttached(this as V)
        presenter?.onStart(firstStart)
        firstStart = false
    }

    /**
     * Call the presenter callbacks for onStop
     */
    private fun doStop() {
        Log.i(logTag, "doStop")
        presenter?.onStop()
        presenter?.onViewDetached()
    }

    private companion object {
        private const val HAPTIC_FEEDBACK_DURATION = 5L
    }
}