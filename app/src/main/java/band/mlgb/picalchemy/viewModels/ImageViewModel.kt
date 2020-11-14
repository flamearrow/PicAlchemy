package band.mlgb.picalchemy.viewModels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

// this viewmodel needs to be bound with activity
// representing image picked (from gallery or camera)
class ImageViewModel(app: Application) : AndroidViewModel(app) {
    val image: MutableLiveData<Uri> = MutableLiveData()

    // Repost the value to notify the observers
    fun repostIfNotNull() {
        image.value?.let {
            image.postValue(it)
        }
    }
}