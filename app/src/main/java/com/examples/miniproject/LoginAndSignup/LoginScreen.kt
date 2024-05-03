package com.examples.miniproject.LoginAndSignup

import android.content.Context
import com.examples.miniproject.Database.DataBaseHelper
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.examples.miniproject.R
import com.examples.miniproject.databinding.ActivityLoginScreenBinding

class LoginScreen : AppCompatActivity() {

    private lateinit var binding : ActivityLoginScreenBinding
    private lateinit var databaseHelper: DataBaseHelper

    private var loginAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        val forgotPass = findViewById<TextView>(R.id.forgotpass)
        val signUp = findViewById<TextView>(R.id.signup)


        databaseHelper = DataBaseHelper(this)

        forgotPass.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
        signUp.setOnClickListener {
            startActivity(Intent(this, RegistrationScreen::class.java))
        }

        binding.loginbutton.setOnClickListener {
            val loginUsername = binding.editName.text.toString()
            val loginPassword = binding.editPass.text.toString()
            loginDatabase(loginUsername,loginPassword)
            

        }


    }

    private fun showAlertDialog(title: String, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun loginDatabase(username: String, password: String) {
        // Check if both username and password are entered
        if (username.isEmpty() || password.isEmpty()) {
            showAlertDialog("Incomplete", "Please enter both username and password.")
            return
        }

        // Increment login attempts
        loginAttempts++

        // Check if login attempts exceed 3
        if (loginAttempts > 3) {
            showAlertDialog("Forgot Password", "You have failed to enter the password for more than 3 times. Click on Forgot Password.")
            return
        }

        // Check if the username exists in the database
        val userExistsInDatabase = databaseHelper.readUser(username, password)

        // Check if the username and password exist in SharedPreferences
        val sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE)
        val storedUsername = sharedPreferences.getString("username", "")
        val storedPassword = sharedPreferences.getString("password", "")

        if (username == storedUsername && password == storedPassword) {
            // Username and password match the stored credentials in SharedPreferences
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LogoScreen::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Check if the username exists in the database
        if (userExistsInDatabase) {
            // Username exists in the database, but password may be incorrect
            if (password.length < 8) {
                showAlertDialog("Password Too Short", "Password should contain at least 8 characters.")
            } else {
                showAlertDialog("Login Failed", "Incorrect password.")
            }
        } else {
            // Username not found in the database
            showAlertDialog("User not Found", "The Username is not registered.")
        }
    }


}
