package band.mlgb.picalchemy.adapters

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import band.mlgb.picalchemy.R
import com.bumptech.glide.Glide

@BindingAdapter("bindGalleryImage")
fun bindGalleryImage(view: ImageView, uri: Uri) {
    Glide.with(view.context)
        .load(uri)
        .centerCrop()
        .placeholder(R.drawable.placeholder)
        .into(view)
}

@BindingAdapter("bindStyleImage")
fun bindStyleImage(view: ImageView, uri: Uri) {
    Glide.with(view.context)
        .load(uri)
        .centerInside()
        .into(view)
}