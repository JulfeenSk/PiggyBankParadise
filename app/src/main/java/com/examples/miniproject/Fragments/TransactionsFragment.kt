package com.examples.miniproject.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.examples.miniproject.HelperActivities.Constants
import com.examples.miniproject.HelperActivities.Helper
import com.examples.miniproject.HelperActivities.MainViewModel
import com.examples.miniproject.HelperActivities.adapters.TransactionsAdapter
import com.examples.miniproject.R
import com.examples.miniproject.databinding.FragmentTransactionsBinding
import com.google.android.material.tabs.TabLayout
import java.util.Calendar


class TransactionsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionsBinding
    lateinit var viewModel: MainViewModel
    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var transactionsAdapter: TransactionsAdapter
    private lateinit var helper: Helper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTransactionsBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        transactionsAdapter = TransactionsAdapter(requireContext(), mutableListOf(),viewModel)
        helper = Helper()

        updateDate()
        viewModel.refreshTransactions()


        binding.nextDateBtn.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            updateDate()
            calendar.add(Calendar.MONTH, 1) // Move to the next month
            updateDate()
        }

        binding.nextDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1)
            } else {
                calendar.add(Calendar.MONTH, 1)
            }
            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1)
            } else {
                calendar.add(Calendar.MONTH, -1)
            }
            updateDate()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Monthly" -> {
                        Constants.SELECTED_TAB = Constants.MONTHLY
                        updateDate()
                    }
                    "Daily" -> {
                        Constants.SELECTED_TAB = Constants.DAILY
                        calendar = Calendar.getInstance()
                        updateDate()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.transactionsList.layoutManager = LinearLayoutManager(context)
        binding.transactionsList.adapter = transactionsAdapter

        viewModel.transactions.observe(viewLifecycleOwner) { transactions ->
            transactionsAdapter.setTransactions(transactions)
            binding.emptyState.visibility = if (transactions.isNotEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
            binding.incomeLbl.text = income.toString()
        }

        viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
            binding.expenseLbl.text = expense.toString()
        }

        viewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            binding.totalLbl.text = total.toString()
        }

        viewModel.getTransactions(calendar)

        return binding.root
    }

    private fun updateDate() {
        binding.currentDate.text = if (Constants.SELECTED_TAB == Constants.DAILY) {
            helper.formatDate(calendar.time)
        } else {
            helper.formatDateByMonth(calendar.time)
        }
        viewModel.getTransactions(calendar)
    }



    override fun onResume() {
        super.onResume()
        viewModel.refreshTransactions()
    }
}
