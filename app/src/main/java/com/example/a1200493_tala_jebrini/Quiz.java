package com.example.a1200493_tala_jebrini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.graphics.Color;
import android.widget.Toast;

public class Quiz extends AppCompatActivity {

    private List<String> questions = new ArrayList<>();
    private List<String> answers = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    String username;
    private CountDownTimer timer;
    private TextView timeView;
    private TextView questionNumberView;
    private TextView questionView;
    private Button answer1, answer2, answer3;
    private String correctAnswer; // Store the correct answer separately

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // Retrieve the username from the intent
        username = getIntent().getStringExtra("username");

        timeView = findViewById(R.id.textView2);
        questionNumberView = findViewById(R.id.QuestionNumberText);
        questionView = findViewById(R.id.QuestionText);
        answer1 = findViewById(R.id.Answer1);
        answer2 = findViewById(R.id.Answer2);
        answer3 = findViewById(R.id.Answer3);

        // Load questions and answers from CSV
        loadQuestionsFromCSV();
        displayNextQuestion();

        // Set up button listeners
        answer1.setOnClickListener(v -> checkAnswer(answer1));
        answer2.setOnClickListener(v -> checkAnswer(answer2));
        answer3.setOnClickListener(v -> checkAnswer(answer3));
    }

    private void loadQuestionsFromCSV() {
        try {
            InputStream inputStream = getAssets().open("Questions.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    questions.add(parts[0]);
                    answers.add(parts[1]);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayNextQuestion() {
        if (currentQuestionIndex >= 5) {
            goToEndActivity();
            return;
        }

        // Set question number
        questionNumberView.setText(String.format(Locale.getDefault(), "#%d", currentQuestionIndex + 1));

        // Pick a random question that hasn't been used yet
        Random random = new Random();
        int randomIndex = random.nextInt(questions.size());
        String question = questions.get(randomIndex);
        correctAnswer = answers.get(randomIndex); // Store the correct answer

        // Remove used question
        questions.remove(randomIndex);
        answers.remove(randomIndex);

        questionView.setText(question);

        // Generate random close answers
        List<String> options = new ArrayList<>();
        options.add(correctAnswer);
        while (options.size() < 3) {
            int closeAnswer = Integer.parseInt(correctAnswer) + random.nextInt(5) - 2; // Adjust range as needed
            if (!options.contains(String.valueOf(closeAnswer))) {
                options.add(String.valueOf(closeAnswer));
            }
        }
        Collections.shuffle(options); // Randomize button order

        // Set options on buttons
        answer1.setText(options.get(0));
        answer2.setText(options.get(1));
        answer3.setText(options.get(2));

        startTimer();
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }

        long timeDuration = 60000; // 60 seconds in milliseconds
        timer = new CountDownTimer(timeDuration, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                timeView.setText(String.format(Locale.getDefault(), "%02d", secondsRemaining));
            }

            @Override
            public void onFinish() {
                // Time's up, move to next question
                timeView.setText("Time's up!");
                currentQuestionIndex++;
                displayNextQuestion();
            }
        }.start();
    }

    private void checkAnswer(Button selectedButton) {
        if (timer != null) {
            timer.cancel();
        }

        String selectedAnswer = selectedButton.getText().toString();

        if (selectedAnswer.equals(correctAnswer)) { // Compare with the stored correct answer
            selectedButton.setBackgroundColor(Color.GREEN); // Set button color to green for correct answer
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            score++;
        } else {
            selectedButton.setBackgroundColor(Color.RED); // Set button color to red for incorrect answer
            Toast.makeText(this, "Incorrect!", Toast.LENGTH_SHORT).show();

            //show the correct answer by changing the color of the correct answer button
            if (answer1.getText().toString().equals(correctAnswer)) {
                answer1.setBackgroundColor(Color.GREEN);
            } else if (answer2.getText().toString().equals(correctAnswer)) {
                answer2.setBackgroundColor(Color.GREEN);
            } else if (answer3.getText().toString().equals(correctAnswer)) {
                answer3.setBackgroundColor(Color.GREEN);
            }
        }

        // Delay before loading the next question to allow user to see the color change
        selectedButton.postDelayed(() -> {
            // Reset button colors to default after displaying feedback
            answer1.setBackgroundColor(getResources().getColor(R.color.purple_200));
            answer2.setBackgroundColor(getResources().getColor(R.color.purple_200));
            answer3.setBackgroundColor(getResources().getColor(R.color.purple_200));

            currentQuestionIndex++;
            displayNextQuestion();
        }, 1000);  // 1-second delay
    }

    private void goToEndActivity() {
        // Update the score in the database
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this, "User", null, 1);
        dataBaseHelper.updateUserScore(username, score); // Call method to update score

        Intent intent = new Intent(Quiz.this, End.class);
        intent.putExtra("username", username); // Pass the username to End activity
        startActivity(intent);
        finish();
    }

}
