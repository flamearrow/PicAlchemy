package band.mlgb.picalchemy.viewModels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import band.mlgb.picalchemy.inject.ActivityScope
import javax.inject.Inject

// this viewmodel needs to be bound with activity
// representing image picked (from gallery or camera)
@ActivityScope
class ImageViewModel @Inject constructor() : ViewModel() {
    val image: MutableLiveData<Uri> = MutableLiveData()

    // Repost the value to notify the observers
    fun repostIfNotNull() {
        image.value?.let {
            image.postValue(it)
        }
    }
}