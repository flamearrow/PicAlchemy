package band.mlgb.picalchemy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import band.mlgb.picalchemy.adapters.StyleListAdapter
import band.mlgb.picalchemy.databinding.FragmentAlchemyBinding
import band.mlgb.picalchemy.viewModels.InputImageViewModel
import band.mlgb.picalchemy.viewModels.StyleListViewModel

class AlchemyFragment : Fragment() {
    private val styleListViewModel: StyleListViewModel by viewModels {
        StyleListViewModel.providerFactory(requireActivity().assets)
    }

    private val inputImageViewModel: InputImageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAlchemyBinding.inflate(inflater)

        with(StyleListAdapter()) {
            binding.styleList.adapter = this
            styleListViewModel.styleList.observe(viewLifecycleOwner) {
                this.submitList(it)
            }
        }

        inputImageViewModel.imagePicked.observe(viewLifecycleOwner) {
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
}