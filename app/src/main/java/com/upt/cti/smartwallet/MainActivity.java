package com.upt.cti.smartwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upt.cti.smartwallet.model.MonthlyExpenses;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private  final static String PREFS_SETTINGS = "prefs_settings";
    private SharedPreferences prefsUser, prefsApp;

    // ui
    private TextView tStatus;
    private EditText eSearch, eIncome, eExpenses;

    //firebase
    private DatabaseReference databaseReference;

    private String currentMonth;
    private ValueEventListener databaseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tStatus = (TextView) findViewById(R.id.tStatus);
        eSearch = (EditText) findViewById(R.id.eSearch);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-b8dc0-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference();
/*
        // named preference file
        prefsUser = getSharedPreferences( PREFS_SETTINGS, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefsUser.edit();
        editor.putInt("KEY1",10);
        editor.putString("KEY2","hello there");
        editor.putBoolean("KEY3", true);
        editor.putFloat("KEY4", 3.1415f);
        editor.commit();

        // one-line code for writing data to file
        prefsUser.edit().putLong("KEY5",100022L).apply();

        // citire

        int score = prefsUser.getInt("KEY1",0); // 0 = valoarea daca nu se gaseste
        String name = prefsUser.getString("KEY2",null);

        //default prefs file for this app
        prefsApp = getPreferences(Context.MODE_PRIVATE);


        // Internal storage and external

        String filename = "settings";
        String string = "Hello there!";
        FileOutputStream outputStream;

        try{
            outputStream = openFileOutput(filename,Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */



    }

    public void clicked(View view) {
        switch( view.getId()){
            case R.id.bSearch:
                if(!eSearch.getText().toString().isEmpty()){
                    // save text to lower case
                     currentMonth = eSearch.getText().toString().toLowerCase(Locale.ROOT);
                    tStatus.setText("Searching ...");
                    createNewDBListener();
                }
                else{
                    Toast.makeText(this,"Search field may not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bUpdate:
                if(!eIncome.getText().toString().isEmpty() &&
                        !eExpenses.getText().toString().isEmpty() &&
                        !tStatus.getText().toString().contains("Doesn't")
                ){
                    String key = currentMonth;
//                            databaseReference.child("calendar").push().getKey();
                    MonthlyExpenses post = new MonthlyExpenses(currentMonth, Float.parseFloat(eIncome.getText().toString()), Float.parseFloat(eExpenses.getText().toString()));
                    Map<String, Object> postValues = post.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/calendar/" + key, postValues);
//                    childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

                    databaseReference.updateChildren(childUpdates);
                }
                break;
        }
    }
    private void createNewDBListener() {
        // remove previous databaseListener
        if (databaseReference != null && currentMonth != null && databaseListener != null)
            databaseReference.child("calendar").child(currentMonth).removeEventListener(databaseListener);
        System.out.println("CEVA");
        databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                MonthlyExpenses monthlyExpense = dataSnapshot.getValue(MonthlyExpenses.class);
                // explicit mapping of month name from entry key
                if(monthlyExpense == null){
                    tStatus.setText("Doesn't found entry for " + currentMonth);
//                    Toast.makeText(this,"Search field may not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                monthlyExpense.month = dataSnapshot.getKey();

                eIncome.setText(String.valueOf(monthlyExpense.getIncome()));
                eExpenses.setText(String.valueOf(monthlyExpense.getExpenses()));
                tStatus.setText("Found entry for " + currentMonth);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };

        // set new databaseListener
        databaseReference.child("calendar").child(currentMonth).addValueEventListener(databaseListener);
    }
}