package com.examples.miniproject.Fragments


import com.examples.miniproject.HelperActivities.adapters.NotesAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.examples.miniproject.Database.NoteDatabaseHelper
import com.examples.miniproject.HelperActivities.Note
import com.examples.miniproject.databinding.FragmentNotesBinding

class NotesFragment : Fragment() {

    private lateinit var db: NoteDatabaseHelper
    private lateinit var binding: FragmentNotesBinding
    private lateinit var notesAdapter: NotesAdapter
    private var notesList: List<Note> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = NoteDatabaseHelper(requireContext())
        notesList = db.getAllNotes()
        notesAdapter = NotesAdapter(notesList, requireContext())

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRecyclerView.adapter = notesAdapter

        // Set up search functionality
        binding.notesSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterNotes(s.toString())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        notesList = db.getAllNotes()
        notesAdapter.refreshData(notesList)
    }

    private fun filterNotes(query: String) {
        val filteredNotes = ArrayList<Note>()
        for (note in notesList) {
            if (note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)) {
                filteredNotes.add(note)
            }
        }
        notesAdapter.filterList(filteredNotes)
    }
}
