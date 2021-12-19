package dev.tim.sdv.ml.face.lib.mvp.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.face.Face
import dev.tim.sdv.ml.face.lib.R
import dev.tim.sdv.ml.face.lib.mvp.photo.PhotoActivity
import dev.tim.sdv.ml.face.lib.mvp.photo.PhotoActivity.Companion.EXTRA_PHOTO_URI
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : dev.tim.sdv.ml.face.lib.mvp.base.view.BaseMasksActivity<CameraContract.Presenter, CameraContract.View>(),
    CameraContract.View {

    override val logTag: String = "CameraActivity"

    override fun getPresenterFactory(): dev.tim.sdv.ml.face.lib.mvp.base.presenter.PresenterFactory<CameraContract.View, CameraContract.Presenter> {
        return CameraPresenterFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        galleryPickerImageView.setOnClickListener { openGallery() }
        galleryPickerPlusImageView.setOnClickListener { openGallery() }
    }

    override fun onStart() {
        super.onStart()
        checkAndStartCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "$logTag camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!

                val intent = Intent(this, PhotoActivity::class.java).apply {
                    putExtra(EXTRA_PHOTO_URI, uri)
                }
                startActivity(intent)
                finish()
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAndStartCamera() {
        if (permissionGranted()) {
            startCamera()
        } else {
            requestPermission()
        }
    }

    private fun startCamera() {
        startCamera(::onSetSourceInfo, ::onFacesDetected)
    }

    private fun startCamera(
        setSourceInfo: (dev.tim.sdv.ml.face.lib.info.SourceInfo) -> Unit,
        onFacesDetected: (List<Face>) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // Analysis
            val analysis = getImageAnalysisUseCase(lens, setSourceInfo, onFacesDetected)

            // Select front camera as a default
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, analysis
                )

            } catch (exc: Exception) {
                Log.e(logTag, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun getImageAnalysisUseCase(
        lens: Int,
        setSourceInfo: (dev.tim.sdv.ml.face.lib.info.SourceInfo) -> Unit,
        onFacesDetected: (List<Face>) -> Unit
    ): ImageAnalysis? {
        imageProcessor = try {
            dev.tim.sdv.ml.face.lib.detector.FaceDetectorProcessor()
        } catch (e: Exception) {
            Log.e(logTag, "Can not create image processor", e)
            return null
        }
        val builder = ImageAnalysis.Builder()
        val analysisUseCase = builder.build()

        var sourceInfoUpdated = false

        analysisUseCase.setAnalyzer(
            TaskExecutors.MAIN_THREAD,
            { imageProxy: ImageProxy ->
                if (!sourceInfoUpdated) {
                    setSourceInfo(obtainSourceInfo(lens, imageProxy))
                    sourceInfoUpdated = true
                }
                try {
                    imageProcessor?.processImageProxy(imageProxy, onFacesDetected)
                } catch (e: MlKitException) {
                    Log.e(logTag, "Failed to process image. Error: " + e.localizedMessage)
                }
            }
        )
        return analysisUseCase
    }

    private fun permissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA), 0
        )
    }

    private fun openGallery() {
        oneShotHapticFeedback()

        ImagePicker.with(this)
            .galleryOnly()
            .start()
    }
}