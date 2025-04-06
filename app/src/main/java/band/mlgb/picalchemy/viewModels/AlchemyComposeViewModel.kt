package band.mlgb.picalchemy.viewModels

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.mlgb.picalchemy.R
import band.mlgb.picalchemy.tensorflow.ImageSharer
import band.mlgb.picalchemy.tensorflow.StyleTransferer
import band.mlgb.picalchemy.utils.ImageSaver
import band.mlgb.picalchemy.views.AlchemyState
import band.mlgb.picalchemy.views.SRC_URI
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class AlchemyComposeViewModel @Inject constructor(
    private val styleTransferer: StyleTransferer,
    private val galleryImageSaver: ImageSaver,
    private val imageSharer: ImageSharer,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private companion object {
        val STYLES = (0..25).map {
            "file:///android_asset/thumbnails/style$it.jpg".toUri()
        }
    }

    private val srcUri: Uri = URLDecoder.decode(
        savedStateHandle[SRC_URI],
        StandardCharsets.UTF_8.name()
    ).toUri()


    val alchemyViewStateFlow: MutableStateFlow<AlchemyState> =
        MutableStateFlow(AlchemyState.Idle(srcUri, STYLES))

    fun selectStyle(styleUri: Uri) {
        alchemyViewStateFlow.update {
            AlchemyState.Loading(it.styles)
        }

        viewModelScope.launch {
            runCatching {
                styleTransferer.transferStyle(styleUri, srcUri)
            }.onSuccess { resultUri ->
                alchemyViewStateFlow.update {
                    AlchemyState.Success(srcUri, resultUri, it.styles)
                }
            }.onFailure {
                alchemyViewStateFlow.update {
                    AlchemyState.Idle(srcUri, it.styles)
                }
            }
        }
    }

    fun save(resultUri: Uri) {
        viewModelScope.launch {
            galleryImageSaver.save(resultUri)
            Toast.makeText(
                context,
                context.getString(R.string.saved),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun share(resultUri: Uri) {
        viewModelScope.launch {
            imageSharer.share(resultUri)
        }
    }
}