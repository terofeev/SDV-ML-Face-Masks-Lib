package dev.tim.sdv.ml.face.lib.selector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.tim.sdv.ml.face.lib.R

class SelectorAdapter(private val dataSet: List<dev.tim.sdv.ml.face.lib.Mask>):
    RecyclerView.Adapter<SelectorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mask, parent, false)
        return SelectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {
        when (val mask = dataSet[position]) {
            dev.tim.sdv.ml.face.lib.Mask.EMPTY -> holder.maskImageView.setImageDrawable(null)
            else -> Glide.with(holder.itemView.context)
                .load(mask.drawable)
                .fitCenter()
                .into(holder.maskImageView)
        }
    }

    override fun getItemCount(): Int = dataSet.size
}