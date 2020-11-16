package band.mlgb.picalchemy

import android.app.Application
import band.mlgb.picalchemy.inject.DaggerPicAlchemyComponent
import band.mlgb.picalchemy.inject.PicAlchemyComponent
import band.mlgb.picalchemy.inject.TensorflowModule

class PicAlchemyApp : Application() {
    lateinit var appComponent: PicAlchemyComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerPicAlchemyComponent.builder()
            .tensorflowModule(TensorflowModule(applicationContext))
            .build()
    }

}