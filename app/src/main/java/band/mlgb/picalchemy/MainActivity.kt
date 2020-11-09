package band.mlgb.picalchemy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import band.mlgb.picalchemy.adapters.StyleListAdapter
import band.mlgb.picalchemy.databinding.ActivityMainBinding
import band.mlgb.picalchemy.viewModels.StyleListViewModel

// Full screen activity
class MainActivity : AppCompatActivity() {

    private val viewModel: StyleListViewModel by viewModels {
        StyleListViewModel.providerFactory(assets)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(StyleListAdapter()) {
            binding.styleList.adapter = this
            viewModel.styleList.observe(this@MainActivity) {
                this.submitList(it)
            }
        }

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}