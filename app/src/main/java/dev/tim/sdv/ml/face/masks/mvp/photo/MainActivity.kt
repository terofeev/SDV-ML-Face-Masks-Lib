package dev.tim.sdv.ml.face.masks.mvp.photo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.tim.sdv.ml.face.lib.mvp.camera.CameraActivity
import dev.tim.sdv.ml.face.masks.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
        finish() // Optional
    }
}