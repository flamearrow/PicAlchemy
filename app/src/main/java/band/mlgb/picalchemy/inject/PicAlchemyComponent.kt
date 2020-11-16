package band.mlgb.picalchemy.inject

import dagger.Component
import javax.inject.Singleton

/**
 * Application component, managers tensorflow singletons and activity scoped viewModels
 * The tenerated builder only has TensorflowModule setters,
 * doesn't have AlchemuSubComponentMoudle setters as its a subcomponent module and should be created later
 */
@Singleton
@Component(modules = [TensorflowModule::class])
interface PicAlchemyComponent {
    // Note this component doesn't need to inject(), it just needs to be hold in Application
    // and used to expose the actual subcomponent to call inject() when needed
    fun alchemyComponentFactory(): AlchemyComponent.Factory
}