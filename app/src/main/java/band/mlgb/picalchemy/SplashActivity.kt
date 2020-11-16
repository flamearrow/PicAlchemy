package band.mlgb.picalchemy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(350)
        startActivity(Intent(this, AlchemyActivity::class.java))
        finish()
    }
}