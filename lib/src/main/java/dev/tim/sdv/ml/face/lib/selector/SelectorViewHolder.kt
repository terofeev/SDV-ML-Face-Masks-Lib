package dev.tim.sdv.ml.face.lib.selector

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dev.tim.sdv.ml.face.lib.R

class SelectorViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val maskImageView: ImageView = view.findViewById(R.id.maskImageView)
}