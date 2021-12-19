package dev.tim.sdv.ml.face.lib.detector

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectorProcessor(
    performanceMode: Int = FaceDetectorOptions.PERFORMANCE_MODE_FAST
) {

    private val detector: FaceDetector

    private val executor = TaskExecutors.MAIN_THREAD

    init {
        val faceDetectorOptions = FaceDetectorOptions.Builder()
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .setPerformanceMode(performanceMode)
            .setMinFaceSize(0.4f)
            .build()

        detector = FaceDetection.getClient(faceDetectorOptions)
    }

    fun stop() {
        detector.close()
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun processImageProxy(image: ImageProxy, onDetectionFinished: (List<Face>) -> Unit) {
        detector.process(InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees))
            .addOnSuccessListener(executor) { results: List<Face> -> onDetectionFinished(results) }
            .addOnFailureListener(executor) { e: Exception ->
                Log.e(TAG, "Error detecting face", e)
            }
            .addOnCompleteListener { image.close() }
    }

    fun processInputImage(inputImage: InputImage, onDetectionFinished: (List<Face>) -> Unit) {
        detector.process(inputImage)
            .addOnSuccessListener(executor) { results: List<Face> -> onDetectionFinished(results) }
            .addOnFailureListener(executor) { e: Exception ->
                Log.e(TAG, "Error detecting face", e)
            }
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }
}