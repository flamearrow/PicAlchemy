package band.mlgb.picalchemy.views

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import band.mlgb.picalchemy.R
import band.mlgb.picalchemy.viewModels.GalleryComposeViewModel
import band.mlgb.picalchemy.views.theme.PicAlchemyTheme
import coil.compose.AsyncImage

sealed interface GalleryState {
    object Loading : GalleryState
    data class Loaded(val images: List<Uri>) : GalleryState
}

@Composable
fun GalleryView(navController: NavController) {
    val vm: GalleryComposeViewModel = hiltViewModel()

    val state by vm.galleryState.collectAsState()
    GalleryContent(
        state,
        onImageSelected = {
            navController.navigateToAlchemy(it)
        }, onCameraButtonPressed = {
            vm.takePicture()
        }
    )
}

@Composable
fun GalleryContent(
    state: GalleryState,
    onImageSelected: (Uri) -> Unit,
    onCameraButtonPressed: () -> Unit
) {
    when (state) {
        is GalleryState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is GalleryState.Loaded -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.images) { uri ->
                        if (LocalInspectionMode.current) {
                            Image(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(6.dp)),
                                painter = painterResource(R.drawable.tora),
                                contentDescription = "iamge",
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onImageSelected(uri) },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                FloatingActionButton(
                    onClick = onCameraButtonPressed,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(48.dp)
                        .size(80.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize().padding(18.dp),
                        painter = painterResource(R.drawable.ic_photo_camera_white_24dp),
                        contentDescription = "Camera button"
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun GalleryViewPreviewLoaded() {
    PicAlchemyTheme {
        Surface {
            GalleryContent(
                GalleryState.Loaded(
                    listOf(
                        Uri.EMPTY,
                        Uri.EMPTY,
                        Uri.EMPTY,
                        Uri.EMPTY,
                        Uri.EMPTY,
                    )
                ),
                {}
            ) {}
        }
    }
}
