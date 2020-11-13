package band.mlgb.picalchemy.viewModels

import android.content.res.AssetManager
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StyleListViewModel(assets: AssetManager) : ViewModel() {

    val styleList: LiveData<List<Uri>> by lazy {
        MutableLiveData<List<Uri>>().apply {
            assets.list("thumbnails")?.let {
                var list = ArrayList<Uri>()
                for (uriPath: String in it) {
                    list.add(Uri.parse("file:///android_asset/thumbnails/$uriPath"))
                }
                postValue(list)
            }
        }
    }

    val selectedStyle: LiveData<Uri> by lazy {
        MutableLiveData()
    }

    companion object {
        fun providerFactory(
            assets: AssetManager
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return StyleListViewModel(assets) as T
            }

        }

    }

}
