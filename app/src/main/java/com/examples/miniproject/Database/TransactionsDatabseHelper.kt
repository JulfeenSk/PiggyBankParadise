package com.examples.miniproject.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.examples.miniproject.HelperActivities.models.Transaction
import java.util.Calendar
import java.util.Date

class TransactionsDatabseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "eManager.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TRANSACTIONS = "transactions"

        // Transaction Table Columns
        private const val COL_ID = "id"
        private const val COL_TYPE = "type"
        private const val COL_CATEGORY = "category"
        private const val COL_ACCOUNT = "account"
        private const val COL_NOTE = "note"
        private const val COL_DATE = "date"
        private const val COL_AMOUNT = "amount"
        private const val COL_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_TRANSACTIONS ("
                + "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COL_TYPE TEXT NOT NULL, "
                + "$COL_CATEGORY TEXT, "
                + "$COL_ACCOUNT TEXT, "
                + "$COL_NOTE TEXT, "
                + "$COL_DATE INTEGER NOT NULL, "
                + "$COL_AMOUNT REAL NOT NULL, "
                + "$COL_TIMESTAMP INTEGER NOT NULL"
                + ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades if needed
    }

    fun addTransaction(transaction: Transaction) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COL_TYPE, transaction.type)
            put(COL_CATEGORY, transaction.category)
            put(COL_ACCOUNT, transaction.account)
            put(COL_NOTE, transaction.note)
            put(COL_DATE, transaction.date?.time ?: Date().time) // Use current time if date is null
            put(COL_AMOUNT, transaction.amount)
            put(COL_TIMESTAMP, System.currentTimeMillis())
        }
        db.insert(TABLE_TRANSACTIONS, null, contentValues)
        db.close()
    }

    fun deleteTransaction(timestamp: Long): Boolean {
        val db = writableDatabase
        val deletedRows = db.delete(TABLE_TRANSACTIONS, "$COL_TIMESTAMP = ?", arrayOf(timestamp.toString()))
        db.close()
        return deletedRows > 0
    }





    fun getTransactions(calendar: Calendar, type: String): List<Transaction> {
        val db = readableDatabase
        val transactionsList = mutableListOf<Transaction>()

        val calendarStart = calendar.clone() as Calendar
        calendarStart.set(Calendar.HOUR_OF_DAY, 0)
        calendarStart.set(Calendar.MINUTE, 0)
        calendarStart.set(Calendar.SECOND, 0)
        calendarStart.set(Calendar.MILLISECOND, 0)

        val calendarEnd = calendarStart.clone() as Calendar
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)

        val selection = "$COL_DATE >= ? AND $COL_DATE < ? AND $COL_TYPE = ?"
        val selectionArgs = arrayOf(
            calendarStart.timeInMillis.toString(),
            calendarEnd.timeInMillis.toString(),
            type
        )

        val cursor = db.query(
            TABLE_TRANSACTIONS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_TIMESTAMP DESC"
        )

        while (cursor.moveToNext()) {
            val transaction = Transaction(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE)),
                Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE))),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
            )
            transactionsList.add(transaction)
        }

        cursor.close()
        db.close()

        return transactionsList
    }

    fun getTransactions(calendar: Calendar): List<Transaction> {
        val db = readableDatabase
        val transactionsList = mutableListOf<Transaction>()

        val calendarStart = calendar.clone() as Calendar
        calendarStart.set(Calendar.HOUR_OF_DAY, 0)
        calendarStart.set(Calendar.MINUTE, 0)
        calendarStart.set(Calendar.SECOND, 0)
        calendarStart.set(Calendar.MILLISECOND, 0)

        val calendarEnd = calendarStart.clone() as Calendar
        calendarEnd.add(Calendar.DAY_OF_MONTH, 1)

        val selection = "$COL_DATE >= ? AND $COL_DATE < ?"
        val selectionArgs = arrayOf(
            calendarStart.timeInMillis.toString(),
            calendarEnd.timeInMillis.toString()
        )

        val cursor = db.query(
            TABLE_TRANSACTIONS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_DATE DESC"
        )

        while (cursor.moveToNext()) {
            val transaction = Transaction(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE)),
                Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE))),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
            )
            transactionsList.add(transaction)
        }

        cursor.close()
        db.close()

        return transactionsList
    }
    fun getTransactionsForMonth(calendar: Calendar, type: String): List<Transaction> {
        val db = readableDatabase
        val transactionsList = mutableListOf<Transaction>()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar month starts from 0

        val calendarStart = calendar.clone() as Calendar
        calendarStart.set(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 0)
        calendarStart.set(Calendar.MINUTE, 0)
        calendarStart.set(Calendar.SECOND, 0)
        calendarStart.set(Calendar.MILLISECOND, 0)

        val calendarEnd = calendar.clone() as Calendar
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23)
        calendarEnd.set(Calendar.MINUTE, 59)
        calendarEnd.set(Calendar.SECOND, 59)
        calendarEnd.set(Calendar.MILLISECOND, 999)

        val selection = "$COL_DATE >= ? AND $COL_DATE <= ? AND $COL_TYPE = ?"
        val selectionArgs = arrayOf(
            calendarStart.timeInMillis.toString(),
            calendarEnd.timeInMillis.toString(),
            type
        )

        val cursor = db.query(
            TABLE_TRANSACTIONS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_DATE DESC" // Order by date in descending order
        )

        while (cursor.moveToNext()) {
            val transaction = Transaction(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE)),
                Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE))),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
            )
            transactionsList.add(transaction)
        }

        cursor.close()
        db.close()

        return transactionsList
    }

    fun getTransactionsForMonth(calendar: Calendar): List<Transaction> {
        val db = readableDatabase
        val transactionsList = mutableListOf<Transaction>()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar month starts from 0

        val calendarStart = calendar.clone() as Calendar
        calendarStart.set(Calendar.DAY_OF_MONTH, 1)
        calendarStart.set(Calendar.HOUR_OF_DAY, 0)
        calendarStart.set(Calendar.MINUTE, 0)
        calendarStart.set(Calendar.SECOND, 0)
        calendarStart.set(Calendar.MILLISECOND, 0)

        val calendarEnd = calendar.clone() as Calendar
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23)
        calendarEnd.set(Calendar.MINUTE, 59)
        calendarEnd.set(Calendar.SECOND, 59)
        calendarEnd.set(Calendar.MILLISECOND, 999)

        val selection = "$COL_DATE >= ? AND $COL_DATE <= ?"
        val selectionArgs = arrayOf(
            calendarStart.timeInMillis.toString(),
            calendarEnd.timeInMillis.toString()
        )

        val cursor = db.query(
            TABLE_TRANSACTIONS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_DATE DESC" // Order by date in descending order
        )

        while (cursor.moveToNext()) {
            val transaction = Transaction(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE)),
                Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE))),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
            )
            transactionsList.add(transaction)
        }

        cursor.close()
        db.close()

        return transactionsList
    }




    fun getTransactionsForDay(calendar: Calendar, type: String): List<Transaction> {
        val db = readableDatabase
        val transactionsList = mutableListOf<Transaction>()

        val calendarStart = calendar.clone() as Calendar
        calendarStart.set(Calendar.HOUR_OF_DAY, 0)
        calendarStart.set(Calendar.MINUTE, 0)
        calendarStart.set(Calendar.SECOND, 0)
        calendarStart.set(Calendar.MILLISECOND, 0)

        val calendarEnd = calendar.clone() as Calendar
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23)
        calendarEnd.set(Calendar.MINUTE, 59)
        calendarEnd.set(Calendar.SECOND, 59)
        calendarEnd.set(Calendar.MILLISECOND, 999)

        val selection = "$COL_DATE >= ? AND $COL_DATE <= ? AND $COL_TYPE = ?"
        val selectionArgs = arrayOf(
            calendarStart.timeInMillis.toString(),
            calendarEnd.timeInMillis.toString(),
            type
        )

        val cursor = db.query(
            TABLE_TRANSACTIONS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_TIMESTAMP DESC"  // Order by date in descending order
        )


        while (cursor.moveToNext()) {
            val transaction = Transaction(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE)),
                Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE))),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
            )
            transactionsList.add(transaction)
        }

        cursor.close()
        db.close()

        return transactionsList
    }

    fun getTransactionsForDay(calendar: Calendar): List<Transaction> {
        val db = readableDatabase
        val transactionsList = mutableListOf<Transaction>()

        val calendarStart = calendar.clone() as Calendar
        calendarStart.set(Calendar.HOUR_OF_DAY, 0)
        calendarStart.set(Calendar.MINUTE, 0)
        calendarStart.set(Calendar.SECOND, 0)
        calendarStart.set(Calendar.MILLISECOND, 0)

        val calendarEnd = calendar.clone() as Calendar
        calendarEnd.set(Calendar.HOUR_OF_DAY, 23)
        calendarEnd.set(Calendar.MINUTE, 59)
        calendarEnd.set(Calendar.SECOND, 59)
        calendarEnd.set(Calendar.MILLISECOND, 999)

        val selection = "$COL_DATE >= ? AND $COL_DATE <= ?"
        val selectionArgs = arrayOf(
            calendarStart.timeInMillis.toString(),
            calendarEnd.timeInMillis.toString()
        )

        val cursor = db.query(
            TABLE_TRANSACTIONS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            "$COL_TIMESTAMP DESC"  // Order by date in descending order
        )


        while (cursor.moveToNext()) {
            val transaction = Transaction(
                cursor.getString(cursor.getColumnIndexOrThrow(COL_TYPE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_ACCOUNT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTE)),
                Date(cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE))),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT)),
                cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIMESTAMP))
            )
            transactionsList.add(transaction)
        }

        cursor.close()
        db.close()

        return transactionsList
    }


}
