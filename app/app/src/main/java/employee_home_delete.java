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
 * Employee Delete Class
 * Allows the employee to delete a user depending on the registrationNo
 * Author: Lewandowski
 * Date: 17-Nov-17
 **/

public class employee_home_delete extends AppCompatActivity implements View.OnClickListener
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_delete);
        textStyles();
        setClickListeners();
    }

    @Override
    public void onClick(View v)                                                                 //onClick function for deleting
    {
        try
        {
            final EditText deleteRegNo   = (EditText)findViewById(R.id.employee_delete_account_regNo);

            int registrationNo     = Integer.parseInt(deleteRegNo.getText().toString());

            Customer deleteUser    = db.selectCustomer(registrationNo);

            db.deleteUser(registrationNo);

            String title        = "Customer Deleted";
            String message      = "You have succesfully deleted " + deleteUser.getName() + ".";
            String ok       = "OK";


            AlertDialog alertDialog = new AlertDialog.Builder(this).create();           //Inform employee of deletion
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, ok,
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            deleteRegNo.setText("");
                            deleteRegNo.clearFocus();
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
            customToast("Unable to delete user.");
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
        Button deleteAccountButton      = (Button)findViewById(R.id.employee_delete_account_button);

        // Find fonts
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set fonts
        welcome_to_textview.setTypeface(face);
        deleteAccountButton.setTypeface(face);
    }

    public void setClickListeners()                                                             //Set on click listener
    {
        findViewById(R.id.employee_delete_account_button).setOnClickListener(this);
    }

}
