package com.examples.miniproject.HelperActivities.helperFragamentsActivities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.examples.miniproject.Database.NoteDatabaseHelper
import com.examples.miniproject.HelperActivities.Note
import com.examples.miniproject.R
import com.examples.miniproject.databinding.ActivityAddNotesBinding

class AddNotes : AppCompatActivity() {

    private lateinit var binding : ActivityAddNotesBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbarNotes)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        db = NoteDatabaseHelper(this)

        binding.savebutton.setOnClickListener{
            val title= binding.titletext.text.toString()
            val content = binding.contentNotes.text.toString()
            val note = Note(0,title,content)
            db.insertNote(note)
            finish()
            Toast.makeText(this,"Note Saved",Toast.LENGTH_SHORT).show()
        }

    }
}