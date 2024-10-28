package com.example.a1200493_tala_jebrini;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class End extends AppCompatActivity {

    private DataBaseHelper dataBaseHelper;
    private TextView TopScoresList, TotalPlayersCount, AverageScoreValue, HighestScoreDetails, PlayerScores;
    private EditText UsernameEditText;
    private Button GetScoresButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        TopScoresList = findViewById(R.id.TopScoresList);
        TotalPlayersCount = findViewById(R.id.TotalPlayersCount);
        AverageScoreValue = findViewById(R.id.AverageScoreValue);
        HighestScoreDetails = findViewById(R.id.HighestScoreDetails);
        PlayerScores = findViewById(R.id.ScoresofUser);
        UsernameEditText = findViewById(R.id.UsernamePlaintext);
        GetScoresButton = findViewById(R.id.GetScoresBotton);
        dataBaseHelper = new DataBaseHelper(this, "User", null, 1);

        top5Scores();
        totalPlayers();
        AverageScore();
        HighestScore();
        AllPlayerScores();


    }

    public void top5Scores() {

        Cursor cursor = dataBaseHelper.getTop5Users();

        // StringBuilder to hold formatted scores
        StringBuilder topScoresText = new StringBuilder();

        // Check if cursor has data and read each row
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                int score = cursor.getInt(1);
                topScoresText.append("Name: ").append(name).append(", Score: ").append(score).append("\n");
            }
            cursor.close();
        } else {
            topScoresText.append("No data available");
        }

        TopScoresList.setText(topScoresText.toString());
    }

    public void totalPlayers() {

        int totalPlayers = dataBaseHelper.getTotalNumberOfPlayers();
        TotalPlayersCount.setText(String.valueOf(totalPlayers));
    }

    public void AverageScore() {
        double AverageScore = dataBaseHelper.getAverageScore();
        AverageScoreValue.setText(String.valueOf(AverageScore));
    }

    public void HighestScore() {
        Cursor cursor = dataBaseHelper.getHighestScore();

        // StringBuilder to hold the highest score details
        StringBuilder highestScoreText = new StringBuilder();

        // Check if cursor has data
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);         // ID column
            String name = cursor.getString(1); // Name column
            int score = cursor.getInt(2);      // Score column

            // Format the text
            highestScoreText.append("ID: ").append(id)
                    .append(", Name: ").append(name)
                    .append(", Score: ").append(score);

            cursor.close();
        } else {
            highestScoreText.append("No data available");
        }

        // Set the formatted text in the HighestScoreDetails TextView
        HighestScoreDetails.setText(highestScoreText.toString());
    }


    public void AllPlayerScores() {

        GetScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = UsernameEditText.getText().toString().trim();

                // Check if Username is empty
                if (Username.isEmpty()) {
                    Toast.makeText(End.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the user exists in the database
                if (!dataBaseHelper.userExists(Username)) {
                    Toast.makeText(End.this, "Username does not exist in the database", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the scores of the specified user
                Cursor cursor = dataBaseHelper.getScoresByUsername(Username);

                StringBuilder userScoresText = new StringBuilder();

                // Check if there are scores available for the user
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(0);       // First column (ID)
                        String name = cursor.getString(1); // Second column (Name)
                        int score = cursor.getInt(2);    // Third column (Score)

                        userScoresText.append("ID: ").append(id)
                                .append(", Name: ").append(name)
                                .append(", Score: ").append(score).append("\n");
                    }
                    cursor.close(); // Close cursor after use
                } else {
                    userScoresText.append("No scores available for this user.");
                }

                // Display the scores in the PlayerScores TextView
                PlayerScores.setText(userScoresText.toString());
            }
        });
    }


}
