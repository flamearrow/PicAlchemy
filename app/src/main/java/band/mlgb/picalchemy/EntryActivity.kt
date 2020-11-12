package band.mlgb.picalchemy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import band.mlgb.picalchemy.databinding.ActivityEntryBinding

class EntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Different ways to data binding
        // setContentView(ActivityEntryBinding.inflate(layoutInflater).root)
        DataBindingUtil.setContentView<ActivityEntryBinding>(this, R.layout.activity_entry)
    }
}