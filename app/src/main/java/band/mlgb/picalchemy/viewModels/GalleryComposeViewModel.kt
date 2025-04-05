package band.mlgb.picalchemy.viewModels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.mlgb.picalchemy.repo.GalleryImageRepo
import band.mlgb.picalchemy.views.GalleryState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GalleryComposeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    galleryImageRepo: GalleryImageRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // if true, this is used for picking input image, otherwise for picking style
    private val isPickingInput = savedStateHandle.get<Boolean>("isPickingInput") ?: false

    @OptIn(ExperimentalCoroutinesApi::class)
    val galleryState = galleryImageRepo.galleryImages.mapLatest {
        GalleryState.Loaded(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GalleryState.Loading
    )

    fun takePicture() {

    }

}