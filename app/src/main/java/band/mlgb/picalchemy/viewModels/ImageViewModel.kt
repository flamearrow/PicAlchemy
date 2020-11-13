package band.mlgb.picalchemy.viewModels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// this viewmodel needs to be bound with activity
// representing image picked (from gallery or camera)
class ImageViewModel(app: Application) : AndroidViewModel(app) {
    val image: MutableLiveData<Uri> = MutableLiveData()

    companion object {
        fun providerFactory(
            app: Application
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return GalleryViewModel(app.contentResolver) as T

                }
            }
    }
}