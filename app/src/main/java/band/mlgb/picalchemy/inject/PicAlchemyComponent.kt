package band.mlgb.picalchemy.inject

/**
 * Application component, managers tensorflow singletons and activity scoped viewModels
 * The tenerated builder only has TensorflowModule setters,
 * doesn't have AlchemuSubComponentMoudle setters as its a subcomponent module and should be created later
 */
//@Singleton
//@Component(modules = [AppModule::class, TensorflowModule::class])
@Deprecated(message = "replaced with hilt")
interface PicAlchemyComponent {
    // Note this component doesn't need to inject(), it just needs to be hold in Application
    // and used to expose the actual subcomponent to call inject() when needed
//    fun alchemyComponentFactory(): AlchemySubComponent.Factory
}