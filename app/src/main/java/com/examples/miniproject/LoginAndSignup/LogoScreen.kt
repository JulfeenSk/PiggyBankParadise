package com.examples.miniproject.LoginAndSignup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.examples.miniproject.HomeScreen
import com.examples.miniproject.R

class LogoScreen : AppCompatActivity() {

    private lateinit var progressBarHorizontal: ProgressBar
    private lateinit var progressBarCircular: ProgressBar
    private lateinit var percentageTextView: TextView
    private lateinit var captionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo_screen)

        // Initialize views
        val logo = findViewById<ImageView>(R.id.logo)
        val title = findViewById<TextView>(R.id.title)
        val slogan = findViewById<TextView>(R.id.slogan)
        val slogan2 = findViewById<TextView>(R.id.slogan2)
        progressBarHorizontal = findViewById(R.id.progressBarHorizontal)
        progressBarCircular = findViewById(R.id.progressBarCircular)
        percentageTextView = findViewById(R.id.percentageTextView)
        captionTextView = findViewById(R.id.captionTextView)

        // Apply fade-in animation to views
        applyFadeInAnimation(logo, 1500)
        applyFadeInAnimation(title, 2000)
        applyFadeInAnimation(slogan, 2500)
        applyFadeInAnimation(slogan2, 2500)

        // Start progress bars animations after fade-in animations complete
        Handler(Looper.getMainLooper()).postDelayed({
            progressBarHorizontal.visibility = android.view.View.VISIBLE
            progressBarCircular.visibility = android.view.View.VISIBLE
            startProgressBarsAnimations()
        }, 2500)
    }

    private fun applyFadeInAnimation(view: android.view.View, duration: Long) {
        val fadeInAnimation = AlphaAnimation(0.0f, 1.0f)
        fadeInAnimation.duration = duration // Set custom duration in milliseconds
        view.startAnimation(fadeInAnimation)
    }

    private fun startProgressBarsAnimations() {
        // Start horizontal progress bar animation
        progressBarHorizontal.progress = 0
        val progressBarHandler = Handler()
        progressBarHandler.postDelayed({
            var progress = 0
            val progressRunnable = object : Runnable {
                override fun run() {
                    if (progress <= 100) {
                        progressBarHorizontal.progress = progress
                        percentageTextView.text = "$progress%"
                        updateCaption(progress)
                        progress += 2 // Increase progress by 2 (adjust as needed)
                        progressBarHandler.postDelayed(this, 50) // Update every 50 milliseconds
                    } else {
                        progressBarHandler.removeCallbacks(this)
                        val i = Intent(this@LogoScreen, HomeScreen::class.java)
                        startActivity(i)
                        finish()
                    }
                }
            }
            progressBarHandler.post(progressRunnable)
        }, 1000) // Delay the start by 1 second

        // Start circular progress bar animation
        val circularProgressHandler = Handler()
        circularProgressHandler.postDelayed({
            var progress = 0
            val circularProgressRunnable = object : Runnable {
                override fun run() {
                    if (progress <= 100) {
                        progressBarCircular.progress = progress
                        progress += 2 // Increase progress by 2 (adjust as needed)
                        circularProgressHandler.postDelayed(this, 50) // Update every 50 milliseconds
                    }
                }
            }
            circularProgressHandler.post(circularProgressRunnable)
        }, 1000) // Delay the start by 1 second
    }


    private fun updateCaption(progress: Int) {
        when (progress) {
            in 0..25 -> captionTextView.text = "Patience is key."
            in 26..50 -> captionTextView.text = "Good things take time."
            in 51..70 -> captionTextView.text = "Almost there, hold up."
            in 71..90 -> captionTextView.text = "Are you ready to explore."
            in 91..100 -> captionTextView.text = "Boom!! There you go."
        }
    }
}
