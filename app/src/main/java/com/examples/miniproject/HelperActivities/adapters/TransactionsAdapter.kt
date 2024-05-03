package com.examples.miniproject.HelperActivities.adapters

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.examples.miniproject.HelperActivities.Constants
import com.examples.miniproject.HelperActivities.Helper
import com.examples.miniproject.HelperActivities.MainViewModel
import com.examples.miniproject.HelperActivities.models.Transaction
import com.examples.miniproject.R
import com.examples.miniproject.databinding.FragmentHomeBinding
import com.examples.miniproject.databinding.RowTransactionBinding

class TransactionsAdapter(
    private val context: Context,
    private var transactions: MutableList<Transaction>,
    private val viewModel: MainViewModel

) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    private val helper = Helper() // Initialize the helper property
    private val selectedItems = HashSet<Int>()
    private lateinit var binding: FragmentHomeBinding




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = RowTransactionBinding.inflate(inflater, parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]

        holder.binding.transactionAmount.text = transaction.amount.toString()
        holder.binding.accountLbl.text = transaction.account

        holder.binding.transactionDate.text = helper.formatDate(transaction.date!!) // Assuming date is non-null
        holder.binding.transactionCategory.text = transaction.category



        val transactionCategory = Constants.getCategoryDetails(transaction.category)
        if (transactionCategory != null) {
            holder.binding.categoryIcon.setImageResource(transactionCategory.categoryImage)
        }



        holder.binding.categoryIcon.backgroundTintList =
            transactionCategory?.let { ContextCompat.getColorStateList(context, it.categoryColor) }
        holder.binding.accountLbl.backgroundTintList =
            ContextCompat.getColorStateList(context, Constants.getAccountsColor(transaction.account))

        val color = when (transaction.type) {
            Constants.INCOME -> R.color.greenColor
            Constants.EXPENSE -> R.color.redColor
            else -> R.color.black
        }
        holder.binding.transactionAmount.setTextColor(ContextCompat.getColor(context, color))

        holder.itemView.setOnLongClickListener {
            showDeleteConfirmationDialog(transaction)
            true
        }





        // Handle item click for regular selection (if needed)
        holder.itemView.setOnClickListener {
            // Perform actions on item click
        }
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    fun setTransactions(transactions: List<Transaction>) {
        this.transactions.clear() // Clear the existing list
        this.transactions.addAll(transactions) // Add all transactions from the new list
        notifyDataSetChanged()
    }


    private fun showDeleteConfirmationDialog(transaction: Transaction) {
        AlertDialog.Builder(context)
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteTransaction(transaction.id) // Pass transaction ID to delete
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteTransaction(transaction: Transaction) {
        transactions.remove(transaction)
        notifyDataSetChanged() // Notify adapter about the change
        viewModel.calculateTotals(transactions) // Recalculate totals after deletion
    }
    private fun toggleSelection(position: Int) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position)
        } else {
            selectedItems.add(position)
        }
        notifyItemChanged(position)
    }
    fun deleteSelectedItems() {
        val sortedSelectedItems = selectedItems.sortedDescending()
        sortedSelectedItems.forEach { position ->
            transactions.removeAt(position)
            notifyItemRemoved(position)
        }
        selectedItems.clear()
    }

    fun getSelectedTransactions(): List<Transaction> {
        return selectedItems.map { transactions[it] }
    }

    fun clearSelection() {
        selectedItems.clear()
        notifyDataSetChanged()
    }


    class TransactionViewHolder(val binding: RowTransactionBinding) : RecyclerView.ViewHolder(binding.root)
}
