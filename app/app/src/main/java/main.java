package com.example.lewandowski.bank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
            Main Class
            Starts the app at the login screen
            Author: Szymon Bialkowski
            Date: 28-10-2017
 */


public class main extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textStyles();
        setClickListeners();
    }


    @Override
    public void onClick(View v)                                                                 //onClick function to open Intents
    {
        switch (v.getId())
        {
            case R.id.home_user_login:
                Intent myIntent = new Intent(this, user_login.class);
                startActivityForResult(myIntent, 1);
                break;
            case R.id.user_register:
                Intent myIntent2 = new Intent(this, user_register.class);
                startActivityForResult(myIntent2, 2);
                break;
            case R.id.employee_login:
                Intent myIntent3 = new Intent(this, employee_login.class);
                startActivityForResult(myIntent3, 3);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK)
            {

                Intent home = new Intent(this, user_home.class);
                String newText = data.getStringExtra("registrationNo");
                home.putExtra("registrationNo", newText);
                startActivity(home);
            }
        }
        else if (requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Intent myIntent = new Intent(this, user_login.class);
                startActivityForResult(myIntent, 1);
            }
        }
        else
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Intent home = new Intent(this, employee_home.class);
                String newText = data.getStringExtra("employeeNo");
                home.putExtra("employeeNo", newText);
                startActivity(home);
            }
        }
    }

    private void setClickListeners()                                                            //Set onClickListeners for onClick function
    {
        findViewById(R.id.home_user_login).setOnClickListener(this);
        findViewById(R.id.user_register).setOnClickListener(this);
        findViewById(R.id.employee_login).setOnClickListener(this);
    }

    private void textStyles()                                                                   //Set fonts for Buttons and TextViews
    {
        // Find Elements
        TextView welcome_to_textview    =(TextView)findViewById(R.id.home_welcome_to);
        TextView sib_textview           =(TextView)findViewById(R.id.home_sib);
        Button user_login_button        =(Button)findViewById(R.id.home_user_login);
        Button user_register_button     =(Button)findViewById(R.id.user_register);
        Button employee_login_button    =(Button)findViewById(R.id.employee_login);

        // Find fonts
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set fonts
        welcome_to_textview.setTypeface(face);
        sib_textview.setTypeface(face);
        user_login_button.setTypeface(face);
        user_register_button.setTypeface(face);
        employee_login_button.setTypeface(face);
    }
}
