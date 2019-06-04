package com.tribyssapps.refreshmentinvestment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tribyssapps.refreshmentinvestment.R;
import com.tribyssapps.refreshmentinvestment.database.InvestmentEntity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InvestmentReportRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<InvestmentEntity> investmentFromDateRange;

    public InvestmentReportRVAdapter(Context context, List<InvestmentEntity> investmentFromDateRange) {
        this.context = context;
        this.investmentFromDateRange = investmentFromDateRange;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.investment_report_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        viewHolder.textViewDate.setText(simpleDateFormat.format(investmentFromDateRange.get(viewHolder.getAdapterPosition()).getDate()));
        viewHolder.textViewInvestmentName.setText(investmentFromDateRange.get(viewHolder.getAdapterPosition()).getName());
        viewHolder.textViewCredit.setText(String.format("Rs. %s", investmentFromDateRange.get(viewHolder.getAdapterPosition()).getCr()));
        viewHolder.textViewDebit.setText(String.format("Rs. %s", investmentFromDateRange.get(viewHolder.getAdapterPosition()).getDr()));
        viewHolder.textViewBalance.setText(String.format("Rs. %s", investmentFromDateRange.get(viewHolder.getAdapterPosition()).getBalance()));
    }

    @Override
    public int getItemCount() {
        return investmentFromDateRange.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewDate,textViewInvestmentName,textViewAmount,textViewBalance,textViewCredit,textViewDebit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate=itemView.findViewById(R.id.textViewDate);
            textViewInvestmentName=itemView.findViewById(R.id.textViewInvestmentName);
            textViewBalance=itemView.findViewById(R.id.textViewBalance);
            textViewCredit=itemView.findViewById(R.id.textViewCredit);
            textViewDebit=itemView.findViewById(R.id.textViewDebit);

        }
    }
}
