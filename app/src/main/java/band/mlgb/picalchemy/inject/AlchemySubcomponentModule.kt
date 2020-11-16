package band.mlgb.picalchemy.inject

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck

/**
 * Provides activity scoped viewmodels to [AlchemyAcitivity] and its two
 * fragments [AlchemyFragment] and [GalleryFragment].
 *
 * This is an overkill as there's no multiple instances of AlchemyActivity,
 * just demonstrating dagger subcomponent with activities scope.
 */

@DisableInstallInCheck
@Module
class AlchemySubcomponentModule {
    @Provides
    @ActivityScope
    fun provideToyComplicatedClassInstance(context: Context): ToyComplicatedClass {
        return ToyComplicatedClass(context)
    }
}