package com.examples.miniproject.Fragments


import android.content.Context.MODE_APPEND
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.examples.miniproject.R

class FeedbackFragment : Fragment() {

    private val file = "feedback.txt"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)

        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val feedback = view.findViewById<EditText>(R.id.feedback)
        val submit = view.findViewById<Button>(R.id.submit)
        val viewFeedback = view.findViewById<TextView>(R.id.viewFeedback)
        val viewBtn = view.findViewById<Button>(R.id.viewBtn)
        val clear = view.findViewById<Button>(R.id.clear)

        submit.setOnClickListener {
            val rating = ratingBar.rating
            val feed = feedback.text.toString()

            val data = " Rating : $rating\nFeedback : $feed\n\n"

            try {
                val fOut = requireContext().openFileOutput(file, MODE_APPEND)
                fOut.write(data.toByteArray())
                fOut.close()
                feedback.text.clear()
                Toast.makeText(requireContext(), "Feedback saved Successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Could not save details", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        viewBtn.setOnClickListener {
            try {
                val fin = requireContext().openFileInput(file)
                var c: Int
                var temp = ""
                while (fin.read().also { c = it } != -1) {
                    temp += c.toChar().toString()
                }

                viewFeedback.text = temp
                fin.close()
                Toast.makeText(requireContext(), "File read successfully", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Unable to read file", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        clear.setOnClickListener {
            feedback.text.clear()
            viewFeedback.text = ""
        }

        return view
    }
}
