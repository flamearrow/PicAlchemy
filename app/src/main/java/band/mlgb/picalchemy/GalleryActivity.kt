package band.mlgb.picalchemy

import android.os.Bundle
import android.view.View
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
        ActivityGalleryBinding.inflate(layoutInflater).let { binding ->
            setContentView(binding.root)
            with(GalleryAdapter()) {
                binding.gallery.adapter = this
                binding.setOnClickListener {
                    debugBGLM("i got clicked, open camera and pick a picture")
                }
                viewMainActivity.imageUris.observe(this@GalleryActivity) {
                    submitList(it)
                }
            }
        }


    }
}