package com.example.lewandowski.bank;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

/**
 * Employee User List Class
 * Allows the employee to see all users and delete them
 * Author: Lewandowski
 * Date: 15-Nov-17
 **/

public class employee_home_user_list extends AppCompatActivity
{
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home_user_list);
        createListView();
        textStyles();
    }

    private void createListView()
    {
        final ListView listView = (ListView) findViewById(R.id.list);                           //Create ListView

        final List<Customer> userList = db.allUsers();

        if (userList.size() == 1)                                                               //If no customers
        {
            ListAdapter customerAdapter = new userListAdapter(this,
                    R.layout.customerlistrow, userList);

            listView.setAdapter(customerAdapter);
        }
        else
        {
            try
            {
                ListAdapter customerAdapter = new userListAdapter(this, R.layout.customerlistrow, userList);

                listView.setAdapter(customerAdapter);

            }
            catch (Exception e)
            {
                customToast(e.toString());
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,                       //onTransactionClick AlertDialog with info
                                    int position, long id)
            {
                if (position != 0)
                {
                    try
                    {

                        final String registrationNo   = Integer.toString(userList.get(position).getRegistrationNo());
                        String PAC                    = Integer.toString(userList.get(position).getPAC());
                        String Name                   = userList.get(position).getName();
                        String Balance                = Double.toString(userList.get(position).getBalance());



                        String title    = "Customer";
                        String message  = "regNo: \t\t\t" + registrationNo +
                                            "\nPAC: \t\t\t\t" + PAC +
                                            "\nName: \t\t\t" + Name +
                                            "\nBalance: \t\t€" + Balance + ".";
                        String cancel       = "Cancel";
                        String deleteUser       = "Delete User";


                        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).create();   //Inform user of login credentials
                        alertDialog.setTitle(title);
                        alertDialog.setMessage(message);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, cancel,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {}
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, deleteUser,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        db.deleteUser(Integer.parseInt(registrationNo));
                                        employee_home_user_list.this.createListView();
                                        customToast("User deleted.");
                                    }
                                });
                        alertDialog.show();
                        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#1e7888"));
                        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#C91F37"));

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
                        customToast("Unable to load user details.");
                    }
                }
            }
        });

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
