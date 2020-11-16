package band.mlgb.picalchemy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import band.mlgb.picalchemy.databinding.ActivityEntryBinding
import band.mlgb.picalchemy.inject.AlchemyComponent

class AlchemyActivity : AppCompatActivity() {
    // The ActivityScoped subcomponent is to be created inside an activity and accessed by Fragment
    lateinit var alchemoyComponent: AlchemyComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alchemoyComponent =
            (application as PicAlchemyApp).appComponent.alchemyComponentFactory().create()
        // no need to inject here
        // alchemoyComponent.inject(this)

        // Different ways to data binding
        // setContentView(ActivityEntryBinding.inflate(layoutInflater).root)
        DataBindingUtil.setContentView<ActivityEntryBinding>(this, R.layout.activity_entry)
    }

}