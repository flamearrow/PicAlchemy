package band.mlgb.picalchemy.inject

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Provides activity scoped viewmodels to [AlchemyAcitivity] and its two
 * fragments [AlchemyFragment] and [GalleryFragment].
 *
 * This is an overkill as we hiltified the app and installed in [ActivityRetainedComponent],
 * it shouldn't be called subcomponent.
 */

@Module
@InstallIn(ActivityRetainedComponent::class)
class AlchemySubcomponentModule {
    @Provides
    fun provideToyComplicatedClassInstance(@ApplicationContext context: Context): ToyComplicatedClass {
        return ToyComplicatedClass(context)
    }
}