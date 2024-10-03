package com.example.a1200493_tala_jebrini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText FirstName = findViewById(R.id.First_name);
        EditText LastName = findViewById(R.id.Last_name);
        EditText ID = findViewById(R.id.ID);
        Button StartButton = (Button) findViewById(R.id.start);

        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String FirstNameInput = FirstName.getText().toString().trim();
                String LastNameInput = LastName.getText().toString().trim();
                String IDInput = ID.getText().toString().trim();
                //Check if their is missing info
                if (TextUtils.isEmpty(FirstNameInput) || TextUtils.isEmpty(LastNameInput) || TextUtils.isEmpty(IDInput)) {
                    Toast.makeText(MainActivity.this, "There is missing information!!", Toast.LENGTH_SHORT).show();
                }

                else{
                    //Extract the info to the second activity and move to it
                    Intent intent = new Intent(MainActivity.this, Hangman.class);
                    intent.putExtra("firstName", FirstName.getText().toString());
                    intent.putExtra("lastName", LastName.getText().toString());
                    intent.putExtra("studentid", ID.getText().toString());
                    MainActivity.this.startActivity(intent);
                    finish();
                }
            }
        });
    }

}