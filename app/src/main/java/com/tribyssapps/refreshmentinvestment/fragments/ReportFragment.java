package com.tribyssapps.refreshmentinvestment.fragments;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tribyssapps.refreshmentinvestment.R;
import com.tribyssapps.refreshmentinvestment.adapters.InvestmentReportRVAdapter;
import com.tribyssapps.refreshmentinvestment.database.AppDatabase;
import com.tribyssapps.refreshmentinvestment.database.InvestmentEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {


    Calendar myCalendarStartDate;
    DatePickerDialog.OnDateSetListener StartDate;
    DatePickerDialog.OnDateSetListener EndDate;
    Calendar myCalendarEndDate;
    Date startDate, endDate;
    TextView textViewTotalAmountGiven, textViewTotalAmountInvested;
    public ReportFragment() {
        // Required empty public constructor
    }

    public static ReportFragment getInstance() {
        return new ReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        TextView textViewStartDate = view.findViewById(R.id.textViewStartDate);
        TextView textViewEndDate = view.findViewById(R.id.textViewEndDate);

        textViewStartDate.setOnClickListener(v -> {
            openStartDatePicker();
        });
        textViewEndDate.setOnClickListener(v -> {
            openEndDatePicker();
        });
        Button buttonGenerate = view.findViewById(R.id.buttonGenerate);
        textViewTotalAmountInvested = view.findViewById(R.id.textViewTotalAmountInvested);
        textViewTotalAmountGiven = view.findViewById(R.id.textViewTotalAmountGiven);
        myCalendarStartDate = Calendar.getInstance();
        StartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStartDate.set(Calendar.YEAR, year);
                myCalendarStartDate.set(Calendar.MONTH, monthOfYear);
                myCalendarStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendarStartDate.set(Calendar.MINUTE, 0);
                myCalendarStartDate.set(Calendar.HOUR_OF_DAY, 0);
                startDate = myCalendarStartDate.getTime();
                updateLabel(startDate, textViewStartDate);
                //updateLabel();
            }
        };
        myCalendarEndDate = Calendar.getInstance();
        EndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarEndDate.set(Calendar.YEAR, year);
                myCalendarEndDate.set(Calendar.MONTH, monthOfYear);
                myCalendarEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                //myCalendarEndDate.set(Calendar.MINUTE,0);
                // myCalendarEndDate.set(Calendar.HOUR_OF_DAY,0);
                endDate = myCalendarEndDate.getTime();
                updateLabel(endDate, textViewEndDate);
                //updateLabel();
            }
        };

        buttonGenerate.setOnClickListener(v -> {
            if (textViewStartDate.getText().toString().isEmpty() || textViewEndDate.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please select start/end dates", Toast.LENGTH_LONG).show();
                return;
            }

            if (startDate != null && endDate != null) {
                AppDatabase localDataBase = AppDatabase.getLocalDataBase(getActivity().getApplicationContext());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<InvestmentEntity> investmentFromDateRange = localDataBase.getInvestmentDAO().getInvestmentFromDateRange(startDate, endDate);
                        InvestmentEntity balanceSheet = localDataBase.getInvestmentDAO().getBalanceFromRange(startDate, endDate);
                        double credit = localDataBase.getInvestmentDAO().getCreditFromRange(startDate, endDate);
                        double debit = localDataBase.getInvestmentDAO().getDebitFromRange(startDate, endDate);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(new InvestmentReportRVAdapter(getActivity(), investmentFromDateRange));
                                textViewTotalAmountGiven.setText("Rs " + balanceSheet.getCr());
                                textViewTotalAmountInvested.setText("Rs " + balanceSheet.getDr());
                                Log.e("total credit", "is " + credit);
                                Log.e("total debit", "is " + debit);
                            }
                        });
                    }
                }).start();
            }


        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void updateLabel(Date date, TextView textView) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        textView.setText(simpleDateFormat.format(date));
    }


    private void openEndDatePicker() {
        new DatePickerDialog(getActivity(), EndDate, myCalendarEndDate
                .get(Calendar.YEAR), myCalendarEndDate.get(Calendar.MONTH),
                myCalendarEndDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void openStartDatePicker() {
        new DatePickerDialog(getActivity(), StartDate, myCalendarStartDate
                .get(Calendar.YEAR), myCalendarStartDate.get(Calendar.MONTH),
                myCalendarStartDate.get(Calendar.DAY_OF_MONTH)).show();
    }
}
