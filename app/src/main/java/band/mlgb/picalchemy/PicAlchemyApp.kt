package band.mlgb.picalchemy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PicAlchemyApp : Application()
//{
//    lateinit var appComponent: PicAlchemyComponent
//
//    override fun onCreate() {
//        super.onCreate()
//        appComponent = DaggerPicAlchemyComponent.builder()
//            .appModule(AppModule(applicationContext))
//            .tensorflowModule(TensorflowModule(applicationContext))
//            .build()
//    }
//
//}