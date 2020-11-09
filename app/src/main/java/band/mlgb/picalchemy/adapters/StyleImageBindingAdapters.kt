package band.mlgb.picalchemy.adapters

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageFromUri")
fun bindImageFromUri(view: ImageView, uri: Uri) {
    Glide.with(view.context)
        .load(uri)
        .centerInside()
        .into(view)
}