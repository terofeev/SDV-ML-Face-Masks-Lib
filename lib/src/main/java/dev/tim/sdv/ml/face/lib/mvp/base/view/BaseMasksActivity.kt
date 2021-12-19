package dev.tim.sdv.ml.face.lib.mvp.base.view

import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageProxy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import dev.tim.sdv.ml.face.lib.R
import dev.tim.sdv.ml.face.lib.mvp.base.presenter.IBaseMasksPresenter
import dev.tim.sdv.ml.face.lib.overlay.OverlayView

abstract class BaseMasksActivity<P : IBaseMasksPresenter<V>, V : IBaseMasksView> :
    BaseActivity<P, V>(), IBaseMasksView {

    private var selectorAdapter: dev.tim.sdv.ml.face.lib.selector.SelectorAdapter? = null
    private var snapHelper: PagerSnapHelper? = null
    private var snapOnScrollListener: dev.tim.sdv.ml.face.lib.utils.SnapOnScrollListener? = null

    private var sourceInfo: dev.tim.sdv.ml.face.lib.info.SourceInfo =
        dev.tim.sdv.ml.face.lib.info.SourceInfo(
            width = -1,
            height = -1,
            isImageFlipped = false
        )

    private val maskSelectorRecyclerView: RecyclerView
        get() = findViewById(R.id.maskSelectorRecyclerView)
    private val overlayView: OverlayView
        get() = findViewById(R.id.overlayView)
    private val maskTitleText: TextView
        get() = findViewById(R.id.maskTitleText)
    private val maskSubTitleText: TextView
        get() = findViewById(R.id.maskSubTitleText)

    protected var imageProcessor: dev.tim.sdv.ml.face.lib.detector.FaceDetectorProcessor? = null
    protected var lens = CameraSelector.LENS_FACING_FRONT

    override fun onStop() {
        imageProcessor?.stop()
        imageProcessor = null

        maskSelectorRecyclerView.onFlingListener = null
        maskSelectorRecyclerView.clearOnScrollListeners()
        selectorAdapter = null
        snapHelper = null
        snapOnScrollListener = null
        super.onStop()
    }

    final override fun setCarousel(masks: List<dev.tim.sdv.ml.face.lib.Mask>) {
        if (selectorAdapter == null) {
            selectorAdapter = dev.tim.sdv.ml.face.lib.selector.SelectorAdapter(masks)
            maskSelectorRecyclerView.adapter = selectorAdapter
            maskSelectorRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
        if (snapHelper == null) {
            snapHelper = PagerSnapHelper()
            snapHelper?.attachToRecyclerView(maskSelectorRecyclerView)
        }
        if (snapOnScrollListener == null) {
            snapOnScrollListener = dev.tim.sdv.ml.face.lib.utils.SnapOnScrollListener(
                snapHelper,
                dev.tim.sdv.ml.face.lib.utils.SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                this
            )
            snapOnScrollListener?.let { listener -> maskSelectorRecyclerView.addOnScrollListener(listener) }
        }
    }

    @CallSuper
    override fun setMaskToOverlayView(mask: dev.tim.sdv.ml.face.lib.Mask) {
        overlayView.mask = mask
    }

    final override fun setMaskTitle(@StringRes title: Int) {
        maskTitleText.setText(title)
    }

    final override fun setMaskAuthor(author: Int) {
        maskSubTitleText.setText(author)
    }

    final override fun onSnapPositionChange(position: Int) {
        oneShotHapticFeedback()
        presenter?.onMaskSelectedPosition(position)
    }

    protected fun onFacesDetected(faces: List<Face>) {
        val face: Face? = faces.firstOrNull()
        overlayView.visibility = if (face == null) {
            View.GONE
        } else {
            View.VISIBLE
        }
        overlayView.face = face
    }

    protected fun onSetSourceInfo(sourceInfo: dev.tim.sdv.ml.face.lib.info.SourceInfo) {
        this.sourceInfo = sourceInfo
        overlayView.isImageFlipped = sourceInfo.isImageFlipped
        overlayView.previewWidth = sourceInfo.width
        overlayView.previewHeight = sourceInfo.height
    }

    protected fun obtainSourceInfo(inputImage: InputImage): dev.tim.sdv.ml.face.lib.info.SourceInfo {
        val rotationDegrees = inputImage.rotationDegrees
        return if (rotationDegrees == 0 || rotationDegrees == 180) {
            dev.tim.sdv.ml.face.lib.info.SourceInfo(
                height = inputImage.height,
                width = inputImage.width,
                isImageFlipped = false
            )
        } else {
            dev.tim.sdv.ml.face.lib.info.SourceInfo(
                height = inputImage.width,
                width = inputImage.height,
                isImageFlipped = false
            )
        }
    }

    protected fun obtainSourceInfo(lens: Int, imageProxy: ImageProxy): dev.tim.sdv.ml.face.lib.info.SourceInfo {
        val isImageFlipped = lens == CameraSelector.LENS_FACING_FRONT
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
        return if (rotationDegrees == 0 || rotationDegrees == 180) {
            dev.tim.sdv.ml.face.lib.info.SourceInfo(
                height = imageProxy.height,
                width = imageProxy.width,
                isImageFlipped = isImageFlipped
            )
        } else {
            dev.tim.sdv.ml.face.lib.info.SourceInfo(
                height = imageProxy.width,
                width = imageProxy.height,
                isImageFlipped = isImageFlipped
            )
        }
    }
}