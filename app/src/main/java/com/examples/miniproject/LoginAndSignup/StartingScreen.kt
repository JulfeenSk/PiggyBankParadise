package com.examples.miniproject.LoginAndSignup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.examples.miniproject.R

class StartingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_screen)

        var logomain  = findViewById<ImageView>(R.id.logomain)



        // Apply fade-in animation to ImageView
        applyFadeInAnimation(logomain,1500)

    }

    private fun applyFadeInAnimation(view: android.view.View, duration: Long) {
        val fadeInAnimation = AlphaAnimation(0.0f, 1.0f)
        fadeInAnimation.duration = duration // Set custom duration in milliseconds
        view.startAnimation(fadeInAnimation)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                val i = Intent(this, PreLogin::class.java)
                startActivity(i)
                finish() }, 1500)
    }

}