package com.example.lewandowski.bank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * User Transaction Adapter
 * Custom adapter for generating customers transactions
 * Author: Lewandowski
 * Date: 19-Nov-17
 **/

public class userTransactionAdapter extends ArrayAdapter<Transaction>
{

    protected userTransactionAdapter(Context context, int resource, List<Transaction> transactions)
    {
        super(context, resource, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)                       //Creates a ListView item
    {
        View v = convertView;

        if (v == null)
        {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.customertransactionrow, parent, false);
        }

        Transaction transaction = getItem(position);

        if (transaction != null)
        {
            if (position != 0)                                                                  //Normal user
            {
                TextView tt1 = v.findViewById(R.id.transactionListSender);
                TextView tt2 = v.findViewById(R.id.transactionListReceiver);
                TextView tt3 = v.findViewById(R.id.transactionListAmount);
                boolean deposit = false;

                if (tt1 != null)
                {
                    if (transaction.getSender() == transaction.getReceiver())
                    {
                        String sender = "Deposit";
                        tt1.setText(sender);
                        deposit = true;
                    }
                    else if(transaction.getSender() == 0)
                    {
                        String sender = "SIB";
                        tt1.setText(sender);
                    }
                    else
                    {
                        String sender = Integer.toString(transaction.getSender());
                        tt1.setText(sender);
                    }
                }

                if (tt2 != null)
                {
                    if (deposit)
                    {
                        tt2.setText("");
                    }
                    else
                    {
                        String receiver = Integer.toString(transaction.getReceiver());
                        tt2.setText(receiver);
                    }
                }

                if (tt3 != null)
                {
                    String amount = "€" + Double.toString(transaction.getAmount());
                    tt3.setText(amount);
                }
            }
            else                                                                                //First entry enter column identifiers
            {
                TextView tt1 = v.findViewById(R.id.transactionListSender);
                TextView tt2 = v.findViewById(R.id.transactionListReceiver);
                TextView tt3 = v.findViewById(R.id.transactionListAmount);

                String Sender       = "From";
                String Receiver     = "To";
                String Amount       = "Amount";

                tt1.setText(Sender);
                tt2.setText(Receiver);
                tt3.setText(Amount);
            }
        }

        return v;
    }


}
