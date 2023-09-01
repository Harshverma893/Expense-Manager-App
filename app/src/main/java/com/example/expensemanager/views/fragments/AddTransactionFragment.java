package com.example.expensemanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.expensemanager.R;
import com.example.expensemanager.Utills.Constants;
import com.example.expensemanager.Utills.Helper;
import com.example.expensemanager.adapters.AccountsAdapter;
import com.example.expensemanager.adapters.CategoryAdapter;
import com.example.expensemanager.databinding.FragmentAddTransactionBinding;
import com.example.expensemanager.databinding.ListDialogBinding;
import com.example.expensemanager.models.Account;
import com.example.expensemanager.models.Category;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.views.activites.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AddTransactionFragment extends BottomSheetDialogFragment {


    public AddTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddTransactionBinding binding;
    Transaction transaction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentAddTransactionBinding.inflate(inflater);

       transaction = new Transaction();

       binding.incomeBtn.setOnClickListener(view ->{
           binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
           binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
           binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
           binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));

           transaction.setType(Constants.INCOME);
           updateSaveButtonState();
       });

       binding.expenseBtn.setOnClickListener(view ->{
           binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
           binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
           binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
           binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
           transaction.setType(Constants.EXPENSE);
           updateSaveButtonState();
       });


       binding.date.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
               datePickerDialog.setOnDateSetListener((datePicker, year, month, dayOfMonth) -> {
                   Calendar calendar = Calendar.getInstance();
                   calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                   calendar.set(Calendar.MONTH,datePicker.getMonth());
                   calendar.set(Calendar.YEAR,datePicker.getYear());

                  // SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
                   String datetoShow = Helper.DateFormat(calendar.getTime());
                   binding.date.setText(datetoShow);

                   transaction.setDate(calendar.getTime());
                   transaction.setId(calendar.getTime().getTime());
               });

               datePickerDialog.show();
               updateSaveButtonState();
           }
       });

       binding.category.setOnClickListener(c ->{
           ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
           AlertDialog categoryDialog = new AlertDialog.Builder(getContext()).create();
           categoryDialog.setView(dialogBinding.getRoot());


           CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, new CategoryAdapter.CategoryClickListener() {
               @Override
               public void onCategoryClicked(Category category) {
                   binding.category.setText(category.getCategoryName());
                   transaction.setCategory(category.getCategoryName());
                   categoryDialog.dismiss();
               }
           });
           dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
           dialogBinding.recyclerView.setAdapter(categoryAdapter);
           categoryDialog.show();
           updateSaveButtonState();
       });

       binding.account.setOnClickListener(c ->{
           ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
           AlertDialog accountDialog = new AlertDialog.Builder(getContext()).create();
           accountDialog.setView(dialogBinding.getRoot());
           ArrayList<Account> accounts = new ArrayList<>();
           accounts.add(new Account(0,"Cash"));
           accounts.add(new Account(0,"Bank"));
           accounts.add(new Account(0,"Paytm"));
           accounts.add(new Account(0,"EasyPaisa"));
           accounts.add(new Account(0,"Other"));

           AccountsAdapter accountsAdapter = new AccountsAdapter(getContext(), accounts, new AccountsAdapter.AccountsClickListener() {
               @Override
               public void onAccountSelected(Account account) {
                   binding.account.setText(account.getAccountName());
                   transaction.setAccount(account.getAccountName());
                  accountDialog.dismiss();
               }
           });

           dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
           dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
           dialogBinding.recyclerView.setAdapter(accountsAdapter);
           accountDialog.show();
           updateSaveButtonState();
       });
        binding.saveTransactionBtn.setOnClickListener(c-> {
            double amount = Double.parseDouble(binding.amount.getText().toString());
            String note = binding.note.getText().toString();


            if(transaction.getType().equals(Constants.EXPENSE)) {
                transaction.setAmount(amount*-1);
            } else {
                transaction.setAmount(amount);
            }

            transaction.setNote(note);

            ((MainActivity)getActivity()).viewModel.addTransaction(transaction);
            ((MainActivity)getActivity()).getTransactions();
            updateSaveButtonState();
            dismiss();
        });
//binding.incomeBtn.addTextChangedListener(TransactionTextWatcher);
//binding.expenseBtn.addTextChangedListener(TransactionTextWatcher);
//binding.date.addTextChangedListener(TransactionTextWatcher);
//binding.amount.addTextChangedListener(TransactionTextWatcher);
//binding.account.addTextChangedListener(TransactionTextWatcher);
//binding.category.addTextChangedListener(TransactionTextWatcher);
//binding.note.addTextChangedListener(TransactionTextWatcher);
        return binding.getRoot();
    }
    private void updateSaveButtonState() {
        String amount = binding.amount.getText().toString().trim();
        String date = binding.date.getText().toString().trim();
        String note = binding.note.getText().toString().trim();
        String category = binding.category.getText().toString().trim();
        String account = binding.account.getText().toString().trim();
        boolean incomeBtnSelected = binding.incomeBtn.isSelected();
        boolean expenseBtnSelected = binding.expenseBtn.isSelected();

        // Check if either incomeBtn or expenseBtn is selected, and all input fields are not empty
        binding.saveTransactionBtn.setEnabled(!note.isEmpty() && !amount.isEmpty() && !date.isEmpty() && !category.isEmpty() && !account.isEmpty());

//        binding.saveTransactionBtn.setEnabled(isSaveEnabled);
    }
}
