package band.mlgb.picalchemy.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import band.mlgb.picalchemy.UriPickedListener
import band.mlgb.picalchemy.databinding.GalleryItemBinding
import band.mlgb.picalchemy.debugBGLM

class GalleryAdapter(private val uriPickedListener: UriPickedListener) :
    ListAdapter<Uri, GalleryViewHolder>(GalleryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            GalleryItemBinding.inflate(LayoutInflater.from(parent.context)),
            uriPickedListener
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class GalleryViewHolder(
    private val binding: GalleryItemBinding,
    private val uriPickedListener: UriPickedListener
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.setOnClickListener {
            debugBGLM("I'm clicked, " + binding.uri)
            binding.uri?.let {
                uriPickedListener.onUriPicked(it)
            }
        }
    }

    fun bind(uri: Uri) {
        binding.uri = uri
        binding.executePendingBindings()
    }
}

class GalleryDiffCallback : DiffUtil.ItemCallback<Uri>() {

    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }
}