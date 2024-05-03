package com.examples.miniproject.HelperActivities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.examples.miniproject.HelperActivities.models.Account
import com.examples.miniproject.R
import com.examples.miniproject.databinding.RowAccountBinding

class AccountsAdapter(
    private val context: Context,
    private val accountArrayList: ArrayList<Account>,
    private val accountsClickListener: AccountsClickListener
) : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    interface AccountsClickListener {
        fun onAccountSelected(account: Account)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_account, parent, false)
        return AccountsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        val account = accountArrayList[position]
        holder.binding.accountName.text = account.accountName
        holder.itemView.setOnClickListener {
            accountsClickListener.onAccountSelected(account)
        }
    }

    override fun getItemCount(): Int {
        return accountArrayList.size
    }

    inner class AccountsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RowAccountBinding.bind(itemView)
    }
}
