package com.examples.miniproject.LoginAndSignup

import android.content.Context
import com.examples.miniproject.Database.DataBaseHelper
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.examples.miniproject.HelperActivities.models.Transaction
import com.examples.miniproject.R
import com.examples.miniproject.databinding.ActivityRegistrationScreenBinding

class RegistrationScreen : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationScreenBinding
    private lateinit var dataBaseHelper: DataBaseHelper
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_registration_screen)

        val toolbar = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        dataBaseHelper = DataBaseHelper(this)
        sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE)


        val register = findViewById<Button>(R.id.registerbutton)

        val enterPass = findViewById<EditText>(R.id.enterPassword)
        val confirmPass = findViewById<EditText>(R.id.editPass)

        binding.registerbutton.setOnClickListener {
            val registerUsername = binding.username.text.toString() // Use EditText for username
            val registerPassword = binding.enterPassword.text.toString()
            val confirmPassword = binding.editPass.text.toString()

            if (registerUsername.isEmpty() || registerPassword.isEmpty() || confirmPassword.isEmpty()) {
                showAlert("Incomplete Details", "Please fill in all the fields.")
            } else if (registerPassword.length < 8) {
                showPasswordLengthAlert()
            } else if (registerPassword != confirmPassword) {
                showPasswordMismatchAlert()
            } else {
                // Proceed with registration
                signupDatabase(registerUsername, registerPassword)
            }
        }

        binding.logintext.setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
        }
    }

    private fun showPasswordLengthAlert() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Password Too Short")
        alertDialogBuilder.setMessage("Password should contain at least 8 characters.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showPasswordMismatchAlert() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Password Mismatch")
        alertDialogBuilder.setMessage("Entered passwords do not match.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showAlert(title: String, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun signupDatabase(username:String,password : String){
        val insertedRowId = dataBaseHelper.insertUSer(username, password)
        if(insertedRowId != -1L){
            saveCredentials(username, password)
            Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }
        else {
            Toast.makeText(this,"Registration Failed",Toast.LENGTH_SHORT).show()
        }
    }
    private fun saveCredentials(username: String, password: String) {
        // Save user credentials in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()

        // Save user credentials in internal storage (optional)
        applicationContext.openFileOutput("credentials.txt", Context.MODE_PRIVATE).use {
            it.write("$username,$password".toByteArray())
        }
    }
}
