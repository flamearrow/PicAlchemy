package band.mlgb.picalchemy.inject

import dagger.Module
import dagger.hilt.migration.DisableInstallInCheck

/**
 * Provides activity scoped viewmodels to [AlchemyAcitivity] and its two
 * fragments [AlchemyFragment] and [GalleryFragment].
 *
 * This is an overkill as there's no multiple instances of AlchemyActivity,
 * just demonstrating dagger subcomponent with activities scope.
 */

@DisableInstallInCheck
@Module(subcomponents = [AlchemyComponent::class])
interface AlchemySubcomponentModule