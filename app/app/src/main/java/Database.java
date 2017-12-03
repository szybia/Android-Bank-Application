package com.example.lewandowski.bank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
            Database Class
            Connects to the database and performs all CRUD operations
            Author: Szymon Bialkowski
            Date: 02-11-2017
 */

public class Database extends SQLiteOpenHelper
{
    public static int registrationNo            = 1;                                             //Keep track of registrationNo for register screen
    private static final String DATABASE_NAME   = "SIB.db";
    private static final String[] TABLE_NAMES = {"`customer`", "`transaction`", "`employee`"};
    private static final String[][] TABLE_COLUMNS = {
            {"`registrationNo`", "`PAC`", "`name`", "`balance`"},
            {"`sender`", "`receiver`", "`amount`", "`transaction_date`"},
            {"`employeeNo`", "`password`", "`name`"}};


    public Database(Context context)                                                            //Create database constructor
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)                                                     //Creates database tables
    {
        String CREATE = "CREATE TABLE `customer`( `registrationNo` INTEGER PRIMARY KEY " +
                "AUTOINCREMENT, `PAC` INTEGER, `name` TEXT, `balance` REAL )";

        db.execSQL(CREATE);

        CREATE = "CREATE TABLE `transaction` ( `sender` INTEGER, `receiver` INTEGER, " +
                "`amount` REAL, `transaction_date` TEXT, PRIMARY KEY(`sender`,`receiver`, `transaction_date`) )";

        db.execSQL(CREATE);

        CREATE = "CREATE TABLE `employee` ( `employeeNo` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT" +
                ", `password` TEXT NOT NULL, `name` TEXT )";

        db.execSQL(CREATE);

        String INSERT = "INSERT INTO `employee` VALUES (1, '$2a$10$/FqCIMv34alSfBmrDG" +        //Insert admin and hashed password into employee database
                "dQqu2BDHdj4cHfDDGplu//dz9K1Nu8OhQ8C', 'admin');";

        db.execSQL(INSERT);

        INSERT = "INSERT INTO `customer` VALUES (0, 20627224, 'SIB', 1000000.0);";

        db.execSQL(INSERT);
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)                    //onUpgrade drop all tables and create
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES[0]);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES[1]);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES[2]);

        // Create tables again
        onCreate(db);
    }

    public int insertCustomer(int PAC, String name, double balance)                             //Inserts customer into customer table
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (this.registrationNo == 1)
        {
            String query = "SELECT max(registrationNo) as max FROM " + TABLE_NAMES[0];
            Cursor result = db.rawQuery(query, null);
            if (result.moveToFirst())
            {
                this.registrationNo = result.getInt(result.getColumnIndex("max"));
            }
            else
            {
                this.registrationNo = 1;
            }
        }

        ContentValues values = new ContentValues();
        values.put("PAC", PAC);
        values.put("name", name);
        values.put("balance", balance);
        db.insert(TABLE_NAMES[0], null, values);

        return ++registrationNo;
    }

    public List<Customer> allUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Customer> userList = new ArrayList<Customer>();

        String selectAllUsers = "SELECT * FROM customer";

        Cursor cursor = db.rawQuery(selectAllUsers, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Customer customer = new Customer();

                customer.setRegistrationNo(cursor.getInt(0));
                customer.setPAC(cursor.getInt(1));
                customer.setName(cursor.getString(2));
                customer.setBalance(cursor.getDouble(3));

                userList.add(customer);
            }
            while(cursor.moveToNext());
        }

        return userList;
    }

    public Customer selectCustomer(int registrationNo)                                          //Select user where registrationNo = db registrationNo
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.query(
                TABLE_NAMES[0],
                new String[] {
                TABLE_COLUMNS[0][0], TABLE_COLUMNS[0][1], TABLE_COLUMNS[0][2], TABLE_COLUMNS[0][3]},
                TABLE_COLUMNS[0][0] + " = ?", new String[] {String.valueOf(registrationNo)},
                null, null, null, null);

        if (result != null)
        {
            result.moveToFirst();

            Customer customer = new Customer(Integer.parseInt(result.getString(0)),
                    Integer.parseInt(result.getString(1)),
                    result.getString(2),
                    Double.parseDouble(result.getString(3)));

            return customer;
        }

        return null;
    }

    public void updateBalance(int registrationNo, double balance)                               //Update balance of selected customer
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_COLUMNS[0][3], balance);

        db.update(TABLE_NAMES[0], values, TABLE_COLUMNS[0][0] + " = ?",
                new String[] { String.valueOf(registrationNo)});
    }

    public void updateName(int registrationNo, String name)                                     //Update Name of selected user
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_COLUMNS[0][2], name);

        db.update(TABLE_NAMES[0], values, TABLE_COLUMNS[0][0] + " = ?",
                new String[] { String.valueOf(registrationNo)});
    }

    public void updatePAC(int registrationNo, int PAC)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TABLE_COLUMNS[0][1], PAC);

        db.update(TABLE_NAMES[0], values, TABLE_COLUMNS[0][0] + " = ?",
                new String[] { String.valueOf(registrationNo)});
    }

    public void deleteUser(int registrationNo)                                                  //Delete a user along with his transactions
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (this.registrationNo == 0)
        {
            String query = "SELECT max(registrationNo) as max FROM " + TABLE_NAMES[0];
            Cursor result = db.rawQuery(query, null);
            if (result.moveToFirst())
            {
                this.registrationNo = result.getInt(result.getColumnIndex("max"));
            }
            else
            {
                this.registrationNo = 0;
            }
        }

        db.delete(TABLE_NAMES[0], TABLE_COLUMNS[0][0] + " = ?", new String[] {String.valueOf(registrationNo)});

        db.delete(TABLE_NAMES[1], TABLE_COLUMNS[1][0] + " = ? OR " + TABLE_COLUMNS[1][1] + " = ?", new String[] {String.valueOf(registrationNo), String.valueOf(registrationNo)});

        this.registrationNo--;
    }

    public void insertTransaction(int sender, int receiver, double amount)                      //Insert a single transaction
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss-SSS").format(new Date());

        ContentValues values = new ContentValues();

        values.put("`sender`", sender);
        values.put("`receiver`", receiver);
        values.put("`amount`", amount);
        values.put("`transaction_date`", date);

        db.insert("`transaction`", null, values);

    }

    public List<Transaction> allTransactions(int registrationNo)                                //Return all transactions user has been involved with
    {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Transaction> transactionList = new ArrayList<Transaction>();
        String registrationStr = Integer.toString(registrationNo);

        String selectAllTransactions = "SELECT * FROM `transaction` WHERE receiver = ? OR sender = ?";

        Cursor cursor = db.rawQuery(selectAllTransactions, new String[] {registrationStr, registrationStr});

        if (cursor.moveToFirst())
        {
            do
            {
                Transaction transaction = new Transaction();

                transaction.setSender(Integer.parseInt(cursor.getString(0)));
                transaction.setReceiver(Integer.parseInt(cursor.getString(1)));
                transaction.setAmount(Double.parseDouble(cursor.getString(2)));
                transaction.setDate(cursor.getString(3));

                transactionList.add(transaction);
            }
            while(cursor.moveToNext());
        }

        return transactionList;

    }

    public Employee selectEmployee(int employeeNo)                                          //Select user where registrationNo = db registrationNo
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.query(
                TABLE_NAMES[2],
                new String[] {
                        TABLE_COLUMNS[2][0], TABLE_COLUMNS[2][1], TABLE_COLUMNS[2][2]},
                TABLE_COLUMNS[2][0] + " = ?", new String[] {String.valueOf(employeeNo)},
                null, null, null, null);

        if (result != null)
        {
            result.moveToFirst();

            Employee employee = new Employee(Integer.parseInt(result.getString(0)),
                    result.getString(1),
                    result.getString(2));

            return employee;
        }

        return null;
    }

    public void clearDatabase()                                                                 //Delete all rows in database (without deleting tables)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAMES[0], null, null);
        db.delete(TABLE_NAMES[1], null, null);
        db.delete(TABLE_NAMES[2], null, null);

        registrationNo = 0;
    }
}
