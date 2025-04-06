package band.mlgb.picalchemy.inject

import band.mlgb.picalchemy.tensorflow.ImageSharer
import band.mlgb.picalchemy.tensorflow.IntentImageSharer
import band.mlgb.picalchemy.utils.GalleryImageSaver
import band.mlgb.picalchemy.utils.ImageSaver
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AlchemyUtilModule {

    @Binds
    fun bindsImageSaver(galleryImageSaver: GalleryImageSaver): ImageSaver

    @Binds
    fun bindsImageSharer(intentImageSharer: IntentImageSharer): ImageSharer

}