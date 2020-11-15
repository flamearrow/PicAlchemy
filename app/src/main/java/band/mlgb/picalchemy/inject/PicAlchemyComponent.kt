package band.mlgb.picalchemy.inject

import band.mlgb.picalchemy.AlchemyFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TensorflowModule::class])
interface PicAlchemyComponent {
    fun inject(fragment: AlchemyFragment)
}