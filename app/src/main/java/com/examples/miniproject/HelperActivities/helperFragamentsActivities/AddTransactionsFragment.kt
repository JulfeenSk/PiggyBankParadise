package com.examples.miniproject.HelperActivities.helperFragamentsActivities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.examples.miniproject.HelperActivities.Constants
import com.examples.miniproject.HelperActivities.adapters.AccountsAdapter
import com.examples.miniproject.HelperActivities.adapters.CategoryAdapter
import com.examples.miniproject.HelperActivities.models.Account
import com.examples.miniproject.HelperActivities.models.Category
import com.examples.miniproject.HelperActivities.models.Transaction
import com.examples.miniproject.HomeScreen
import com.examples.miniproject.R
import com.examples.miniproject.databinding.FragmentAddTransactionsBinding
import com.examples.miniproject.databinding.ListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class AddTransactionsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddTransactionsBinding
    private var transaction = Transaction()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTransactionsBinding.inflate(inflater)

        binding.incomeBtn.setOnClickListener {
            binding.incomeBtn.background = requireContext().getDrawable(R.drawable.income_selector)
            binding.expenseBtn.background = requireContext().getDrawable(R.drawable.default_selector)
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.greenColor))

            transaction.type = Constants.INCOME
        }

        binding.expenseBtn.setOnClickListener {
            binding.incomeBtn.background = requireContext().getDrawable(R.drawable.default_selector)
            binding.expenseBtn.background = requireContext().getDrawable(R.drawable.expense_selector)
            binding.incomeBtn.setTextColor(requireContext().getColor(R.color.textColor))
            binding.expenseBtn.setTextColor(requireContext().getColor(R.color.redColor))

            transaction.type = Constants.EXPENSE
        }

        binding.date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { datePicker, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                val dateToShow = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault()).format(calendar.time)
                binding.date.text = Editable.Factory.getInstance().newEditable(dateToShow)

                transaction.date = calendar.time
                transaction.id = calendar.time.time
            }
            datePickerDialog.show()
        }

        binding.category.setOnClickListener {
            val dialogBinding = ListDialogBinding.inflate(layoutInflater)
            val categoryDialog = AlertDialog.Builder(requireContext()).create()
            categoryDialog.setView(dialogBinding.root)

            val categoryAdapter = CategoryAdapter(requireContext(), Constants.categories, object : CategoryAdapter.CategoryClickListener {
                override fun onCategoryClicked(category: Category) {
                    binding.category.text = Editable.Factory.getInstance().newEditable(category.categoryName)
                    transaction.category = category.categoryName
                    categoryDialog.dismiss()
                }
            })
            dialogBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            dialogBinding.recyclerView.adapter = categoryAdapter

            categoryDialog.show()
        }

        binding.account.setOnClickListener {
            val dialogBinding = ListDialogBinding.inflate(layoutInflater)
            val accountsDialog = AlertDialog.Builder(requireContext()).create()
            accountsDialog.setView(dialogBinding.root)

            val accounts = ArrayList<Account>()
            accounts.add(Account(0.0, "Cash"))
            accounts.add(Account(0.0, "Primary Bank"))
            accounts.add(Account(0.0, "Secondary Bank"))
            accounts.add(Account(0.0, "Savings"))
            accounts.add(Account(0.0, "Other"))

            val adapter = AccountsAdapter(requireContext(), accounts, object : AccountsAdapter.AccountsClickListener {
                override fun onAccountSelected(account: Account) {
                    binding.account.text = Editable.Factory.getInstance().newEditable(account.accountName)
                    transaction.account = account.accountName
                    accountsDialog.dismiss()
                }
            })
            dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            dialogBinding.recyclerView.adapter = adapter

            accountsDialog.show()
        }

        binding.saveTransactionBtn.setOnClickListener {
            val amountStr = binding.amount.text.toString()
            if (amountStr.isNotEmpty()) {
                val amount = amountStr.toDouble()
                val note = binding.note.text.toString()

                // Check if all mandatory fields are filled
                if (transaction.type != null && transaction.category != null && transaction.account != null) {
                    transaction.amount = if (transaction.type == Constants.EXPENSE) -amount else amount
                    transaction.note = note

                    (requireActivity() as? HomeScreen)?.viewModel?.addTransaction(transaction)
                    // Refresh transactions after adding a new one
                    dismiss()
                } else {
                    // Show error message for unfilled mandatory fields
                    Toast.makeText(requireContext(), "Please fill all mandatory fields", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show error message for empty amount field
                Toast.makeText(requireContext(), "Please enter amount", Toast.LENGTH_SHORT).show()
            }
        }



        return binding.root
    }
}