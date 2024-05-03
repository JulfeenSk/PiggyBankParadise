package com.examples.miniproject.LoginAndSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.examples.miniproject.R

class PreLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_login)

        val signup = findViewById<Button>(R.id.signup)
        val signin = findViewById<Button>(R.id.signin)
        val guest = findViewById<TextView>(R.id.guest)

        signup.setOnClickListener {
            val intent_signup = Intent(this, RegistrationScreen::class.java)
            startActivity(intent_signup)
        }

        signin.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
        }

        guest.setOnClickListener {
            val explore_guest = Intent(this, LogoScreen::class.java)
            startActivity(explore_guest)
        }

        }

}