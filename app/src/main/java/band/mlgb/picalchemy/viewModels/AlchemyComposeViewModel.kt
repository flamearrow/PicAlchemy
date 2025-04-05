package band.mlgb.picalchemy.viewModels

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import band.mlgb.picalchemy.views.AlchemyState
import band.mlgb.picalchemy.views.SRC_URI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class AlchemyComposeViewModel @Inject constructor(
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


    val alchemyViewStateFlow = MutableStateFlow(AlchemyState.Idle(srcUri, STYLES))

    fun selectStyle(styleUri: Uri) {

    }

    fun save() {

    }

    fun share() {

    }
}