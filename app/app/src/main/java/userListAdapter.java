package com.example.lewandowski.bank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * User List Adapter
 * Custom adapter for generating customer list for employee
 * Author: Lewandowski
 * Date: 19-Nov-17
 **/

public class userListAdapter extends ArrayAdapter<Customer>
{

    protected userListAdapter(Context context, int resource, List<Customer> customers)
    {
        super(context, resource, customers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)                       //Creates a ListView item
    {
        View v = convertView;

        if (v == null)
        {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.customerlistrow, null);
        }

        Customer customer = getItem(position);

        if (customer != null)
        {
            if (position != 0)                                                                  //Normal user
            {
                TextView tt1 = v.findViewById(R.id.userListRegistrationNo);
                TextView tt2 = v.findViewById(R.id.userListPAC);
                TextView tt3 = v.findViewById(R.id.userListName);
                TextView tt4 = v.findViewById(R.id.userListBalance);

                if (tt1 != null)
                {
                    String regNo = Integer.toString(customer.getRegistrationNo());
                    tt1.setText(regNo);
                }

                if (tt2 != null)
                {
                    String PAC = Integer.toString(customer.getPAC());
                    if (PAC.length() == 3)
                    {
                        PAC = "0" + PAC;
                        tt2.setText(PAC);
                    }
                    else
                    {
                        tt2.setText(PAC);
                    }
                }

                if (tt3 != null)
                {
                    tt3.setText(customer.getName());
                }

                if (tt4 != null)
                {
                    String balance = "€" + Double.toString(customer.getBalance());
                    tt4.setText(balance);
                }
            }
            else                                                                                //First entry enter column identifiers
            {
                TextView tt1 = v.findViewById(R.id.userListRegistrationNo);
                TextView tt2 = v.findViewById(R.id.userListPAC);
                TextView tt3 = v.findViewById(R.id.userListName);
                TextView tt4 = v.findViewById(R.id.userListBalance);

                String regNo    = "regNo";
                String PAC      = "PAC";
                String Name     = "Name";
                String Balance  = "Balance";

                tt1.setText(regNo);
                tt2.setText(PAC);
                tt3.setText(Name);
                tt4.setText(Balance);
            }

        }

        return v;
    }


}
