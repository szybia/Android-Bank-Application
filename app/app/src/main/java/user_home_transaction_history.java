package com.example.lewandowski.bank;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Transaction History Class
 * Lists users transactions
 * Author: Lewandowski
 * Date: 10-Nov-17
 **/

public class user_home_transaction_history extends AppCompatActivity
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_transaction_history);
        createListView();
        textStyles();
    }

    private void createListView()
    {
        final ListView listView = (ListView) findViewById(R.id.list);                           //Create ListView

        int registrationNo = Integer.parseInt(                                                  //Get registrationNo sent from parent
                getIntent().getStringExtra("registrationNo"));

        final Customer user = db.selectCustomer(registrationNo);                                //Get currrent user

        final List<Transaction> transactionList = db.allTransactions(registrationNo);           //Get all transactions from Database

        transactionList.add(0, new Transaction());

        if (transactionList.size() == 0)                                                        //If no transactions
        {
            ListAdapter customerAdapter = new userTransactionAdapter(this,
                    R.layout.customerlistrow, transactionList);

            listView.setAdapter(customerAdapter);
        }
        else
        {
            try
            {

                ListAdapter customerAdapter = new userTransactionAdapter(this, R.layout.customertransactionrow, transactionList);

                listView.setAdapter(customerAdapter);
            }
            catch (Exception e)
            {
                customToast(e.toString());
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,                       //onTransactionClick AlertDialog with info
                                        int position, long id)
                {
                    try
                    {
                        if (position != 0)
                        {
                            String popupText;
                            Date date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.ENGLISH).parse(transactionList.get(position).getDate());


                            popupText = "FROM: \t" + user.getName() + "\nTO: \t\t\t" +
                                    db.selectCustomer(transactionList.get(position).getReceiver()).getName() +
                                "\nAmount: \t€" + Double.toString(transactionList.get(position).getAmount()) + "\nDate: \t\t" +
                                date.toString();

                            String title    = "Transaction";
                            String ok       = "OK";

                            AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();   //Inform user of login credentials
                            alertDialog.setTitle(title);
                            alertDialog.setMessage(popupText);
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, ok,
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#1e7888"));

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
                    catch(Exception e)
                    {
                        customToast("Unable to load Transaction details.");
                    }
                }
            });
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

        // Find fonts
        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/CaviarDreams.ttf");

        // Set fonts
        welcome_to_textview.setTypeface(face);
    }
}
