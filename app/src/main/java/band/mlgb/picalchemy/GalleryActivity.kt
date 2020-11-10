package band.mlgb.picalchemy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import band.mlgb.picalchemy.adapters.GalleryAdapter
import band.mlgb.picalchemy.databinding.ActivityGalleryBinding
import band.mlgb.picalchemy.viewModels.GalleryViewModel

// Activity to display local photo gallery
class GalleryActivity : AppCompatActivity() {
    private val viewMainActivity: GalleryViewModel by viewModels {
        GalleryViewModel.providerFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(GalleryAdapter()) {
            binding.gallery.adapter = this
            viewMainActivity.imageUris.observe(this@GalleryActivity) {
                submitList(it)
            }
        }


    }
}