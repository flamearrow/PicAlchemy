package band.mlgb.picalchemy.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import band.mlgb.picalchemy.databinding.StyleItemBinding
import band.mlgb.picalchemy.debugBGLM

// The adapter will be expanded as user add new styles
// Note the Adapter already accests a list of Uri as input
class StyleListAdapter : ListAdapter<Uri, StyleViewHolder>(StyleDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StyleViewHolder {
        return StyleViewHolder(StyleItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: StyleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class StyleViewHolder(private val binding: StyleItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.setOnClickListener {
            debugBGLM("I got clicked, " + binding.uri)
        }
    }


    fun bind(uri: Uri) {
        with(binding) {
            this.uri = uri
            // data changed, let the data adapter in side xml to execute the binding and update the view
            executePendingBindings()
        }

    }
}

class StyleDiffCallback : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
        return oldItem == newItem
    }


}
