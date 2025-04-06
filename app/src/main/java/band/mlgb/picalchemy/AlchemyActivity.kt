package band.mlgb.picalchemy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import band.mlgb.picalchemy.utils.ActivityPhotoTaker
import band.mlgb.picalchemy.utils.LocalPhotoTaker
import band.mlgb.picalchemy.utils.PhotoTaker
import band.mlgb.picalchemy.views.AlchemyScaffold
import band.mlgb.picalchemy.views.theme.PicAlchemyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlchemyActivity : ComponentActivity() {
    // The ActivityScoped subcomponent is to be created inside an activity and accessed by Fragment
    // no longer needed after hilt
    // lateinit var alchemoySubComponent: AlchemySubComponent

    lateinit var photoTaker: PhotoTaker

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, proceed with your logic
            setContent {
                PicAlchemyTheme {
                    CompositionLocalProvider(
                        LocalPhotoTaker provides photoTaker
                    ) {
                        AlchemyScaffold()
                    }
                }
            }
        } else {
            // Permission denied, show a message to the user
            Toast.makeText(
                this,
                getString(R.string.continue_without_external_read),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register before onResume
        photoTaker = ActivityPhotoTaker(this)

        // no longer needed after hilt
        // alchemoySubComponent =
        //    (application as PicAlchemyApp).appComponent.alchemyComponentFactory().create()
        checkAndRequestForPermission()
    }

    private fun mediaPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    private fun checkAndRequestForPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                mediaPermission()
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted, proceed with your logic
                setContent {
                    PicAlchemyTheme {
                        CompositionLocalProvider(
                            LocalPhotoTaker provides photoTaker
                        ) {
                            AlchemyScaffold()
                        }
                    }
                }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                mediaPermission()
            ) -> {
                // Show rationale to the user
                showPermissionRationale()
            }

            else -> {
                // Request the permission
                requestPermissionLauncher.launch(mediaPermission())
            }
        }
    }

    private fun showPermissionRationale() {
        androidx.appcompat.app.AlertDialog.Builder(this).let { alertDialogBuilder ->
            alertDialogBuilder.setMessage(getString(R.string.permission_rationale))
            alertDialogBuilder.setPositiveButton(
                getString(R.string.okay)
            ) { _, _ ->
                requestPermissionLauncher.launch(mediaPermission())
            }
            alertDialogBuilder.setOnCancelListener {
                requestPermissionLauncher.launch(mediaPermission())
            }

            alertDialogBuilder.create().also {
                it.show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1 shl 1
    }
}