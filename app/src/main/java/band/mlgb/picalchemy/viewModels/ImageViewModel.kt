package band.mlgb.picalchemy.viewModels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A viewmodel needs to be bound with activity , representing image picked (from gallery or camera)
 * or image result of tflite model.
 * This class needs to be explicitly injected as there will be two different instance with different
 * scopes.
 */
class ImageViewModel : ViewModel() {
    val image: MutableLiveData<Uri> = MutableLiveData()

    // Repost the value to notify the observers
    fun repostIfNotNull() {
        image.value?.let {
            image.postValue(it)
        }
    }
}