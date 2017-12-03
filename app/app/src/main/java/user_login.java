package com.example.lewandowski.bank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * User Login Class
 * Allows the user to login to their account
 * Author: Lewandowski
 * Date: 10-Nov-17
 **/

public class user_login extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for logging in user
    {
        EditText registrationEditText   = (EditText)findViewById(R.id.user_login_registrationNo);
        EditText PACEditText            = (EditText)findViewById(R.id.user_login_PAC);

        String registrationString       = registrationEditText.getText().toString();
        String PACString                = PACEditText.getText().toString();

        if (registrationString.length() == 0 || PACString.length() == 0)
        {
            customToast("Please enter in your credentials.");
        }
        else
        {
            try                                                                                  //Try turning to int
            {
                int registrationNo  =  Integer.parseInt(registrationString);
                int PAC             =  Integer.parseInt(PACString);
                try
                {
                    Customer customer = db.selectCustomer(registrationNo);

                    if (customer.getPAC() != PAC || registrationNo == 0)
                    {

                        customToast("Incorrect Registration Number or PAC.");
                    }
                    else
                    {
                        Intent back = new Intent();
                        back.putExtra("registrationNo",Integer.toString(customer.getRegistrationNo()));
                        setResult(Activity.RESULT_OK, back);
                        finish();
                    }
                }
                catch(Exception e)                                                          //Select failed
                {
                    customToast("Incorrect Registration Number or PAC.");
                }
            }
            catch (Exception e)
            {
                customToast("Please only enter your Registration Number");
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

    private void setClickListeners()                                                            //onClick listener for login
    {
        findViewById(R.id.customer_login).setOnClickListener(this);
    }

    @Override
    public void onBackPressed()                                                                 //onBack Button pressed, finish Intent
    {
        db.close();
        finish();
    }

    private void textStyles()                                                                   //Set font for TextView
    {
        // Find Elements
        TextView sib_textview   = (TextView)findViewById(R.id.home_sib);
        Button login            = (Button)findViewById(R.id.customer_login);
        EditText registrationNo = (EditText)findViewById(R.id.user_login_registrationNo);
        EditText PAC            = (EditText)findViewById(R.id.user_login_PAC);

        // Find fonts
        Typeface caviarDreams   =  Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");
        Typeface lato           =  Typeface.createFromAsset(getAssets(),"fonts/Lato-Regular.ttf");

        // Set fonts
        sib_textview.setTypeface(caviarDreams);
        login.setTypeface(caviarDreams);
        registrationNo.setTypeface(lato);
        PAC.setTypeface(lato);
    }
}
