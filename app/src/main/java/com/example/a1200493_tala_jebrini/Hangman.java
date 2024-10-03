package com.example.a1200493_tala_jebrini;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class Hangman extends AppCompatActivity {
    private int FailureCounter=0;
    private int SucceedCounter=0;
    //This ArrayList to save the entered letters to not repeat them
    private ArrayList<Character> enteredLetters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);


        Button SubmitButton = findViewById(R.id.submit);
        Button RestartButton = (Button) findViewById(R.id.restart);

        FrameLayout frameLayout = findViewById(R.id.shape);

        EditText LetterEditText = findViewById(R.id.letter);

        TextView FullNameTextView = findViewById(R.id.full_name);
        TextView IDTextView = findViewById(R.id.Id_num);
        TextView Letter1TextView = findViewById(R.id.l1);
        TextView Letter2TextView = findViewById(R.id.l2);
        TextView Letter3TextView = findViewById(R.id.l3);
        TextView Letter4TextView = findViewById(R.id.l4);
        TextView Letter5TextView = findViewById(R.id.l5);
        TextView Letter6TextView = findViewById(R.id.l6);
        TextView Letter7TextView = findViewById(R.id.l7);

        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String StudentId = getIntent().getStringExtra("studentid");

        // Display or use the data as needed
        FullNameTextView.setText(firstName + " " + lastName);
        IDTextView.setText(StudentId);


        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String LetterInput = LetterEditText.getText().toString().trim();
                LetterEditText.setText("");
                //Check if the user click the button before entering a letter
                if (TextUtils.isEmpty(LetterInput) ) {
                    Toast.makeText(Hangman.this, "Enter a letter!", Toast.LENGTH_SHORT).show();
                }
                //Check if the user entered only one letter
                else if (LetterInput.length() == 1 && Character.isLetter(LetterInput.charAt(0))) {
                    char UserLetter = LetterInput.charAt(0); // Convert to char
                    //Check if the user entered the same letter more than once
                    if (isLetterEnteredBefore(enteredLetters, UserLetter)) {
                        Toast.makeText(Hangman.this, "You tried this letter already!", Toast.LENGTH_SHORT).show();
                    }
                    //Ckeck if the letter is correct or no
                    else{

                        if (UserLetter == 'b' || UserLetter == 'B'){
                            Letter1TextView.setText("B");
                            SucceedCounter++;
                        }
                        else if (UserLetter == 'i' || UserLetter == 'I'){
                            Letter2TextView.setText("i");
                            Letter6TextView.setText("i");
                            SucceedCounter++;

                        }
                        else if (UserLetter == 'r' || UserLetter == 'R'){
                            Letter3TextView.setText("r");
                            SucceedCounter++;

                        }
                        else if (UserLetter == 'z' || UserLetter == 'Z'){
                            Letter4TextView.setText("z");
                            SucceedCounter++;

                        }
                        else if (UserLetter == 'e' || UserLetter == 'E'){
                            Letter5TextView.setText("e");
                            SucceedCounter++;

                        }
                        else if (UserLetter == 't' || UserLetter == 'T'){
                            Letter7TextView.setText("t");
                            SucceedCounter++;

                        }


                        else{
                            FailureCounter++;

                            if(FailureCounter == 1){
                                frameLayout.addView(createHeadTextView());
                            }
                            else if(FailureCounter == 2){
                                frameLayout.addView(createBodyTextView());
                            }
                            else if(FailureCounter == 3){
                                frameLayout.addView(createRightHandTextView());
                            }
                            else if(FailureCounter == 4){
                                frameLayout.addView(createLeftHandTextView());
                            }
                            else if(FailureCounter == 5){
                                frameLayout.addView(createRightLegTextView());
                            }
                            else if(FailureCounter == 6){
                                frameLayout.addView(createLeftLegTextView());
                                for (int i = 0; i < 500; i++) {
                                }
                                String LosingText = "Game Over  "+ firstName +", you lost :(";
                                frameLayout.addView(createResultTextView(LosingText,getResources().getColor(android.R.color.holo_red_light)));
                                frameLayout.addView(createLeftXTextView());
                                frameLayout.addView(createRightXTextView());
                            }

                        }

                        if(SucceedCounter == 6){
                            String WinningText = "Congrats "+ firstName +", you won!!";
                            frameLayout.addView(createResultTextView(WinningText,getResources().getColor(android.R.color.holo_green_light)));

                        }

                    }
                }
                else {
                    // Invalid input
                    Toast.makeText(Hangman.this, "Please enter only one letter.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        RestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Return to the initial values to start the game again
                frameLayout.removeAllViews();
                LetterEditText.setHint("Enter a letter");
                LetterEditText.setText("");
                Letter1TextView.setText("---");
                Letter2TextView.setText("---");
                Letter3TextView.setText("---");
                Letter4TextView.setText("---");
                Letter5TextView.setText("---");
                Letter6TextView.setText("---");
                Letter7TextView.setText("---");
                FailureCounter=0;
                SucceedCounter=0;
                enteredLetters = new ArrayList<>();

            }
        });
    }

    //The function that checks repetition
    public boolean isLetterEnteredBefore(ArrayList<Character> enteredLetters, char letter) {
        if (enteredLetters.contains(letter)) {
            return true; // Letter was entered before
        } else {
            enteredLetters.add(letter); // Add letter to list
            return false; // First time entering this letter
        }
    }

    //Cresting the text that contains the result of the game
    private TextView createResultTextView(String text, int color) {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));
        textView.setText(text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(color);  // Use the passed color parameter
        textView.setTextSize(34);
        textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL)); // Set font family
        return textView;
    }

    //Creating the head and locate it
    //The rest methods for the rest of the body
    private TextView createHeadTextView() {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
        params.leftMargin = 495; // margin in pixels
        params.topMargin = 138;   // margin in pixels
        textView.setLayoutParams(params);

        textView.setText("o");
        textView.setTextSize(80);
        textView.setTypeface(Typeface.create("sans-serif-condensed-medium", Typeface.NORMAL)); // Set font family
        return textView;
    }

    private TextView createBodyTextView() {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
        params.leftMargin = 523; // margin in pixels
        params.topMargin = 330;  // margin in pixels
        textView.setLayoutParams(params);

        textView.setText("|");
        textView.setTextSize(80);
        textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL)); // Set font family
        return textView;
    }

    private TextView createLeftHandTextView() {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
        params.leftMargin = 454; // margin in pixels
        params.topMargin = 330;  // margin in pixels
        textView.setLayoutParams(params);

        textView.setText("/");
        textView.setTextSize(80);
        textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL)); // Set font family
        return textView;
    }

    private TextView createRightHandTextView() {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
        params.leftMargin = 564; // margin in pixels
        params.topMargin = 330;  // margin in pixels
        textView.setLayoutParams(params);

        textView.setText("\\");
        textView.setTextSize(80);
        textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL)); // Set font family
        return textView;
    }

    private TextView createLeftLegTextView() {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
        params.leftMargin = 454; // margin in pixels
        params.topMargin = 523;  // margin in pixels
        textView.setLayoutParams(params);

        textView.setText("/");
        textView.setTextSize(80);
        textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL)); // Set font family
        return textView;
    }

    private TextView createRightLegTextView() {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
        params.leftMargin = 564; // margin in pixels
        params.topMargin = 523;  // margin in pixels
        textView.setLayoutParams(params);

        textView.setText("\\");
        textView.setTextSize(80);
        textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL)); // Set font family
        return textView;
    }

    private TextView createLeftXTextView() {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
        params.leftMargin = 399; // margin in pixels
        params.topMargin = 55;  // margin in pixels
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        textView.setText("/");
        textView.setTextSize(300);
        textView.setTypeface(Typeface.create("cursive", Typeface.NORMAL)); // Set font family
        return textView;
    }

    private TextView createRightXTextView() {
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT));

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) textView.getLayoutParams();
        params.leftMargin = 344; // margin in pixels
        params.topMargin = 55;  // margin in pixels
        textView.setLayoutParams(params);
        textView.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        textView.setText("\\");
        textView.setTextSize(300);
        textView.setTypeface(Typeface.create("cursive", Typeface.NORMAL)); // Set font family
        return textView;
    }

}
