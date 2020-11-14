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
            assets.list("thumbnails")?.let { styleFiles ->
                postValue(
                    ArrayList<Uri>().also {
                        for (uriPath: String in styleFiles) {
                            it.add(Uri.parse("file:///android_asset/thumbnails/$uriPath"))
                        }
                    }
                )
            }
        }
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
