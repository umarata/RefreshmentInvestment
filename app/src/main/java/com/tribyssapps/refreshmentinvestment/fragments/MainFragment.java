package com.tribyssapps.refreshmentinvestment.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tribyssapps.refreshmentinvestment.R;
import com.tribyssapps.refreshmentinvestment.activities.MainActivity;
import com.tribyssapps.refreshmentinvestment.database.AppDatabase;
import com.tribyssapps.refreshmentinvestment.database.InvestmentEntity;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    private int selectedPosition=0;
    private TextView textViewBalance;
    private EditText editTextPrice,editTextAmount;
    private Button buttonAddBalance,buttonAddInvestment,buttonGenerateReport,buttonRefreshCurrentBalance;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    private String [] stringsArray;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getActivity()==null)return;
        textViewBalance=view.findViewById(R.id.textViewBalance);
        editTextPrice=view.findViewById(R.id.editTextPrice);
        editTextAmount=view.findViewById(R.id.editTextAmount);
        buttonAddBalance=view.findViewById(R.id.buttonAddBalance);
        buttonAddInvestment=view.findViewById(R.id.buttonAddInvestment);
        buttonGenerateReport=view.findViewById(R.id.buttonGenerateReport);
        buttonRefreshCurrentBalance=view.findViewById(R.id.buttonRefreshCurrentBalance);
        Spinner spinner =view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        stringsArray=getResources().getStringArray(R.array.spinner_array);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        buttonAddBalance.setOnClickListener(v-> checkAmountAndSave());
        buttonAddInvestment.setOnClickListener(v-> checkInvestmentAndSave());
        buttonGenerateReport.setOnClickListener(v->{
            generateReport();
        });
        updateCurrentBalance();
        buttonRefreshCurrentBalance.setOnClickListener(v->updateCurrentBalance());

        super.onViewCreated(view, savedInstanceState);
    }

    private void updateCurrentBalance() {
        AppDatabase appDatabase=AppDatabase.getLocalDataBase(getActivity().getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                double currentBalance = appDatabase.getInvestmentDAO().getCurrentBalance();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textViewBalance.setText(String.format("Rs. %s", currentBalance));
                    }
                });
            }
        }).start();

    }

    private void generateReport() {
       /* AppDatabase appDatabase=AppDatabase.getLocalDataBase(getActivity().getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                double currentBalance = appDatabase.getInvestmentDAO().getCurrentBalance();
                List<InvestmentEntity> allInvestments = appDatabase.getInvestmentDAO().getAllInvestments();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //allInvestments;


                    }
                });
                Log.e("total investments",allInvestments.size()+" size");
            }
        }).start();*/
        ((MainActivity)getActivity()).addNewFragment(ReportFragment.getInstance());
    }

    private void checkInvestmentAndSave() {
        String investmentName=stringsArray[selectedPosition];
        String price=editTextPrice.getText().toString();
        if (price.isEmpty()){
            Toast.makeText(getActivity(),"Please enter investment price",Toast.LENGTH_LONG).show();
            return;
        }
        double priceDouble=Double.parseDouble(price);
        AppDatabase appDatabase=AppDatabase.getLocalDataBase(getActivity().getApplicationContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                double balance=appDatabase.getInvestmentDAO().getCurrentBalance();
                InvestmentEntity investmentEntity=new InvestmentEntity();
                investmentEntity.setCr(0);
                investmentEntity.setDate(new Date());
                investmentEntity.setName(investmentName);
                investmentEntity.setDr(priceDouble);
                investmentEntity.setBalance(balance-priceDouble);
                appDatabase.getInvestmentDAO().insertInvestment(investmentEntity);
                double currentBalance = appDatabase.getInvestmentDAO().getCurrentBalance();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textViewBalance.setText(String.format("Rs. %s", currentBalance));
                        editTextPrice.setText("");
                    }
                });

            }
        }).start();
    }

    private void checkAmountAndSave() {
        String stringAmount=editTextAmount.getText().toString();
        if (stringAmount.isEmpty()){
            Toast.makeText(getActivity(),"Please enter amount!!",Toast.LENGTH_LONG).show();
            return;
        }
        double amount=Double.parseDouble(stringAmount);
        AppDatabase appDatabase=AppDatabase.getLocalDataBase(getActivity().getApplicationContext());
        new Thread(() -> {
            double balance=appDatabase.getInvestmentDAO().getCurrentBalance();
            InvestmentEntity investmentEntity=new InvestmentEntity();
            investmentEntity.setCr(amount);
            investmentEntity.setDate(new Date());
            investmentEntity.setName("Adding money");
            investmentEntity.setDr(0);
            investmentEntity.setBalance(balance+amount);
            appDatabase.getInvestmentDAO().insertInvestment(investmentEntity);
            double currentBalance = appDatabase.getInvestmentDAO().getCurrentBalance();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    textViewBalance.setText(String.format("Rs. %s", currentBalance));
                    editTextAmount.setText("");
                }
            });

        }).start();
    }
}
