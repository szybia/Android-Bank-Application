package com.example.lewandowski.bank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
            User Home
            Allows the user to perform all actions applicable to a user
            Author: Szymon Bialkowski
            Date: 02-11-2017
 */

public class user_home extends AppCompatActivity implements View.OnClickListener
{

    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        int registrationNo = Integer.parseInt(                                                  //Get RegistrationNo sent from parent activity
                getIntent().getStringExtra("registrationNo"));
        Customer customer = db.selectCustomer(registrationNo);                                  //Select corresponding user to Registration No
        initScreen(customer.getName(), customer.getBalance());
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function to direct user to relevant page
    {
        int registrationNo = Integer.parseInt(                                                  //Get RegistrationNo sent from parent activity
                getIntent().getStringExtra("registrationNo"));
        switch (v.getId())
        {
            case R.id.transaction:
                Intent transaction = new Intent(this, user_home_transaction.class);
                transaction.putExtra("registrationNo", Integer.toString(registrationNo));
                startActivityForResult(transaction, 1);
                break;
            case R.id.deposit:
                Intent deposit = new Intent(this, user_home_deposit.class);
                deposit.putExtra("registrationNo", Integer.toString(registrationNo));
                startActivityForResult(deposit, 1);
                break;
            case R.id.transaction_history:
                Intent transaction_history = new Intent(this, user_home_transaction_history.class);
                transaction_history.putExtra("registrationNo", Integer.toString(registrationNo));
                startActivity(transaction_history);
                break;
            case R.id.changeName:
                Intent changeName = new Intent(this, user_home_change_name.class);
                changeName.putExtra("registrationNo", Integer.toString(registrationNo));
                startActivityForResult(changeName, 2);
                break;
            case R.id.deleteAccount:
                Intent deleteAccount = new Intent(this, user_home_delete_account.class);
                deleteAccount.putExtra("registrationNo", Integer.toString(registrationNo));
                startActivityForResult(deleteAccount, 3);
                break;
            case R.id.logout:
                db.close();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)               //Intent Result
    {
        if (requestCode == 1)                                                                   //Transaction/Deposit
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String newText = data.getStringExtra("balance");                          //If transaction/deposit was made update the balance
                String welcome_balance              = "Balance: €" + newText;

                TextView TextView_welcome_balance   = (TextView)findViewById(R.id.balance);

                TextView_welcome_balance.setText(welcome_balance);
            }
        }
        else if (requestCode == 2)                                                              //Change Name
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String newText = data.getStringExtra("name");                             //If name change was made update the welcoming message
                String welcome_balance              = "Welcome " + newText + ".";

                TextView TextView_welcome_balance   = (TextView)findViewById(R.id.home_sib);

                TextView_welcome_balance.setText(welcome_balance);
            }
        }
        else if (requestCode == 3)                                                              //Delete account
        {
            if(resultCode == Activity.RESULT_OK)
            {
                customToast("Your account was successfully deleted.");
                db.close();
                finish();
            }
        }
    }

    private void customToast(String text)                                                       //Defines a custom toast
    {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.customtoast);
        view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        view.setPadding(50, 20, 50, 20);
        TextView toastText      =  view.getRootView().findViewById(android.R.id.message);           //Find Toast TextView
        Typeface lato           =  Typeface.createFromAsset(getAssets(),                            //Get font
                "fonts/CaviarDreams.ttf");
        toastText.setTypeface(lato);                                                                //Set Toast TextView font
        toast.show();
    }

    @Override
    public void onBackPressed()                                                                 //Ignore Back Button
    {}

    public void initScreen(String name, double balance)                                         //Initialise welcoming header
    {
        String welcome_name                 = "Welcome " + name + ".";
        String welcome_balance              = "Balance: €" + Double.toString(balance);
        TextView TextView_welcome_text      = (TextView)findViewById(R.id.home_sib);
        TextView TextView_welcome_balance   = (TextView)findViewById(R.id.balance);

        TextView_welcome_text.setText(welcome_name);
        TextView_welcome_balance.setText(welcome_balance);
    }

    private void textStyles()                                                                   //Set font for Buttons and TextViews
    {
        // Find Element
        TextView sib_textview       =(TextView)findViewById(R.id.home_sib);
        TextView balance            =(TextView)findViewById(R.id.balance);
        Button transaction          =(Button)findViewById(R.id.transaction);
        Button deposit              =(Button)findViewById(R.id.deposit);
        Button transactionHistory   =(Button)findViewById(R.id.transaction_history);
        Button changeName           =(Button)findViewById(R.id.changeName);
        Button deleteAccount        =(Button)findViewById(R.id.deleteAccount);
        Button logout               =(Button)findViewById(R.id.logout);

        // Find font
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set font
        sib_textview.setTypeface(face);
        balance.setTypeface(face);
        balance.setTextSize(17);

        transaction.setTypeface(face);
        deposit.setTypeface(face);
        transactionHistory.setTypeface(face);
        changeName.setTypeface(face);
        deleteAccount.setTypeface(face);
        logout.setTypeface(face);
    }

    private void setClickListeners()                                                            //onClick listeners
    {
        findViewById(R.id.transaction).setOnClickListener(this);
        findViewById(R.id.deposit).setOnClickListener(this);
        findViewById(R.id.transaction_history).setOnClickListener(this);
        findViewById(R.id.changeName).setOnClickListener(this);
        findViewById(R.id.deleteAccount).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
    }
}
