package com.example.lewandowski.bank;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;

/**
            User Register Class
            Allows the user to register
            Author: Szymon Bialkowski
            Date: 28-10-2017
 */

public class user_register extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function to register user
    {
        EditText nameText = (EditText)findViewById(R.id.user_register_name);
        String name = nameText.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            customToast("Please enter your name.");
        }
        else
        {
            if (!name.matches("[a-zA-Z\\s.]*"))
            {
                customToast("Please only enter letters.");

            }
            else
            {
                if (name.length() > 25)
                {
                    customToast("This name is too long.");
                }
                else
                {
                    SecureRandom random = new SecureRandom();
                    String PACString    = Integer.toString(random.nextInt());                       //Generate cryptographically random number
                    PACString           = PACString.substring(PACString.length() - 4);
                    int PAC             = Integer.parseInt(PACString);

                    int registrationNo = db.insertCustomer(PAC, name, 0);

                    String title    = "Your Login Credentials";
                    String message  = "RegisterNo: " + registrationNo + "\n" +
                                      "PAC: " + PACString + ".\n\nPlease note down your login information.";
                    String login       = "Login";


                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();       //Inform user of login credentials
                    alertDialog.setTitle(title);
                    alertDialog.setMessage(message);


                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, login,
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                    Intent back = new Intent();
                                    setResult(Activity.RESULT_OK, back);
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
        TextView toastText      =  view.getRootView().findViewById(android.R.id.message);       //Find Toast TextView
        Typeface lato           =  Typeface.createFromAsset(getAssets(),                        //Get font
                "fonts/CaviarDreams.ttf");
        toastText.setTypeface(lato);                                                            //Set Toast TextView font
        toast.show();
    }

    @Override
    public void onBackPressed()                                                                 //onBack Button pressed, finish Intent
    {
        db.close();
        finish();
    }

    private void setClickListeners()                                                            //onClick listener for register button
    {
        findViewById(R.id.customer_register).setOnClickListener(this);
    }

    private void textStyles()                                                                   //Set font on TextView
    {
        // Find Element
        TextView sib_textview   = (TextView)findViewById(R.id.home_sib);
        Button login            = (Button)findViewById(R.id.customer_register);
        EditText name           = (EditText)findViewById(R.id.user_register_name);

        // Find fonts
        Typeface caviarDreams   =  Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");
        Typeface lato           =  Typeface.createFromAsset(getAssets(),"fonts/Lato-Regular.ttf");

        // Set font
        sib_textview.setTypeface(caviarDreams);
        login.setTypeface(caviarDreams);
        name.setTypeface(lato);
    }
}

