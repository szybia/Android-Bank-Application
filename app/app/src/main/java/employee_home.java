package com.example.lewandowski.bank;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Employee Home Class
 * Command center for the employee
 * Author: Lewandowski
 * Date: 14-Nov-17
 **/

public class employee_home extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        int employeeNo = Integer.parseInt(                                                  //Get RegistrationNo sent from parent activity
                getIntent().getStringExtra("employeeNo"));
        Employee employee = db.selectEmployee(employeeNo);                                  //Select corresponding user to Registration No
        initScreen(employee.getName());
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function to direct user to relevant page
    {

        switch (v.getId())
        {
            case R.id.transaction:
                Intent transaction = new Intent(this, employee_home_transaction.class);
                startActivity(transaction);
                break;
            case R.id.deposit:
                Intent deposit = new Intent(this, employee_home_deposit.class);
                startActivity(deposit);
                break;
            case R.id.user_list:
                Intent transaction_history = new Intent(this, employee_home_user_list.class);
                startActivity(transaction_history);
                break;
            case R.id.deleteAccount:
                Intent deleteAccount = new Intent(this, employee_home_delete.class);
                startActivity(deleteAccount);
                break;
            case R.id.resetPAC:
                Intent resetPAC = new Intent(this, employee_home_resetpac.class);
                startActivity(resetPAC);
                break;
            case R.id.logout:
                db.close();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed()                                                                 //Ignore Back Button
    {}

    public void initScreen(String name)                                                         //Initialise welcoming header
    {
        String welcome_name                 = "Welcome " + name + ".";
        TextView TextView_welcome_text      = (TextView)findViewById(R.id.home_sib);

        TextView_welcome_text.setText(welcome_name);
    }

    private void textStyles()                                                                   //Set font for Buttons and TextViews
    {
        // Find Element
        TextView sib_textview       =(TextView)findViewById(R.id.home_sib);
        Button transaction          =(Button)findViewById(R.id.transaction);
        Button deposit              =(Button)findViewById(R.id.deposit);
        Button transactionHistory   =(Button)findViewById(R.id.user_list);
        Button changeName           =(Button)findViewById(R.id.resetPAC);
        Button deleteAccount        =(Button)findViewById(R.id.deleteAccount);
        Button logout               =(Button)findViewById(R.id.logout);

        // Find font
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set font
        sib_textview.setTypeface(face);
        transaction.setTypeface(face);
        deposit.setTypeface(face);
        transactionHistory.setTypeface(face);
        changeName.setTypeface(face);
        deleteAccount.setTypeface(face);
        logout.setTypeface(face);
    }

    private void setClickListeners()                                                            //onClick listeners
    {
        findViewById(R.id.transaction).setOnClickListener(this);
        findViewById(R.id.deposit).setOnClickListener(this);
        findViewById(R.id.user_list).setOnClickListener(this);
        findViewById(R.id.resetPAC).setOnClickListener(this);
        findViewById(R.id.deleteAccount).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);
    }

}
