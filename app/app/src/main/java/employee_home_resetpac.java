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

import java.security.SecureRandom;

/**
 * Employee Reset PAC Class
 * Allows the employee to reset a customers if they are not able to access their account
 * Author: Lewandowski
 * Date: 17-Nov-17
 **/

public class employee_home_resetpac extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_resetpac);
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for deleting
    {

        try
        {
            EditText resetpacRegNo  = (EditText)findViewById(R.id.employee_reset_pac_regNo);
            int registrationNo      = Integer.parseInt(resetpacRegNo.getText().toString());

            Customer resetpacUser     = db.selectCustomer(registrationNo);                      //Check if customer exists

            SecureRandom random = new SecureRandom();
            String PACString    = Integer.toString(random.nextInt());
            PACString           = PACString.substring(PACString.length() - 4);
            int PAC             = Integer.parseInt(PACString);

            db.updatePAC(registrationNo, PAC);



            String title    = "New User PAC";
            String message  = PACString + ".";
            String ok       = "OK";


            AlertDialog alertDialog = new AlertDialog.Builder(this).create();           //Inform employee of new PAC for customer
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, ok,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
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
        catch(Exception e)
        {
            customToast("Resetting the PAC has failed.");
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
        Button deleteAccountButton      = (Button)findViewById(R.id.employee_reset_pac_button);

        // Find fonts
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set fonts
        welcome_to_textview.setTypeface(face);
        deleteAccountButton.setTypeface(face);
    }

    public void setClickListeners()                                                             //Set on click listener
    {
        findViewById(R.id.employee_reset_pac_button).setOnClickListener(this);
    }
}
