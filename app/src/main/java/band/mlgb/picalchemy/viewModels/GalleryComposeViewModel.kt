package band.mlgb.picalchemy.viewModels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import band.mlgb.picalchemy.repo.GalleryImageRepo
import band.mlgb.picalchemy.views.GalleryState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryComposeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val galleryImageRepo: GalleryImageRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val galleryState: MutableStateFlow<GalleryState> = MutableStateFlow(GalleryState.Loading)

    init {
        reset()
    }

    fun onError(error: Throwable) {
        galleryState.update {
            GalleryState.Error(error)
        }
    }

    fun reset() {
        galleryState.update {
            GalleryState.Loading
        }
        viewModelScope.launch {
            galleryImageRepo.galleryImages.collectLatest { uris ->
                galleryState.update {
                    GalleryState.Loaded(uris)
                }
            }
        }
    }

    fun onLoading() {
        galleryState.update {
            GalleryState.Loading
        }
    }
}