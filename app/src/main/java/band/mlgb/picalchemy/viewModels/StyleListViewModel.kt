package band.mlgb.picalchemy.viewModels

import android.content.res.AssetManager
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import band.mlgb.picalchemy.utils.errBGLM
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class StyleListViewModel @Inject constructor(assets: AssetManager) : ViewModel() {
    private var currentStyleList =
        assets.list("thumbnails")?.let { styleFiles ->
            ArrayList<Uri>().also {
                // add cartoonizer
                it.add(Uri.parse("file:///android_asset/thumbnails/$CARTOONIZER"))
                // add preloaded styles
                it.addAll(styleFiles.filter { s -> s.startsWith(STYLE_PREFIX) }
                    .map { styleName ->
                        Uri.parse("file:///android_asset/thumbnails/$styleName")
                    })
                // add 'add'
                it.add(Uri.parse("file:///android_asset/thumbnails/$ADD"))
            }
        }


    val styleList: MutableLiveData<List<Uri>> by lazy {
        MutableLiveData<List<Uri>>().apply {
            postValue(currentStyleList)
        }
    }

    // add a new style selected and update the value
    fun prependStyleAndRepost(newStyle: Uri) {
        currentStyleList?.let {
            it.add(0, newStyle)
            styleList.postValue(currentStyleList)
        } ?: run {
            errBGLM("null currentStyleList")
        }
    }

    companion object {
        private const val STYLE_PREFIX = "style"
        private const val CARTOONIZER = "holo.jpg"
        private const val ADD = "add.jpg"

    }

}
