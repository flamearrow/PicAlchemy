package band.mlgb.picalchemy.inject

import band.mlgb.picalchemy.AlchemyActivity
import band.mlgb.picalchemy.AlchemyFragment
import band.mlgb.picalchemy.GalleryFragment
import dagger.Subcomponent
import dagger.hilt.android.scopes.ActivityScoped

/**
 * Managers instances within one instance of [AlchemyActivity], this is an overkill as there's no
 * multiple instances of activities, just demonstrating dagger subcomponent with activities scope.
 *
 * This component will be hold by an activity, then exposed to Fragments to call inject()
 */
@ActivityScoped
@Subcomponent
interface AlchemyComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AlchemyComponent
    }

    //    fun inject(alchemyActivity: AlchemyActivity)
    fun inject(alchemyFragment: AlchemyFragment)
    fun inject(galleryFragment: GalleryFragment)
}