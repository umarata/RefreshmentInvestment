package com.tribyssapps.refreshmentinvestment.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tribyssapps.refreshmentinvestment.R
import com.tribyssapps.refreshmentinvestment.database.InvestmentEntity
import java.text.SimpleDateFormat
import java.util.*

class InvestmentReportRVAdapter(internal var context: Context, internal var investmentFromDateRange: List<InvestmentEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.investment_report_items, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ViewHolder
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        viewHolder.textViewDate.text = simpleDateFormat.format(investmentFromDateRange[viewHolder.adapterPosition].date)
        viewHolder.textViewInvestmentName.text = investmentFromDateRange[viewHolder.adapterPosition].name
        viewHolder.textViewCredit.text = String.format("Rs. %s", investmentFromDateRange[viewHolder.adapterPosition].cr)
        viewHolder.textViewDebit.text = String.format("Rs. %s", investmentFromDateRange[viewHolder.adapterPosition].dr)
        viewHolder.textViewBalance.text = String.format("Rs. %s", investmentFromDateRange[viewHolder.adapterPosition].balance)
    }

    override fun getItemCount(): Int {
        return investmentFromDateRange.size
    }

    internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewDate: TextView
        var textViewInvestmentName: TextView
        var textViewAmount: TextView? = null
        var textViewBalance: TextView
        var textViewCredit: TextView
        var textViewDebit: TextView

        init {
            textViewDate = itemView.findViewById(R.id.textViewDate)
            textViewInvestmentName = itemView.findViewById(R.id.textViewInvestmentName)
            textViewBalance = itemView.findViewById(R.id.textViewBalance)
            textViewCredit = itemView.findViewById(R.id.textViewCredit)
            textViewDebit = itemView.findViewById(R.id.textViewDebit)

        }
    }
}
