package band.mlgb.picalchemy.inject

import band.mlgb.picalchemy.AlchemyActivity
import band.mlgb.picalchemy.AlchemyFragment
import band.mlgb.picalchemy.GalleryFragment

/**
 * Managers instances within one instance of [AlchemyActivity], this is an overkill as there's no
 * multiple instances of activities, just demonstrating dagger subcomponent with activities scope.
 *
 * This component will be hold by an activity, then exposed to Fragments to call inject()
 */
//@ActivityScope
//@Subcomponent(modules = [AlchemySubcomponentModule::class])
@Deprecated(message = "replaced with hilt")
interface AlchemySubComponent {
//    @Subcomponent.Factory
//    interface Factory {
//        fun create(): AlchemySubComponent
//    }

    //    fun inject(alchemyActivity: AlchemyActivity)
    fun inject(alchemyFragment: AlchemyFragment)
    fun inject(galleryFragment: GalleryFragment)
}