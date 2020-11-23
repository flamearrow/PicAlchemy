package band.mlgb.picalchemy

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import band.mlgb.picalchemy.adapters.GalleryAdapter
import band.mlgb.picalchemy.databinding.FragmentGalleryBinding
import band.mlgb.picalchemy.inject.ToyComplicatedClass
import band.mlgb.picalchemy.utils.createInternalFileUri
import band.mlgb.picalchemy.viewModels.GalleryViewModel
import band.mlgb.picalchemy.viewModels.ImageViewModel
import band.mlgb.picalchemy.viewModels.StyleListViewModel
import band.mlgb.picalchemy.views.UriPickedListener
import javax.inject.Inject

class GalleryFragment : Fragment(), UriPickedListener {
    // safe arg plugin magic, equivalent to getArgments().getBoolean("paramName")
    private val args: GalleryFragmentArgs by navArgs()

    // live with the Fragment
    private val galleryViewModel: GalleryViewModel by viewModels {
        GalleryViewModel.providerFactory(requireActivity().contentResolver)
    }

    // When injected, the ImageViewModel has @AciivityScope, which is bound with AlchemyActivity
    // This is essentially same with
    // private val inputImageViewModel: ImageViewModel by activityViewModels()
    // will need to let the ViewModel extend AndroidViewModel and pass application instead
    @Inject
    lateinit var inputImageViewModel: ImageViewModel

    // Similar to inputImageViewModel, this instance is also @ActivityScope, but it's injected
    // inside the subcomponent module AlchemySubcomponentModule, instead of declared in constructor.
    @Inject
    lateinit var toy: ToyComplicatedClass

    // @ActivityScope, add styles to it when GalleryFragment is used for picking a new style
    @Inject
    lateinit var styleListViewModel: StyleListViewModel;

    private var uriByCamera: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AlchemyActivity).alchemoySubComponent.inject(this)

        val binding = FragmentGalleryBinding.inflate(layoutInflater)

        with(GalleryAdapter(this)) {
            binding.gallery.adapter = this
            binding.setOnClickListener {
                takePicture()
            }
            galleryViewModel.imageUris.observe(viewLifecycleOwner) {
                submitList(it)
            }
        }

        return binding.root

    }

    /**
     * start an intent to take image and save it to [uriByCamera]
     */
    private fun takePicture() {
        activity?.let { activity ->
            activity.packageManager?.let { packageManager ->
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        createInternalFileUri(activity).also {
                            uriByCamera = it.contentUri
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriByCamera)
                            startActivityForResult(
                                takePictureIntent, TAKE_PIC
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PIC && resultCode == RESULT_OK) {
            uriByCamera?.also {
                onUriPicked(it)
            }
        }
    }

    override fun onUriPicked(uri: Uri) {
        if (args.isPickingInput) {
            inputImageViewModel.image.postValue(uri)
            findNavController().navigate(GalleryFragmentDirections.pickInputGalleryToAlchemy())
        } else {
            styleListViewModel.prependStyleAndRepost(uri)
//            findNavController().popBackStack() // current GalleryFragment
//            findNavController().popBackStack() // previous AlchemyFragment
            findNavController().navigate(GalleryFragmentDirections.pickStyleGalleryToAlchemy())
        }
    }

    companion object {
        private const val TAKE_PIC = 1 shl 0
    }
}
