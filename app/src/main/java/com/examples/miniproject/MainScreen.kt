package com.examples.miniproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

class MainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        var toolbar = findViewById<Toolbar>(R.id.toolbarmain)
        toolbar.setTitle("Piggy Bank Paradise")
        // toolbar.setLogo(R.drawable.logo_toolbar)
        setSupportActionBar(toolbar) //replace toolbar as an ActionBar
        //toolbar.setNavigationOnClickListener {
         //   Toast.makeText(this, "Back Arrow", Toast.LENGTH_LONG).show()
        //}
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id:Int = item.itemId
        if(id==R.id.action_settings)
        {
            Toast.makeText(applicationContext, "Settings", Toast.LENGTH_LONG).show()
            return true
        }
        else if(id == R.id.action_email)
        {
            Toast.makeText(applicationContext, "Email", Toast.LENGTH_LONG).show()
            return true
        }
        else if (id == R.id.action_profile)
        {
            Toast.makeText(applicationContext, "Profile", Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}