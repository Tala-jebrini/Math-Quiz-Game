package com.example.a1200493_tala_jebrini;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        EditText Username = findViewById(R.id.Username);
        EditText Email = findViewById(R.id.Email);
        Button StartButton = (Button) findViewById(R.id.start);


        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String UsernameInput = Username.getText().toString().trim();
                String EmailInput = Email.getText().toString().trim();
                if (TextUtils.isEmpty(UsernameInput) || TextUtils.isEmpty(EmailInput)) {
                    Toast.makeText(MainActivity.this, "There is missing information!!", Toast.LENGTH_SHORT).show();
                }
                // Validate email format
                else if (!Patterns.EMAIL_ADDRESS.matcher(EmailInput).matches()) {
                    Toast.makeText(MainActivity.this, "Invalid email format!", Toast.LENGTH_SHORT).show();
                }
                // Validate that a date was selected and is in the past
                else if (selectedDate == null || !isValidBirthday(selectedDate)) {
                    Toast.makeText(MainActivity.this, "Please select a valid birth date!", Toast.LENGTH_SHORT).show();
                } else {
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this, "User", null, 1);
                    if (dataBaseHelper.userExists(UsernameInput)) {
                        // Show a message if the username already exists
                        Toast.makeText(MainActivity.this, "Username already exists! Please choose another username.", Toast.LENGTH_SHORT).show();
                    } else {
                        User newUser = new User(UsernameInput, 0);  // Pass only username and default score (0)

                        // Insert the user into the database
                        dataBaseHelper.insertUser(newUser);

                        // Start Quiz activity
                        Intent intent = new Intent(MainActivity.this, Quiz.class);
                        intent.putExtra("username", UsernameInput); // Pass the username to Quiz activity
                        MainActivity.this.startActivity(intent);
                        finish();

                    }
                }
            }
        });
        dateButton.setText(getTodaysDate());
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                selectedDate = makeDateString(day, month, year);
                dateButton.setText(selectedDate);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    private boolean isValidBirthday(String date) {
        Calendar currentDate = Calendar.getInstance();
        Calendar selectedDateCal = Calendar.getInstance();

        // Parse the selected date (assuming the format "MMM dd yyyy")
        String[] parts = date.split(" ");
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        int month = getMonthInt(parts[0]) - 1;

        selectedDateCal.set(year, month, day);

        // Check that the selected date is not in the future
        return !selectedDateCal.after(currentDate);
    }

    private int getMonthInt(String month) {
        switch (month) {
            case "JAN":
                return 1;
            case "FEB":
                return 2;
            case "MAR":
                return 3;
            case "APR":
                return 4;
            case "MAY":
                return 5;
            case "JUN":
                return 6;
            case "JUL":
                return 7;
            case "AUG":
                return 8;
            case "SEP":
                return 9;
            case "OCT":
                return 10;
            case "NOV":
                return 11;
            case "DEC":
                return 12;
            default:
                return 1; // default should never happen
        }
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
