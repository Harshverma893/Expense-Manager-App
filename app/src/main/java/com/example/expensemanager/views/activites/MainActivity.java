package com.example.expensemanager.views.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.example.expensemanager.Utills.Constants;
import com.example.expensemanager.Utills.Helper;
import com.example.expensemanager.adapters.TransactionAdapter;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.viewmodel.MainViewModel;
import com.example.expensemanager.views.fragments.AddTransactionFragment;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Calendar calendar;
    public MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c ->{

            if (Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE,1);
            }
            else if (Constants.SELECTED_TAB == Constants.MONTH){
                calendar.add(Calendar.MONTH,1);
            }
            updateDate();
        });

        binding.previousDateBtn.setOnClickListener(c ->{
            if (Constants.SELECTED_TAB== Constants.DAILY){
                calendar.add(Calendar.DATE,-1);
            }
            else if (Constants.SELECTED_TAB == Constants.MONTH){
                calendar.add(Calendar.MONTH,-1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c -> {
            new AddTransactionFragment().show(getSupportFragmentManager(),null);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")){
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                }
                else if (tab.getText().equals("Daily")){
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        //  ArrayList<Transaction> transactions = new ArrayList<>();
//        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","some note here!",new Date(),500,2));
//        transactions.add(new Transaction(Constants.EXPENSE,"Investment","Bank","some note here!",new Date(),-30,4));
//        transactions.add(new Transaction(Constants.INCOME,"Food","Cash","some note here!",new Date(),100,5));
//        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","some note here!",new Date(),800,6));

//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(new Transaction(Constants.INCOME,"Business","Cash","some note here!",new Date(),500,new Date().getTime()));
//        realm.commitTransaction();

//        RealmResults<Transaction> transactions = realm.where(Transaction.class).findAll();
        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));

        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionAdapter transactionsAdapter = new TransactionAdapter(MainActivity.this,transactions);
                binding.transactionsList.setAdapter(transactionsAdapter);
                if (transactions.size() >0){
                    binding.emptyState.setVisibility(View.GONE);
                }
                else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomeLbl.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenseLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalLbl.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calendar);

    }

    public void getTransactions() {
        viewModel.getTransactions(calendar);
    }
    void updateDate(){
        if (Constants.SELECTED_TAB ==Constants.DAILY){
            binding.currentDate.setText(Helper.DateFormat(calendar.getTime()));
        }
        else if (Constants.SELECTED_TAB ==Constants.MONTH){
            binding.currentDate.setText(Helper.DateFormatbyMonth(calendar.getTime()));
        }
        //    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy");
        viewModel.getTransactions(calendar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}