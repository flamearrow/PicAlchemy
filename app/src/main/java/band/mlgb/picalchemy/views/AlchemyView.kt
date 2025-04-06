package band.mlgb.picalchemy.views

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import band.mlgb.picalchemy.R
import band.mlgb.picalchemy.viewModels.AlchemyComposeViewModel
import band.mlgb.picalchemy.views.theme.PicAlchemyTheme
import coil.compose.AsyncImage


sealed interface AlchemyState {
    val styles: List<Uri>

    data class Idle(val srcUri: Uri, override val styles: List<Uri>) : AlchemyState
    data class Loading(override val styles: List<Uri>) : AlchemyState
    data class Success(val srcUri: Uri, val resultUri: Uri, override val styles: List<Uri>) :
        AlchemyState
}

@Composable
fun AlchemyView() {
    val vm: AlchemyComposeViewModel = hiltViewModel()
    val state by vm.alchemyViewStateFlow.collectAsState()

    AlchemyContent(state, onStyleSelected = { styleUri ->
        vm.selectStyle(styleUri)
    }, onSave = {
        vm.save()
    }, onShare = {
        vm.share()
    })
}

@Composable
fun AlchemyContent(
    state: AlchemyState, onStyleSelected: (Uri) -> Unit, onSave: () -> Unit, onShare: () -> Unit
) {
    val resultSwitchButtonState = rememberResultSwitchButtonState()

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        AlchemyHeader(state, resultSwitchButtonState.shouldShowResult)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            RoundButton(
                icon = R.drawable.ic_save_alt_white_24dp,
                contentDescription = "Save",
                onClick = onSave,
                enabled = state is AlchemyState.Success
            )

            ResultSwitchButton(
                enabled = state is AlchemyState.Success,
                state = resultSwitchButtonState
            )

            RoundButton(
                icon = R.drawable.ic_share_white_24dp,
                contentDescription = "Share",
                onClick = onShare,
                enabled = state is AlchemyState.Success
            )
        }
        StyleRow(state.styles, onStyleSelected = onStyleSelected)
    }
}

@Composable
private fun StyleRow(styles: List<Uri>, onStyleSelected: (Uri) -> Unit) {
    val styleImageSize = LocalConfiguration.current.screenWidthDp / 4.8
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier
            .height((styleImageSize * 2 + 4).dp)
            .padding(horizontal = 4.dp)
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(styles) {
            if (LocalInspectionMode.current) {
                Image(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .size(styleImageSize.dp)
                        .clip(RoundedCornerShape((styleImageSize / 8).dp)),
                    painter = painterResource(R.drawable.tora),
                    contentDescription = "style image for ${it.path}"
                )
            } else {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .size(styleImageSize.dp)
                        .clip(RoundedCornerShape((styleImageSize / 8).dp))
                        .clickable {
                            onStyleSelected(it)
                        },
                    model = it,
                    contentDescription = "style image for ${it.path}"
                )
            }
        }
    }
}

/**
 * Default FilledIconButton doesn't have elevation
 */
@Composable
fun RoundButton(
    @DrawableRes icon: Int,
    contentDescription: String,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Surface(
        modifier = Modifier.size(68.dp),
        shape = CircleShape,
        enabled = enabled,
        onClick = {
            if (enabled) {
                onClick()
            }
        },
        shadowElevation = if (enabled) 8.dp else 0.dp, // No elevation if disabled
        color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.12f
        )
    ) {
        Icon(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.38f
            )
        )
    }
}

private data class ResultSwitchButtonState(
    val shouldShowResult: Boolean,
    val interactionSource: MutableInteractionSource
)

@Composable
private fun rememberResultSwitchButtonState(): ResultSwitchButtonState {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    return remember(isPressed) {
        ResultSwitchButtonState(!isPressed, interactionSource)
    }
}

@Composable
private fun ResultSwitchButton(
    enabled: Boolean,
    state: ResultSwitchButtonState = rememberResultSwitchButtonState(),
) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape,
        enabled = enabled,
        onClick = {},
        shadowElevation = if (enabled) 8.dp else 0.dp,
        color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(
            alpha = 0.12f
        ),
        interactionSource = state.interactionSource
    ) {
        Icon(
            modifier = Modifier.padding(10.dp),
            painter = painterResource(R.drawable.ic_star_half_white_24dp),
            contentDescription = "toggle source and result",
            tint = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.38f
            )
        )
    }
}

@Composable
fun AlchemyHeader(state: AlchemyState, shouldShowResult: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(10.dp),
        contentAlignment = Alignment.Center

    ) {
        when (state) {
            is AlchemyState.Idle -> {
                if (LocalInspectionMode.current) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(R.drawable.tora),
                        contentDescription = "Source image"
                    )
                } else {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        model = state.srcUri,
                        contentDescription = "Source Image"
                    )
                }
            }

            is AlchemyState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                )
            }

            is AlchemyState.Success -> {
                if (LocalInspectionMode.current) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(R.drawable.tora),
                        contentDescription = "Source image"
                    )
                } else {
                    if (shouldShowResult) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            model = state.resultUri,
                            contentDescription = "result Image"
                        )
                    } else {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop,
                            model = state.srcUri,
                            contentDescription = "Source Image"
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun AlchemyPreviewIdle() {
    PicAlchemyTheme {
        Surface {
            AlchemyContent(
                AlchemyState.Idle(
                    Uri.EMPTY,
                    mutableListOf<Uri>().also { list ->
                        repeat(16) {
                            list.add(Uri.EMPTY)
                        }
                    }
                ), {}, {}, {})
        }
    }
}

@Preview
@Composable
fun AlchemyPreviewLoading() {
    PicAlchemyTheme {
        Surface {
            AlchemyContent(
                AlchemyState.Loading(
                    mutableListOf<Uri>().also { list ->
                        repeat(16) {
                            list.add(Uri.EMPTY)
                        }
                    }
                ), {}, {}, {})
        }
    }
}


@Preview
@Composable
fun AlchemyPreviewResult() {
    PicAlchemyTheme {
        Surface {
            AlchemyContent(
                AlchemyState.Success(
                    srcUri = "android.resource://band.mlgb.picalchemy/${R.drawable.ic_alchemy}".toUri(),
                    resultUri = "android.resource://band.mlgb.picalchemy/${R.drawable.tora}".toUri(),
                    styles = mutableListOf<Uri>().also { list ->
                        repeat(16) {
                            list.add(Uri.EMPTY)
                        }
                    }
                ), {}, {}, {})
        }
    }
}
