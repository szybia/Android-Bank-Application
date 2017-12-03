package com.example.lewandowski.bank;

import android.content.DialogInterface;
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

/**
 * Deposit Class
 * Allows the employee to deposit an amount to auser
 * Author: Lewandowski
 * Date: 15-Nov-17
 **/

public class employee_home_deposit extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_deposit);
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for deposit
    {
        EditText receiverEditText   = (EditText)findViewById(R.id.user_deposit_registrationNo);
        EditText amountEditText     = (EditText)findViewById(R.id.user_deposit_amount);

        int registrationNo          = Integer.parseInt(receiverEditText.getText().toString());
        double amount               = Double.parseDouble(amountEditText.getText().toString());



        try
        {
            Customer customer   = db.selectCustomer(registrationNo);
            Customer SIB        = db.selectCustomer(0);

            db.updateBalance(registrationNo, customer.getBalance() + amount);
            db.updateBalance(0, SIB.getBalance() - amount);
            db.insertTransaction(0, registrationNo, amount);

            String title    = "Your deposit has been completed.";
            String message  = "You have deposited €" + Double.toString(amount);
            String ok    = "OK";

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();           //Inform user of login credentials
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, ok,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            db.close();
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
            customToast(e.toString());
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
