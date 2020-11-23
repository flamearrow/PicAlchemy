package band.mlgb.picalchemy.inject

import android.content.Context
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module
@DisableInstallInCheck
class AppModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideAssetManager(): AssetManager {
        return context.assets
    }
}