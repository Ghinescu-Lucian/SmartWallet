package com.upt.cti.smartwallet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.upt.cti.smartwallet.model.MonthlyExpenses;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private  final static String PREFS_SETTINGS = "prefs_settings";
    private SharedPreferences prefsUser, prefsApp;

    // ui
    private TextView tStatus;
    private EditText eSearch, eIncome, eExpenses;
    private Spinner sSearch;

    //firebase
    private DatabaseReference databaseReference;

    private String currentMonth;
    private ValueEventListener databaseListener;

    private final List<MonthlyExpenses> monthlyExpenses = new ArrayList<>();
   private final List<String> monthNames = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tStatus = (TextView) findViewById(R.id.tStatus);
//        eSearch = (EditText) findViewById(R.id.eSearch);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);
        sSearch = (Spinner) findViewById(R.id.spinner);
        sSearch.setOnItemSelectedListener(this);

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



//        databaseReference.child("calendar").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // simpler method
//                MonthlyExpenses monthlyExpense1 = snapshot.getValue(MonthlyExpenses.class);
//
//                // or field by field
//                MonthlyExpenses monthlyExpense2 = new MonthlyExpenses();
//                monthlyExpense2.setIncome((float) snapshot.child("income").getValue());
//                monthlyExpense2.setExpenses((float) snapshot.child("expenses").getValue());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        databaseReference.child("calendar").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    /* Retrieve lists of items or listen for additions to a list of items.
//                    This callback is triggered once for each existing child and then again
//                    every time a new child is added to the specified path. The DataSnapshot
//                    passed to the listener contains the new child's data. */
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                /* Listen for changes to the items in a list. This event fired any time a child
//                    node is modified, including any modifications to descendants of the child node.
//                    The DataSnapshot passed to the event listener contains the updated data for the
//                    child.*/
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                /* Listen for items being removed from a list. The DataSnapshot passed to the
//                event callback contains the data for the removed child. */
//
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                /* Listen for changes to the order of items in an ordered list. This event is
//                triggered whenever the onChildChanged() callback is triggered by an update that
//                causes reordering of the child. It is used with data that is ordered with
//                orderByChild or orderByValue. */
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//

        // spinner adapter
        final ArrayAdapter<String>  sAdapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, monthNames
        );


        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSearch.setAdapter(sAdapter);


        databaseReference.child("calendar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot monthSnapshot : snapshot.getChildren()) {
                    try {
//                        System.out.println("CEVADSAHJLKDBJLKANDK");
//                        System.out.println("CEVA BUB:"+monthSnapshot);

                        // create a new instance of MonthlyExpense
                            MonthlyExpenses m = new MonthlyExpenses();
                        // save the key as month name
                            m.setMonth(monthSnapshot.getKey());
                        // save the month and month name
                            m.setIncome(monthSnapshot.child("income").getValue(Float.class));
                            m.setExpenses(monthSnapshot.child("expenses").getValue(Float.class));

                            monthlyExpenses.add(m);
                            monthNames.add(m.getMonth());


                    } catch (Exception e) {

                    }


                   sAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }

    public void clicked(View view) {
        switch( view.getId()){
//            case R.id.bSearch:
//                if(!eSearch.getText().toString().isEmpty()){
//                    // save text to lower case
//                     currentMonth = eSearch.getText().toString().toLowerCase(Locale.ROOT);
//                    tStatus.setText("Searching ...");
//                    createNewDBListener();
//                }
//                else{
//                    Toast.makeText(this,"Search field may not be empty", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.bUpdate:
                if(!eIncome.getText().toString().isEmpty() &&
                        !eExpenses.getText().toString().isEmpty() &&
                        !tStatus.getText().toString().contains("Doesn't")
                ){

                    String key = sSearch.getSelectedItem().toString();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        if(!eSearch.getText().toString().isEmpty()){
                    // save text to lower case
                     currentMonth = sSearch.getSelectedItem().toString();
                    tStatus.setText("Searching ...");
                    createNewDBListener();
//                }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}