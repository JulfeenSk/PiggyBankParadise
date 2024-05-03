package com.examples.miniproject.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.examples.miniproject.HelperActivities.Constants
import com.examples.miniproject.HelperActivities.Helper
import com.examples.miniproject.HelperActivities.MainViewModel
import com.examples.miniproject.R
import com.examples.miniproject.databinding.FragmentStatsBinding
import com.google.android.material.tabs.TabLayout
import java.util.Calendar

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupListeners()
        setupChart()

        viewModel.getTransactions(calendar, Constants.SELECTED_STATS_TYPE)
    }

    private fun setupListeners() {
        binding.incomeBtn.setOnClickListener {
            setSelectedType(Constants.INCOME)
            updateDate()
        }

        binding.expenseBtn.setOnClickListener {
            setSelectedType(Constants.EXPENSE)
            updateDate()
        }

        binding.nextDateBtn.setOnClickListener {
            calendar.add(if (Constants.SELECTED_TAB_STATS == Constants.DAILY) Calendar.DATE else Calendar.MONTH, 1)
            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            calendar.add(if (Constants.SELECTED_TAB_STATS == Constants.DAILY) Calendar.DATE else Calendar.MONTH, -1)
            updateDate()
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when (it.text) {
                        "Monthly" -> {
                            Constants.SELECTED_TAB_STATS = Constants.MONTHLY
                            updateDate()
                        }
                        "Daily" -> {
                            Constants.SELECTED_TAB_STATS = Constants.DAILY
                            updateDate()
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupChart() {
        val pie = AnyChart.pie()

        viewModel.categoriesTransactions.observe(viewLifecycleOwner, Observer { transactions ->
            if (transactions.isNotEmpty()) {
                binding.emptyState.visibility = View.GONE
                binding.anyChart.visibility = View.VISIBLE

                val data = ArrayList<DataEntry>()
                val categoryMap = HashMap<String, Double>()

                transactions.forEach { transaction ->
                    val category: String = transaction.category ?: ""
                    val amount = transaction.amount

                    val currentTotal = categoryMap[category] ?: 0.0
                    categoryMap[category] = currentTotal + Math.abs(amount)
                }


                categoryMap.forEach { (category, value) ->
                    data.add(ValueDataEntry(category, value))
                }

                pie.data(data)
            } else {
                binding.emptyState.visibility = View.VISIBLE
                binding.anyChart.visibility = View.GONE
            }
        })

        binding.anyChart.setChart(pie)
    }

    val helper = Helper()
    private fun updateDate() {
        binding.currentDate.text = when (Constants.SELECTED_TAB_STATS) {
            Constants.DAILY -> helper.formatDate(calendar.time)
            Constants.MONTHLY -> helper.formatDateByMonth(calendar.time)
            else -> ""
        }
        viewModel.getTransactions(calendar, Constants.SELECTED_STATS_TYPE)
    }

    private fun setSelectedType(type: String) {
        when (type) {
            Constants.INCOME -> {
                binding.incomeBtn.setBackgroundResource(R.drawable.income_selector)
                binding.expenseBtn.setBackgroundResource(R.drawable.default_selector)
                binding.incomeBtn.setTextColor(requireContext().getColor(R.color.greenColor))
                binding.expenseBtn.setTextColor(requireContext().getColor(R.color.textColor))
            }
            Constants.EXPENSE -> {
                binding.incomeBtn.setBackgroundResource(R.drawable.default_selector)
                binding.expenseBtn.setBackgroundResource(R.drawable.expense_selector)
                binding.incomeBtn.setTextColor(requireContext().getColor(R.color.textColor))
                binding.expenseBtn.setTextColor(requireContext().getColor(R.color.redColor))
            }
        }
        Constants.SELECTED_STATS_TYPE = type
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}