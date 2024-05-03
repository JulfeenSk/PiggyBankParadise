package com.examples.miniproject.LoginAndSignup

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.examples.miniproject.R

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val enterPass= findViewById<EditText>(R.id.enterPass2)
        val confirmPass = findViewById<EditText>(R.id.confirmPass2)
        val confirm = findViewById<Button>(R.id.confirm)

        confirm.setOnClickListener {
            val password = enterPass.text.toString().trim()
            val confirmPassword = confirmPass.text.toString().trim()

            // Check if password length is less than 8 characters
            if (password.length < 8) {
                showAlert("Incorrect Password", "Password must be at least 8 characters long.")
                return@setOnClickListener
            }

            // Check if passwords match
            if (password != confirmPassword) {
                showAlert("Password Mismatch", "Passwords do not match.")
                return@setOnClickListener
            }

            // Proceed to LoginScreen activity
            startActivity(Intent(this, LoginScreen::class.java))
             // Optional: finish current activity if no longer needed
        }
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
