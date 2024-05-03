package com.examples.miniproject.HelperActivities.models

import java.util.Date

data class Transaction(
    var type: String? = null,
    var category: String? = null,
    var account: String? = null,
    var note: String? = null,
    var date: Date? = null,
    var amount: Double = 0.0,
    var id: Long = 0
)
