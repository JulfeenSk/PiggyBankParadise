package com.examples.miniproject.HelperActivities

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.examples.miniproject.Database.TransactionsDatabseHelper
import com.examples.miniproject.HelperActivities.models.Transaction
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val transactions = MutableLiveData<List<Transaction>>()
    val categoriesTransactions = MutableLiveData<List<Transaction>>()
    val totalIncome = MutableLiveData<Double>()
    val totalExpense = MutableLiveData<Double>()
    val totalAmount = MutableLiveData<Double>()

    private var calendar: Calendar = Calendar.getInstance()
    private val dbHelper: TransactionsDatabseHelper by lazy { TransactionsDatabseHelper(application) }

    fun getTransactions(calendar: Calendar, type: String) {
        val transactions: List<Transaction> = if (Constants.SELECTED_TAB_STATS == Constants.DAILY) {
            dbHelper.getTransactionsForDay(calendar, type)
        } else {
            dbHelper.getTransactionsForMonth(calendar, type)
        }
        categoriesTransactions.postValue(transactions)
    }

    fun getTransactions(calendar: Calendar) {
        this.calendar = calendar

        val transactions: List<Transaction> = if (Constants.SELECTED_TAB == Constants.DAILY) {
            dbHelper.getTransactionsForDay(calendar)
        } else {
            dbHelper.getTransactionsForMonth(calendar)
        }

        val income = transactions.filter { it.type == Constants.INCOME }.sumOf { it.amount }
        val expense = transactions.filter { it.type == Constants.EXPENSE }.sumOf { it.amount }
        val total = transactions.sumOf { it.amount }

        totalIncome.postValue(income)
        totalExpense.postValue(expense)
        totalAmount.postValue(total)

        this.transactions.postValue(transactions)
    }


    fun addTransaction(transaction: Transaction) {
        dbHelper.addTransaction(transaction)
        refreshTransactions()
    }

    fun deleteTransaction(transactionId: Long) {
        dbHelper.deleteTransaction(transactionId)
        refreshTransactions()
    }


    fun calculateTotals(transactions: List<Transaction>) {
        val income = transactions.filter { it.type == Constants.INCOME }.sumOf { it.amount }
        val expense = transactions.filter { it.type == Constants.EXPENSE }.sumOf { it.amount }
        totalIncome.value = income
        totalExpense.value = expense
        totalAmount.value = income + expense // Calculate difference between income and expenses
    }

    fun refreshTransactions() {
        val allTransactions = dbHelper.getTransactions(calendar)
        transactions.value = allTransactions.sortedByDescending { it.date }
        calculateTotals(allTransactions)
        getTransactions(calendar)
    }

}
