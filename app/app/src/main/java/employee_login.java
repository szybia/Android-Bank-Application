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
 * Employee Login Class
 * Allows the employee to login to their account
 * Author: Lewandowski
 * Date: 13-Nov-17
 **/

public class employee_login extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for logging in employee
    {
        EditText employeeNoText         = (EditText)findViewById(R.id.employee_login_number);
        EditText passwordText           = (EditText)findViewById(R.id.employee_login_password);

        String employeeNoString         = employeeNoText.getText().toString();
        String passwordString           = passwordText.getText().toString();


        if (employeeNoString.length() == 0 || passwordString.length() == 0)
        {
            customToast("Please enter in your credentials.");
        }
        else
        {
            try                                                                             //Try turning to int
            {
                int employeeNo              =  Integer.parseInt(employeeNoString);
                try
                {
                    Employee employee = db.selectEmployee(employeeNo);

                    if (!BCrypt.checkpw(passwordString, employee.getPassword()))
                    {
                        customToast("Incorrect login details.");
                    }
                    else
                    {
                        Intent back = new Intent();
                        back.putExtra("employeeNo",Integer.toString(employee.getEmployeeNo()));
                        setResult(Activity.RESULT_OK, back);
                        finish();
                    }
                }
                catch(Exception e)                                                          //Select failed
                {
                    customToast("Incorrect login details.");
                }
            }
            catch (Exception e)
            {
                customToast("Incorrect login details.");
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
    public void onBackPressed()                                                                 //onBack Button pressed, finish Intent
    {
        db.close();
        finish();
    }

    private void textStyles()                                                                   //Set font for TextView
    {
        // Find Element
        TextView sib_textview       = (TextView)findViewById(R.id.home_sib);
        Button login                = (Button)findViewById(R.id.employee_login);
        EditText employeeNo         = (EditText)findViewById(R.id.employee_login_number);
        EditText employeePassword   = (EditText)findViewById(R.id.employee_login_password);

        // Find fonts
        Typeface caviarDreams   =  Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");
        Typeface lato           =  Typeface.createFromAsset(getAssets(),"fonts/Lato-Regular.ttf");

        // Set font
        sib_textview.setTypeface(caviarDreams);
        login.setTypeface(caviarDreams);
        employeeNo.setTypeface(lato);
        employeePassword.setTypeface(lato);
    }

    private void setClickListeners()                                                                //onClick listener for login
    {
        findViewById(R.id.employee_login).setOnClickListener(this);
    }

}
