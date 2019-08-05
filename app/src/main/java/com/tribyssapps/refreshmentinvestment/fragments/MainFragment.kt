package com.tribyssapps.refreshmentinvestment.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.tribyssapps.refreshmentinvestment.R
import com.tribyssapps.refreshmentinvestment.activities.MainActivity
import com.tribyssapps.refreshmentinvestment.database.AppDatabase
import com.tribyssapps.refreshmentinvestment.database.InvestmentEntity
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {


    private var selectedPosition = 0
    private var textViewBalance: TextView? = null
    private var editTextPrice: EditText? = null
    private var editTextAmount: EditText? = null
    private var buttonAddBalance: Button? = null
    private var buttonAddInvestment: Button? = null
    private var buttonGenerateReport: Button? = null
    private var buttonRefreshCurrentBalance: Button? = null
    private var stringsArray: Array<String>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (activity == null) return
        textViewBalance = view.findViewById(R.id.textViewBalance)
        editTextPrice = view.findViewById(R.id.editTextPrice)
        editTextAmount = view.findViewById(R.id.editTextAmount)
        buttonAddBalance = view.findViewById(R.id.buttonAddBalance)
        buttonAddInvestment = view.findViewById(R.id.buttonAddInvestment)
        buttonGenerateReport = view.findViewById(R.id.buttonGenerateReport)
        buttonRefreshCurrentBalance = view.findViewById(R.id.buttonRefreshCurrentBalance)
        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(this.activity!!, R.array.spinner_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        stringsArray = resources.getStringArray(R.array.spinner_array)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedPosition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        buttonAddBalance!!.setOnClickListener { v -> checkAmountAndSave() }
        buttonAddInvestment!!.setOnClickListener { v -> checkInvestmentAndSave() }
        buttonGenerateReport!!.setOnClickListener { v -> generateReport() }
        updateCurrentBalance()
        buttonRefreshCurrentBalance!!.setOnClickListener { v -> updateCurrentBalance() }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateCurrentBalance() {
        val appDatabase = AppDatabase.getLocalDataBase(activity!!.applicationContext)
        Thread(Runnable {
            val currentBalance = appDatabase.investmentDAO.currentBalance
            activity!!.runOnUiThread { textViewBalance!!.text = String.format("Rs. %s", currentBalance) }
        }).start()

    }

    private fun generateReport() {
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
        (activity as MainActivity).addNewFragment(ReportFragment.getInstance())
    }

    private fun checkInvestmentAndSave() {
        val investmentName = stringsArray!![selectedPosition]
        val price = editTextPrice!!.text.toString()
        if (price.isEmpty()) {
            Toast.makeText(activity, "Please enter investment price", Toast.LENGTH_LONG).show()
            return
        }
        val priceDouble = java.lang.Double.parseDouble(price)
        val appDatabase = AppDatabase.getLocalDataBase(activity!!.applicationContext)
        Thread(Runnable {
            val balance = appDatabase.investmentDAO.currentBalance
            val investmentEntity = InvestmentEntity()
            investmentEntity.cr = 0.0
            investmentEntity.date = Date()
            investmentEntity.name = investmentName
            investmentEntity.dr = priceDouble
            investmentEntity.balance = balance - priceDouble
            appDatabase.investmentDAO.insertInvestment(investmentEntity)
            val currentBalance = appDatabase.investmentDAO.currentBalance
            activity!!.runOnUiThread {
                textViewBalance!!.text = String.format("Rs. %s", currentBalance)
                editTextPrice!!.setText("")
            }
        }).start()
    }

    private fun checkAmountAndSave() {
        val stringAmount = editTextAmount!!.text.toString()
        if (stringAmount.isEmpty()) {
            Toast.makeText(activity, "Please enter amount!!", Toast.LENGTH_LONG).show()
            return
        }
        val amount = java.lang.Double.parseDouble(stringAmount)
        val appDatabase = AppDatabase.getLocalDataBase(activity!!.applicationContext)
        Thread {
            val balance = appDatabase.investmentDAO.currentBalance
            val investmentEntity = InvestmentEntity()
            investmentEntity.cr = amount
            investmentEntity.date = Date()
            investmentEntity.name = "Adding money"
            investmentEntity.dr = 0.0
            investmentEntity.balance = balance + amount
            appDatabase.investmentDAO.insertInvestment(investmentEntity)
            val currentBalance = appDatabase.investmentDAO.currentBalance
            activity!!.runOnUiThread {
                textViewBalance!!.text = String.format("Rs. %s", currentBalance)
                editTextAmount!!.setText("")
            }

        }.start()
    }
}// Required empty public constructor
