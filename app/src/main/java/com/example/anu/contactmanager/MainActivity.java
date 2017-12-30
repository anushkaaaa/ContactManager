


package com.example.anu.contactmanager;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Data.DatabaseHandler;
import Model.Contact;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private EditText phoneNumber;
    private Button add;
    private Button clear;
    private TextView display;
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText) findViewById(R.id.name);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        display = (TextView) findViewById(R.id.display);
        add = (Button) findViewById(R.id.Add_button);
        clear = (Button) findViewById(R.id.clear_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = "";
                Contact c  = new Contact(name.getText().toString(),phoneNumber.getText().toString());
                db.add(c);
                List<Contact> contactList = db.getAll();
                for(Contact contact: contactList ){
                    String temp = contact.getName() + "  " + contact.getPhoneNumber()+"\n";
                    result += temp;
                }
                display.setText(result);
                name.setText("");
                phoneNumber.setText("");
        }});
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                phoneNumber.setText("");
            }
        });
    }

}
