package dev.tim.sdv.ml.face.lib.mvp.photo

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

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
            .asBitmap()
            .load(photoUri)
            .override(640, 480)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    glideBitmap = resource
                    photoImageView.setImageBitmap(resource)
                    if (permissionGranted()) {
                        val uri = saveBitmapToFile(resource)
                        startRecognition(photoUri = uri, ::onSetSourceInfo, ::onFacesDetected)
                    } else {
                        requestPermission()
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 glideBitmap?.let {
                    val uri = saveBitmapToFile(it)
                    startRecognition(photoUri = uri, ::onSetSourceInfo, ::onFacesDetected)
                }
            } else {
                Toast.makeText(this, "$logTag storage permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun setMaskToOverlayView(mask: Mask) {
        super.setMaskToOverlayView(mask)
        overlayView.postInvalidate()
    }

    private fun saveBitmapToFile(resource: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        resource.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            contentResolver,
            resource,
            UUID.randomUUID().toString().toString() + ".png",
            "drawing"
        )
        return Uri.parse(path)
    }

    private fun permissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0
        )
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