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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import band.mlgb.picalchemy.adapters.GalleryAdapter
import band.mlgb.picalchemy.databinding.FragmentGalleryBinding
import band.mlgb.picalchemy.utils.createTemporaryUri
import band.mlgb.picalchemy.viewModels.GalleryViewModel
import band.mlgb.picalchemy.viewModels.ImageViewModel
import band.mlgb.picalchemy.views.UriPickedListener

class GalleryFragment : Fragment(), UriPickedListener {
    companion object {
        private const val TAKE_PIC = 1 shl 0
    }

    private val galleryViewModel: GalleryViewModel by viewModels {
        GalleryViewModel.providerFactory(requireActivity().contentResolver)
    }

    private val inputImageViewModel: ImageViewModel by activityViewModels()

    private var uriByCamera: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                        createTemporaryUri(activity).also {
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

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onUriPicked(photo: Uri) {
        inputImageViewModel.image.postValue(photo)
        findNavController().navigate(R.id.action_gallery_to_alchemy)

    }
}
