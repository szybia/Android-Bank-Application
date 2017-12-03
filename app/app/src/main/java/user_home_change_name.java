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

import java.util.Random;

/**
 * Change Name Screen
 * Allows customer to change name
 * Author: Lewandowski
 * Date: 11-Nov-17
 **/

public class user_home_change_name extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_change_name);
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for logging in user
    {
        EditText nameText = (EditText)findViewById(R.id.user_change_name);
        String name = nameText.getText().toString();

        if (name.length() < 1 || name.length() > 30)
        {
            customToast("No name or length is too long.");
        }
        else
        {
            if (!name.matches("[a-zA-Z\\s.]*"))
            {
                customToast("Please only enter letters.");
            }
            else
            {
                int registrationNo = Integer.parseInt(                                                  //Get registrationNo sent from parent
                        getIntent().getStringExtra("registrationNo"));

                db.updateName(registrationNo, name);

                Intent back = new Intent();
                back.putExtra("name" , name);
                setResult(Activity.RESULT_OK, back);
                db.close();
                finish();
            }
        }
    }

    private void customToast(String text)
    {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        View view   = toast.getView();
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
        Button changeNameButton         = (Button)findViewById(R.id.customer_change_name_button);

        // Find fonts
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set fonts
        welcome_to_textview.setTypeface(face);
        changeNameButton.setTypeface(face);
    }

    public void setClickListeners()                                                             //Set on click listener
    {
        findViewById(R.id.customer_change_name_button).setOnClickListener(this);
    }

}
