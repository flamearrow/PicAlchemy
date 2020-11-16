package band.mlgb.picalchemy

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import band.mlgb.picalchemy.adapters.StyleListAdapter
import band.mlgb.picalchemy.databinding.FragmentAlchemyBinding
import band.mlgb.picalchemy.tensorflow.StyleTransferer
import band.mlgb.picalchemy.utils.debugBGLM
import band.mlgb.picalchemy.utils.saveUriToGallery
import band.mlgb.picalchemy.viewModels.ImageViewModel
import band.mlgb.picalchemy.viewModels.StyleListViewModel
import band.mlgb.picalchemy.views.UriPickedListener
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlchemyFragment : Fragment(), UriPickedListener, View.OnClickListener, View.OnTouchListener {
    private val styleListViewModel: StyleListViewModel by viewModels {
        StyleListViewModel.providerFactory(requireActivity().assets)
    }

    private val inputImageViewModel: ImageViewModel by activityViewModels()

    // this view model only handles the merged result
    private val resultImageViewModel: ImageViewModel by viewModels()

    @Inject
    lateinit var styleTransferer: StyleTransferer

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //        DaggerPicAlchemyComponent.builder().tensorflowModule(TensorflowModule(requireContext())).
        //            .build().inject(this)
        // Instead of creating a new component, access the activity scoped subcomponent from activity
        (activity as AlchemyActivity).alchemoyComponent.inject(this)

        val binding = FragmentAlchemyBinding.inflate(inflater)

        binding.onClickListener = this
        with(StyleListAdapter(this)) {
            binding.styleList.adapter = this
            styleListViewModel.styleList.observe(viewLifecycleOwner) {
                this.submitList(it)
            }
        }
        binding.result.setOnTouchListener(this)
        binding.toggle.setOnTouchListener(this)
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
                // TODO: change image to loading animation
                // will start on a separate thread, main
                styleTransferer.transferStyle(
                    styleFileUri,
                    inputContentUri,
                    requireActivity(),
                )?.let {
                    debugBGLM("posting value on main")
                    resultImageViewModel.image.postValue(it)
                } ?: run {
                    // reset original image
                    inputImageViewModel.repostIfNotNull()
                }
            }
        }
    }


    override fun onClick(view: View?) {
        view?.also {
            when (it.id) {
                R.id.share -> {
                    debugBGLM("share")
                    resultImageViewModel.image.value?.let {
                        startActivity(
                            Intent.createChooser(
                                Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_STREAM, it)
                                    type = "image/jpeg"
                                },
                                resources.getText(R.string.share_with)
                            )
                        )
                    } ?: run {
                        Toast.makeText(
                            context,
                            getString(R.string.no_styled_img),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                R.id.save -> {
                    debugBGLM("save")
                    resultImageViewModel.image.value?.let {
                        viewLifecycleOwner.lifecycleScope.launch {
                            if (saveUriToGallery(it, requireContext())) {
                                Toast.makeText(
                                    context,
                                    getString(R.string.saved),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.save_failed),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }
                    } ?: run {
                        Toast.makeText(
                            context,
                            getString(R.string.no_styled_img),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        v?.let { touchedView ->
            event?.let { event ->
                when (touchedView.id) {
                    R.id.result -> {
                        debugBGLM("result")
                    }
                    R.id.toggle -> {
                        when (event.action) {
                            // press to show original
                            MotionEvent.ACTION_DOWN -> {
                                inputImageViewModel.repostIfNotNull()
                            }
                            // lift to show styled
                            MotionEvent.ACTION_UP -> {
                                resultImageViewModel.repostIfNotNull()
                            }
                        }
                    }
                }
            }
        }
        return false
    }
}