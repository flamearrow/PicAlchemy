package band.mlgb.picalchemy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import band.mlgb.picalchemy.databinding.ActivityEntryBinding
import band.mlgb.picalchemy.views.AlchemyScaffold
import band.mlgb.picalchemy.views.theme.PicAlchemyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlchemyActivity : AppCompatActivity() {
    // The ActivityScoped subcomponent is to be created inside an activity and accessed by Fragment
    // no longer needed after hilt
    // lateinit var alchemoySubComponent: AlchemySubComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    private fun requestRequiredPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(mediaPermission()),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                mediaPermission()
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // This returns true if user ever chosen 'deny and never ask again'
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    mediaPermission()
                )
            ) {
                showPermissionRationale()
            } else {
                requestRequiredPermissions()
            }
        } else {
            // Different ways to data binding
            // setContentView(ActivityEntryBinding.inflate(layoutInflater).root)
//            DataBindingUtil.setContentView<ActivityEntryBinding>(this, R.layout.activity_entry)
            setContent {
                PicAlchemyTheme {
                    AlchemyScaffold()
                }
            }
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this).let { alertDialogBuilder ->
            alertDialogBuilder.setMessage(getString(R.string.permission_rationale))
            alertDialogBuilder.setPositiveButton(
                getString(R.string.okay)
            ) { _, _ ->
                requestRequiredPermissions()
            }
            alertDialogBuilder.setOnCancelListener {
                requestRequiredPermissions()
            }

            alertDialogBuilder.create().also {
                it.show()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isEmpty() ||
                grantResults[0] != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    this,
                    getString(R.string.continue_without_external_read),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        // Different ways to data binding
        // setContentView(ActivityEntryBinding.inflate(layoutInflater).root)
        DataBindingUtil.setContentView<ActivityEntryBinding>(this, R.layout.activity_entry)

    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1 shl 1
    }
}