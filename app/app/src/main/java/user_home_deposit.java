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

/**
 * User Deposit Class
 * Allows the user to deposit a sum into their balance, could be used with a ATM
 * Author: Lewandowski
 * Date: 10-Nov-17
 **/

public class user_home_deposit extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_deposit);
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for deposit
    {
        int registrationNo = Integer.parseInt(                                                  //Get registrationNo sent from parent
                getIntent().getStringExtra("registrationNo"));

        final Customer customer         = db.selectCustomer(registrationNo);                  //Get current user

        EditText depositAmountEditText  = (EditText)findViewById(R.id.user_deposit_amount);

        String amountString             = depositAmountEditText.getText().toString();

        if (Double.parseDouble(amountString) == 0.0 || Double.parseDouble(amountString) > 1000000.0)                                            //Check if zero or 0.00....0 or greater than 1M
        {
            customToast("Number too big or too small.");
        }
        else
        {
            if (amountString.length() == 0)
            {
                customToast("Please fill in the field.");
            }
            else
            {
                try                                                                                 //Try turning to int
                {
                    DecimalFormat format = new DecimalFormat("#.00");                       //Double amount to 2 dp
                    final double amount      =  Double.parseDouble(format.format(Double.parseDouble(amountString)));

                    final String balance = Double.toString((customer.getBalance() + amount));

                    db.updateBalance(customer.getRegistrationNo(), customer.getBalance() + amount);
                    db.insertTransaction(customer.getRegistrationNo(), customer.getRegistrationNo(), amount);


                    String title    = "Your deposit has been completed.";
                    String message  = "You have deposited €" + Double.toString(amount);
                    String ok       = "OK";

                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();   //Inform user of login credentials
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
                catch (Exception e)
                {
                    customToast("Please only enter numbers.");
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
        Button transaction              = (Button)findViewById(R.id.user_deposit_submit);

        // Find fonts
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set fonts
        welcome_to_textview.setTypeface(face);
        transaction.setTypeface(face);
    }

    public void setClickListeners()                                                             //Set on click listener
    {
        findViewById(R.id.user_deposit_submit).setOnClickListener(this);
    }




}
