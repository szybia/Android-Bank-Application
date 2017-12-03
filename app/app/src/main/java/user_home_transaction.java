package com.example.lewandowski.bank;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
            Transaction Class
            Allows the logged in user to make a transaction
            Author: Szymon Bialkowski
            Date: 06-11-2017
 **/


public class user_home_transaction extends AppCompatActivity implements View.OnClickListener
{

    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_transaction);
        textStyles();                                                                           //Set fonts
        setClickListeners();                                                                    //Set onClickListeners
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for logging in user
    {
        int registrationNo = Integer.parseInt(                                                  //Get registrationNo sent from parent
                getIntent().getStringExtra("registrationNo"));

        final Customer customer           = db.selectCustomer(registrationNo);                  //Get current user

        EditText receiverEditText   = (EditText)findViewById(R.id.user_transaction_receiver);
        EditText amountEditText     = (EditText)findViewById(R.id.user_transaction_amount);

        String receiverString       = receiverEditText.getText().toString();
        String amountString         = amountEditText.getText().toString();


        if (receiverString.length() == 0 || amountString.length() == 0)
        {
            customToast("Please fill the fields.");
        }
        else
        {
            if (Double.parseDouble(amountString) == 0.0)                                            //Check if zero or 0.00....0
            {
                customToast("Please enter a number greater than 0");
            }
            else
            {
                if(Integer.parseInt(receiverString) == registrationNo)
                {
                    customToast("You cannot transfer money to yourself.");
                }
                else
                {
                    try                                                                                 //Try turning to int
                    {
                        DecimalFormat format = new DecimalFormat("#.00");                       //Double amount to 2 dp
                        final int receiverNo     =  Integer.parseInt(receiverString);
                        final double amount      =  Double.parseDouble(format.format(Double.parseDouble(amountString)));
                        try
                        {
                            Customer receiver = db.selectCustomer(receiverNo);

                            if (customer.getBalance() < amount || receiverNo == 0)
                            {
                                customToast("You do not have sufficient funds in your account.");
                            }
                            else
                            {
                                final String balance = Double.toString((customer.getBalance() - amount));   //Balance after transaction

                                db.updateBalance(receiverNo, receiver.getBalance() + amount);       //Add amount to receiver balance
                                db.updateBalance(customer.getRegistrationNo(),                              //Take away amount from sender balance
                                        customer.getBalance() - amount);
                                db.insertTransaction(customer.getRegistrationNo(), receiverNo, amount);



                                String title    = "Transaction Completed.";
                                String message  = "Receiver: " + receiver.getName() +
                                        "\n" + "Amount: " + Double.toString(amount);
                                String ok       = "OK";

                                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                                alertDialog.setTitle(title);
                                alertDialog.setMessage(message);
                                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, ok,
                                        new DialogInterface.OnClickListener()
                                        {
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                                Intent back = new Intent();
                                                back.putExtra("balance" , balance);
                                                setResult(Activity.RESULT_OK, back);
                                                finish();
                                            }
                                        });
                                alertDialog.show();
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#1e7888"));

                                try                                                                         //If exception happens alertDialog will still work
                                {
                                    TextView messageTextView    = (TextView) alertDialog.findViewById(android.R.id.message);
                                    Typeface lato               =  Typeface.createFromAsset(getAssets(),"fonts/Lato-Regular.ttf");
                                    messageTextView.setTypeface(lato);

                                    TextView titleTextView = (TextView) alertDialog.findViewById(R.id.alertTitle);
                                    titleTextView.setTypeface(lato);
                                }
                                catch (NullPointerException e)
                                {}
                            }
                        }
                        catch(Exception e)                                                              //Select failed
                        {
                            customToast("This user does not exist.");
                        }
                    }
                    catch (Exception e)
                    {
                        customToast("Please only enter numbers.");
                    }
                }
            }
        }
    }

    private void customToast(String text)
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
    public void onBackPressed()
    {
        db.close();
        finish();
    }

    private void textStyles()                                                                   //Set fonts for Buttons and TextViews
    {
        // Find Elements
        TextView welcome_to_textview    = (TextView)findViewById(R.id.home_sib);
        Button transaction              = (Button)findViewById(R.id.user_transaction_submit);

        // Find fonts
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set fonts
        welcome_to_textview.setTypeface(face);
        transaction.setTypeface(face);
    }

    public void setClickListeners()                                                             //Set on click listener
    {
        findViewById(R.id.user_transaction_submit).setOnClickListener(this);
    }
}
