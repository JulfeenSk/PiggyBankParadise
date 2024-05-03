package com.examples.miniproject.HelperActivities.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.examples.miniproject.Database.NoteDatabaseHelper
import com.examples.miniproject.HelperActivities.Note
import com.examples.miniproject.R
import com.examples.miniproject.HelperActivities.helperFragamentsActivities.UpdateNotes

class NotesAdapter(private var notes: List<Note>, context: Context) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titletextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val cardView: CardView = itemView.findViewById(R.id.cardViewNotes)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        // Set OnClickListener for the update button
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNotes::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        // Set OnClickListener for the card view
        holder.cardView.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNotes::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        // Set OnClickListener for the delete button
        holder.deleteButton.setOnClickListener {
            showConfirmationDialog(holder.itemView.context) {
                // If user confirms, delete the note
                db.deleteNote(note.id)
                refreshData(db.getAllNotes())
                Toast.makeText(holder.itemView.context, "Note Deleted", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun refreshData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }

    fun filterList(filteredNotes: List<Note>) {
        notes = filteredNotes
        notifyDataSetChanged()
    }

    private fun showConfirmationDialog(context: Context, onDeleteConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Delete Note")
        alertDialogBuilder.setMessage("Are you sure you want to delete this note?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, which ->
            // Call the onDeleteConfirmed callback
            onDeleteConfirmed.invoke()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }
}
