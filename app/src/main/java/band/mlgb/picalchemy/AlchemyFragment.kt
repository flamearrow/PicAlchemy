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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import band.mlgb.picalchemy.adapters.StyleListAdapter
import band.mlgb.picalchemy.databinding.FragmentAlchemyBinding
import band.mlgb.picalchemy.inject.InputImageViewModel
import band.mlgb.picalchemy.inject.ResultImageViewModel
import band.mlgb.picalchemy.inject.ToyComplicatedClass
import band.mlgb.picalchemy.tensorflow.Cartonnizer
import band.mlgb.picalchemy.tensorflow.StyleTransferer
import band.mlgb.picalchemy.utils.debugBGLM
import band.mlgb.picalchemy.utils.saveUriToGallery
import band.mlgb.picalchemy.viewModels.ImageViewModel
import band.mlgb.picalchemy.viewModels.StyleListViewModel
import band.mlgb.picalchemy.views.UriPickedListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlchemyFragment : Fragment(), UriPickedListener, View.OnClickListener, View.OnTouchListener {
    // styleListViewModel gets updated by the selection from GalleryViewModel,
    // need to be @ActivityScope
    @Inject
    lateinit var styleListViewModel: StyleListViewModel;
//            by viewModels {
//        StyleListViewModel.providerFactory(requireActivity().assets)
//    }

    // When injected, the ImageViewModel has @AciivityScope, which is bound with AlchemyActivity
    // This is essentially same with
    // private val inputImageViewModel: ImageViewModel by activityViewModels()
    // will need to let the ViewModel extend AndroidViewModel and pass application instead
    @Inject
    @InputImageViewModel
    lateinit var inputImageViewModel: ImageViewModel


    // this view model only handles the merged result
    // Note injected, use vieModels() to bound with this Fragment
//    private val resultImageViewModel: ImageViewModel by viewModels()
    @Inject
    @ResultImageViewModel
    lateinit var resultImageViewModel: ImageViewModel

    @Inject
    lateinit var styleTransferer: StyleTransferer

    @Inject
    lateinit var cartonnizer: Cartonnizer

    // Similar to inputImageViewModel, this instance is also @ActivityScope, but it's injected
    // inside the subcomponent module AlchemySubcomponentModule, instead of declared in constructor.
    @Inject
    lateinit var toy: ToyComplicatedClass

    lateinit var binding: FragmentAlchemyBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Instead of creating a new component, access the activity scoped subcomponent from activity
        // no longer needed after hilt
        // (activity as AlchemyActivity).alchemoySubComponent.inject(this)

        binding = FragmentAlchemyBinding.inflate(inflater)

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
            binding.progressCircular.visibility = View.INVISIBLE
            binding.resultImg.visibility = View.VISIBLE
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


    // An style uri is picked, it's either a preloaded file uri or a newly added content uri
    override fun onUriPicked(uri: Uri) {
        inputImageViewModel.image.value?.let { inputContentUri ->
            inputImageViewModel.viewModelScope.launch {
                uri.path?.substringAfterLast("/")?.let { styleName ->
                    if (styleName.startsWith("style") || uri.scheme == "content") {
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.resultImg.visibility = View.INVISIBLE
                        inputImageViewModel.image.value?.let { inputContentUri ->
                            // input from camera: content://band.mlgb.picalchemy.fileprovider/my_images/Pictures/JPEG_20201113_163511_5835964646040696703.jpg
                            // input from gallery: content://media/external/images/media/11525
                            inputImageViewModel.viewModelScope.launch {
                                styleTransferer.transferStyle(
                                    uri,
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
                    } else if (styleName.startsWith("holo")) {
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.resultImg.visibility = View.INVISIBLE
                        cartonnizer.cartoonize(inputContentUri)?.let {
                            resultImageViewModel.image.postValue(it)
                        } ?: run {
                            // reset original image
                            inputImageViewModel.repostIfNotNull()
                        }
                    } else if (styleName.startsWith("add")) {
                        findNavController().navigate(AlchemyFragmentDirections.actionPickStyle(false))
                    }
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
                                resultImageViewModel.image.value?.let {
                                    inputImageViewModel.repostIfNotNull()
                                } ?: run {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.no_styled_img),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
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