package band.mlgb.picalchemy

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import band.mlgb.picalchemy.adapters.StyleListAdapter
import band.mlgb.picalchemy.databinding.FragmentAlchemyBinding
import band.mlgb.picalchemy.tensorflow.StyleTransferer
import band.mlgb.picalchemy.viewModels.ImageViewModel
import band.mlgb.picalchemy.viewModels.StyleListViewModel
import band.mlgb.picalchemy.views.UriPickedListener
import kotlinx.coroutines.launch

class AlchemyFragment : Fragment(), UriPickedListener {
    private val styleListViewModel: StyleListViewModel by viewModels {
        StyleListViewModel.providerFactory(requireActivity().assets)
    }

    private val inputImageViewModel: ImageViewModel by activityViewModels()

    // this view model only handles the merged result
    private val resultImageViewModel: ImageViewModel by viewModels()

    private val styleTransferer: StyleTransferer by lazy {
        StyleTransferer(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAlchemyBinding.inflate(inflater)

        with(StyleListAdapter(this)) {
            binding.styleList.adapter = this
            styleListViewModel.styleList.observe(viewLifecycleOwner) {
                this.submitList(it)
            }
        }
        // initially display selected image from gallery fragment
        inputImageViewModel.image.observe(viewLifecycleOwner) {
            binding.uri = it
            binding.executePendingBindings()
        }

        // later if a result image is calculated, display the result image
        resultImageViewModel.image.observe(viewLifecycleOwner) {
            binding.uri = it
            binding.executePendingBindings()
        }

        return binding.root
    }

    // unused
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }


    // An style file uri is picked
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onUriPicked(styleFileUri: Uri) {
        inputImageViewModel.image.value?.let { inputContentUri ->
            // input from camera: content://band.mlgb.picalchemy.fileprovider/my_images/Pictures/JPEG_20201113_163511_5835964646040696703.jpg
            // input from gallery: content://media/external/images/media/11525
            inputImageViewModel.viewModelScope.launch {
                styleTransferer.transferStyle(
                    styleFileUri,
                    inputContentUri,
                    resultImageViewModel,
                    requireActivity(),
                )
            }
        }
    }
}