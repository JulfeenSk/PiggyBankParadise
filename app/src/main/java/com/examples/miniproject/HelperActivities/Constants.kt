package com.examples.miniproject.HelperActivities

import com.examples.miniproject.HelperActivities.models.Category
import com.examples.miniproject.R

object Constants {
    const val INCOME = "INCOME"
    const val EXPENSE = "EXPENSE"


    fun setCategories() = {
        categories.clear()
        categories.add(Category("Salary", R.drawable.ic_salary, R.color.yellow))
        categories.add(Category("Fees", R.drawable.ic_business, R.color.category2))
        categories.add(Category("Food", R.drawable.ic_food, R.color.category3))
        categories.add(Category("Loan", R.drawable.ic_loan, R.color.category4))
        categories.add(Category("Rent", R.drawable.ic_rent, R.color.category5))


    }

    val categories = arrayListOf(
        Category("Salary", R.drawable.ic_salary, R.color.yellow),
        Category("Fees", R.drawable.ic_business, R.color.category2),
        Category("Food", R.drawable.ic_food, R.color.category3),
        Category("Loan", R.drawable.ic_loan, R.color.category4),
        Category("Rent", R.drawable.ic_rent, R.color.category5),
        Category("Other", R.drawable.ic_other, R.color.category6)
    )

    const val DAILY = 0
    const val MONTHLY = 1
    const val CALENDAR = 2
    const val SUMMARY = 3
    const val NOTES = 4

    var SELECTED_TAB = 0
    var SELECTED_TAB_STATS = 0
    var SELECTED_STATS_TYPE = INCOME

    fun getCategoryDetails(categoryName: String?): Category? {
        return categories.find { it.categoryName == categoryName }
    }

    fun getAccountsColor(accountName: String?): Int {
        return when (accountName) {
            "Primary Bank" -> R.color.bank_color
            "Cash" -> R.color.cash_color
            "Savings" -> R.color.card_color
            "Secondary Bank" -> R.color.orange
            else -> R.color.default_color
        }
    }
}
