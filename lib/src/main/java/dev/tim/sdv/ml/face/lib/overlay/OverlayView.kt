package dev.tim.sdv.ml.face.lib.overlay

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark
import kotlin.math.abs
import android.view.MotionEvent

class OverlayView(
    context: Context?,
    attrs: AttributeSet?
) : View(context, attrs) {

    // Detected face
    var face: Face? = null
        set(value) {
            field = value
            // Trigger redraw when a new detected face object is passed in
            postInvalidate()
        }

    var mask: dev.tim.sdv.ml.face.lib.Mask? = null
        set(value) {
            field = value
            if (value != null) {
                maskBitmap = BitmapFactory.decodeResource(resources, value.drawable)
            }
        }
    var maskBitmap: Bitmap? = null

    var isImageFlipped = false
    var previewWidth: Int? = null
    var previewHeight: Int? = null

    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f

    private var dX = 0f
    private var dY = 0f

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (face == null) {
            canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        }
        // Create local variables here so they cannot not be changed anywhere else
        val tmpFace: Face = face ?: return
        val tmpCanvas: Canvas = canvas ?: return
        val tmpMask: dev.tim.sdv.ml.face.lib.Mask = mask ?: return
        val tmpMaskBitmap = maskBitmap ?: return
        val tmpPreviewWidth: Int = previewWidth ?: return
        val tmpPreviewHeight: Int = previewHeight ?: return

        // Calculate scale factors
        widthScaleFactor = width.toFloat() / tmpPreviewWidth.toFloat()
        heightScaleFactor = height.toFloat() / tmpPreviewHeight.toFloat()

        drawMask(
            canvas = tmpCanvas,
            face = tmpFace,
            mask = tmpMask,
            maskBitmap = tmpMaskBitmap
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = x - event.rawX
                dY = y - event.rawY
            }
            MotionEvent.ACTION_MOVE -> animate()
                .x(event.rawX + dX)
                .y(event.rawY + dY)
                .setDuration(0)
                .start()
            else -> return false
        }
        return true
    }

    private fun drawMask(canvas: Canvas, face: Face, mask: dev.tim.sdv.ml.face.lib.Mask, maskBitmap: Bitmap) {
        when (mask) {
            dev.tim.sdv.ml.face.lib.Mask.DEER_FACE -> drawDeerFace(canvas, face, maskBitmap)
            dev.tim.sdv.ml.face.lib.Mask.SHOWER_HAT -> drawShowerHat(canvas, face, maskBitmap)
            dev.tim.sdv.ml.face.lib.Mask.DEER_HORNS -> drawDeerHorns(canvas, face, maskBitmap)
            dev.tim.sdv.ml.face.lib.Mask.CARNIVAL_MASK -> drawCarnivalMask(canvas, face, maskBitmap)
            dev.tim.sdv.ml.face.lib.Mask.BEAR_NOSE_EARS -> drawBearNoseEars(canvas, face, maskBitmap)
            dev.tim.sdv.ml.face.lib.Mask.IRISH_HAT -> drawIrishHat(canvas, face, maskBitmap)
            else -> { /*no-op*/
            }
        }
    }

    private fun drawDeerFace(canvas: Canvas, face: Face, maskBitmap: Bitmap) {
        val noseBase: FaceLandmark = face.getLandmark(FaceLandmark.NOSE_BASE) ?: return
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE) ?: return
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE) ?: return
        val eyeDistance = abs(leftEye.position.x - rightEye.position.x)
        val deltaX = widthScaleFactor * eyeDistance
        val deltaY = heightScaleFactor * eyeDistance
        val left = translateX(noseBase.position.x) - deltaX * 3f
        val top = translateY(noseBase.position.y) - deltaY - deltaY * 0.83f
        val right = translateX(noseBase.position.x).toInt() + deltaX * 2.6f
        val bottom = translateY(noseBase.position.y) + deltaY - deltaY * 0.83f
        val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        if (isImageFlipped) {
            canvas.rotate(
                face.headEulerAngleZ,
                translateX(noseBase.position.x),
                translateY(noseBase.position.y)
            )
        }
        canvas.drawBitmap(maskBitmap, null, rect, null)
    }

    private fun drawShowerHat(canvas: Canvas, face: Face, maskBitmap: Bitmap) {
        val noseBase: FaceLandmark = face.getLandmark(FaceLandmark.NOSE_BASE) ?: return
        val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR) ?: return
        val rightEar = face.getLandmark(FaceLandmark.RIGHT_EAR) ?: return
        val earDistance = abs(leftEar.position.x - rightEar.position.x)
        val delta = widthScaleFactor * earDistance / 2
        val left = translateX(rightEar.position.x) - delta * 0.9f
        val top = translateY(leftEar.position.y) - delta * 4f
        val right = translateX(leftEar.position.x) + delta * 1.1f
        val bottom = translateY(rightEar.position.y) - delta * 0.6f
        val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        if (isImageFlipped) {
            canvas.rotate(
                face.headEulerAngleZ,
                translateX(noseBase.position.x),
                translateY(noseBase.position.y)
            )
        }
        canvas.drawBitmap(maskBitmap, null, rect, null)
    }

    private fun drawDeerHorns(canvas: Canvas, face: Face, maskBitmap: Bitmap) {
        val noseBase: FaceLandmark = face.getLandmark(FaceLandmark.NOSE_BASE) ?: return
        val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR) ?: return
        val rightEar = face.getLandmark(FaceLandmark.RIGHT_EAR) ?: return
        val earDistance = abs(leftEar.position.x - rightEar.position.x)
        val delta = widthScaleFactor * earDistance / 2
        val left = translateX(rightEar.position.x) - delta * 0.9f
        val top = translateY(leftEar.position.y) - delta * 6f
        val right = translateX(leftEar.position.x) + delta * 1.1f
        val bottom = translateY(rightEar.position.y) - delta * 2f
        val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        if (isImageFlipped) {
            canvas.rotate(
                face.headEulerAngleZ,
                translateX(noseBase.position.x),
                translateY(noseBase.position.y)
            )
        }
        canvas.drawBitmap(maskBitmap, null, rect, null)
    }

    private fun drawCarnivalMask(canvas: Canvas, face: Face, maskBitmap: Bitmap) {
        val noseBase: FaceLandmark = face.getLandmark(FaceLandmark.NOSE_BASE) ?: return
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE) ?: return
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE) ?: return
        val eyeDistance = abs(leftEye.position.x - rightEye.position.x)
        val deltaX = widthScaleFactor * eyeDistance
        val deltaY = heightScaleFactor * eyeDistance
        val left = translateX(noseBase.position.x) - deltaX * 2f
        val top = translateY(noseBase.position.y) - deltaY - deltaY * 0.83f
        val right = translateX(noseBase.position.x).toInt() + deltaX * 2.4f
        val bottom = translateY(noseBase.position.y) + deltaY - deltaY * 0.5f
        val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        if (isImageFlipped) {
            canvas.rotate(
                face.headEulerAngleZ,
                translateX(noseBase.position.x),
                translateY(noseBase.position.y)
            )
        }
        canvas.drawBitmap(maskBitmap, null, rect, null)
    }

    private fun drawBearNoseEars(canvas: Canvas, face: Face, maskBitmap: Bitmap) {
        val noseBase: FaceLandmark = face.getLandmark(FaceLandmark.NOSE_BASE) ?: return
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE) ?: return
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE) ?: return
        val eyeDistance = abs(leftEye.position.x - rightEye.position.x)
        val deltaX = widthScaleFactor * eyeDistance
        val deltaY = heightScaleFactor * eyeDistance
        val left = translateX(noseBase.position.x) - deltaX * 2f
        val top = translateY(noseBase.position.y) - deltaY - deltaY * 0.83f
        val right = translateX(noseBase.position.x).toInt() + deltaX * 2.4f
        val bottom = translateY(noseBase.position.y) + deltaY - deltaY * 0.83f
        val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        if (isImageFlipped) {
            canvas.rotate(
                face.headEulerAngleZ,
                translateX(noseBase.position.x),
                translateY(noseBase.position.y)
            )
        }
        canvas.drawBitmap(maskBitmap, null, rect, null)
    }

    private fun drawIrishHat(canvas: Canvas, face: Face, maskBitmap: Bitmap) {
        val noseBase: FaceLandmark = face.getLandmark(FaceLandmark.NOSE_BASE) ?: return
        val leftEar = face.getLandmark(FaceLandmark.LEFT_EAR) ?: return
        val rightEar = face.getLandmark(FaceLandmark.RIGHT_EAR) ?: return
        val earDistance = abs(leftEar.position.x - rightEar.position.x)
        val delta = widthScaleFactor * earDistance / 2
        val left = translateX(rightEar.position.x) - delta * 1.1f
        val top = translateY(leftEar.position.y) - delta * 4.5f
        val right = translateX(leftEar.position.x) + delta * 1.4f
        val bottom = translateY(rightEar.position.y) - delta * 0.01f
        val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        if (isImageFlipped) {
            canvas.rotate(
                face.headEulerAngleZ,
                translateX(noseBase.position.x),
                translateY(noseBase.position.y)
            )
        }
        canvas.drawBitmap(maskBitmap, null, rect, null)
    }

    /**
     * Adjusts the x coordinate from the preview's coordinate system to the view coordinate system.
     */
    private fun translateX(x: Float): Float {
        return if (isImageFlipped) width - scaleX(x) else scaleX(x)
    }

    /**
     * Adjusts the y coordinate from the preview's coordinate system to the view coordinate system.
     */
    private fun translateY(y: Float): Float {
        return scaleY(y)
    }

    /**
     * Adjusts a horizontal value of the supplied value from the preview scale to the view scale.
     */
    private fun scaleX(x: Float): Float {
        return x * widthScaleFactor
    }


    /**
     * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
     */
    private fun scaleY(y: Float): Float {
        return y * heightScaleFactor
    }

    companion object {
        private const val TAG = "OverlayView"
        private val LOGS = false
    }
}