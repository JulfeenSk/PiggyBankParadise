package com.examples.miniproject.HelperActivities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Helper {

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }



    fun formatDateByMonth(date: Date): String {
        val dateFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
}