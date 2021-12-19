package dev.tim.sdv.ml.face.lib.mvp.photo

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetectorOptions
import dev.tim.sdv.ml.face.lib.Mask
import dev.tim.sdv.ml.face.lib.R
import dev.tim.sdv.ml.face.lib.detector.FaceDetectorProcessor
import dev.tim.sdv.ml.face.lib.info.SourceInfo
import dev.tim.sdv.ml.face.lib.mvp.base.presenter.PresenterFactory
import dev.tim.sdv.ml.face.lib.mvp.base.view.BaseMasksActivity
import dev.tim.sdv.ml.face.lib.utils.PhotoUtils.loadPhoto
import kotlinx.android.synthetic.main.activity_photo.*
import java.io.IOException

class PhotoActivity : BaseMasksActivity<PhotoContract.Presenter, PhotoContract.View>(),
    PhotoContract.View {

    private var glideBitmap: Bitmap? = null
    private var inputBitmap: Bitmap? = null

    override val logTag: String = "PhotoActivity"

    override fun getPresenterFactory(): PresenterFactory<PhotoContract.View, PhotoContract.Presenter> {
        return PhotoPresenterFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        imageCompletedImageView.setOnClickListener {
            finish()
            super.onBackPressed()
        }

        val photoUri = intent.getParcelableExtra<Uri>(EXTRA_PHOTO_URI)
        Glide.with(photoImageView)
            .load(photoUri)
            .into(photoImageView)
        photoUri?.let { startRecognition(photoUri = it, ::onSetSourceInfo, ::onFacesDetected) }
    }

    override fun setMaskToOverlayView(mask: Mask) {
        super.setMaskToOverlayView(mask)
        overlayView.postInvalidate()
    }

    private fun startRecognition(
        photoUri: Uri,
        setSourceInfo: (SourceInfo) -> Unit,
        onFacesDetected: (List<Face>) -> Unit
    ) {
        try {
            inputBitmap = loadPhoto(this, photoUri)
        } catch (e: IOException) {
            Log.e(logTag, "Failed to load file: $photoUri", e)
            Toast.makeText(this, "Failed to load file!", Toast.LENGTH_SHORT).show()
            return
        }

        imageProcessor = try {
            FaceDetectorProcessor(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        } catch (e: Exception) {
            Log.e(logTag, "Can not create image processor", e)
            return
        }
        val inputImage = InputImage.fromBitmap(inputBitmap!!, 0)
        setSourceInfo.invoke(obtainSourceInfo(inputImage))
        imageProcessor?.processInputImage(inputImage, onFacesDetected)
    }

    companion object {
        const val EXTRA_PHOTO_URI = "extra_photo_uri"
    }
}