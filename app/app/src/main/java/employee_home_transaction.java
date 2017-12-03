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
 * Employee Transaction Class
 * Allows the employee to make a transaction from a users account to another users account
 * Author: Lewandowski
 * Date: 14-Nov-17
 **/

public class employee_home_transaction extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_transaction);
        textStyles();                                                                           //Set fonts
        setClickListeners();                                                                    //Set onClickListeners
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for logging in user
    {
        try
        {
            EditText receiverEditText   = (EditText)findViewById(R.id.user_transaction_receiver);
            EditText senderEditText     = (EditText)findViewById(R.id.user_transaction_sender);
            EditText amountEditText     = (EditText)findViewById(R.id.user_transaction_amount);

            String receiverString       = receiverEditText.getText().toString();
            String senderString         = senderEditText.getText().toString();
            String amountString         = amountEditText.getText().toString();

            int senderNo                = Integer.parseInt(senderString);
            int receiverNo              = Integer.parseInt(receiverString);
            double amount               = Double.parseDouble(amountString);

            Customer sender     = db.selectCustomer(senderNo);
            Customer receiver   = db.selectCustomer(receiverNo);

            db.updateBalance(receiverNo, receiver.getBalance() + amount);               //Add amount to receiver balance
            db.updateBalance(senderNo,                                                          //Take away amount from sender balance
                    sender.getBalance() - amount);
            db.insertTransaction(senderNo, receiverNo, amount);


            String title    = "Your transaction has been completed.";
            String message  = "Receiver: " + receiver.getName() +
                              "\n" + "Amount: " + amountString;
            String ok       = "OK";

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();       //Inform user of login credentials
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, ok,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
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
            customToast("This transaction has failed.");
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
