package com.examples.miniproject.HelperActivities.helperFragamentsActivities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.examples.miniproject.Database.NoteDatabaseHelper
import com.examples.miniproject.HelperActivities.Note
import com.examples.miniproject.R
import com.examples.miniproject.databinding.ActivityUpdateNotesBinding

class UpdateNotes : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNotesBinding

    private lateinit var db: NoteDatabaseHelper
    private var noteId : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbarUpdateNotes)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        db = NoteDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id",-1)
        if(noteId == -1){
            finish()
            return
        }

        val note = db.getNoteById(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentNotes.setText(note.content)

        binding.updateSavebutton.setOnClickListener{
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentNotes.text.toString()
            val updateNotes = Note(noteId,newTitle,newContent)
            db.updateNote(updateNotes)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }



    }
}