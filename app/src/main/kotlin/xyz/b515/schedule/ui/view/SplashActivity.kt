package xyz.b515.schedule.ui.view

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.startActivity
import xyz.b515.schedule.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 500L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity<MainActivity>()
            finish()
        }, SPLASH_DISPLAY_LENGTH)
    }
}
