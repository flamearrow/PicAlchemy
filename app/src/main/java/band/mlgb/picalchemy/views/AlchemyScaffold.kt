package band.mlgb.picalchemy.views

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun AlchemyScaffold() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {

        }
    ) { padding ->
        AlchemyHost(Modifier.padding(padding))
    }
}

@Composable
fun AlchemyHost(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = "gallery/false"
    ) {
        composable(
            GALLERY, arguments = listOf(
                navArgument(IS_PICKING_INPUT) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )) {
            GalleryView(navController)
        }

        composable(
            ALCHEMY, arguments = listOf(
                navArgument(SRC_URI) {
                    type = NavType.StringType
                    nullable = false
                }
            )) {
            AlchemyView()
        }

    }
}

const val IS_PICKING_INPUT = "isPickingInput"
const val SRC_URI = "src_uri"
const val GALLERY = "gallery/{$IS_PICKING_INPUT}"
const val ALCHEMY = "alchemy/{$SRC_URI}"


fun NavController.navigateToGallery(isPickingInput: Boolean) {
    navigate("gallery/$isPickingInput")
}

fun NavController.navigateToAlchemy(sourceImageUri: Uri) {
    val encodedUri = URLEncoder.encode(sourceImageUri.toString(), StandardCharsets.UTF_8.name())
    navigate("alchemy/$encodedUri")
}