package com.example.geoquiz;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";

    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionTextView;
    private int count = 0;

    private Question[] questions = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        questionTextView = findViewById(R.id.question_text_view);

        updateQuestion();

        trueButton.setOnClickListener(view ->
                checkAnswer(true)
        );

        falseButton.setOnClickListener(view ->
                checkAnswer(false)
        );

        nextButton.setOnClickListener(view -> {
            currentIndex = (currentIndex + 1) % questions.length;
            updateQuestion();
        });

        prevButton.setOnClickListener(view -> {
            currentIndex -= 1;
            if (currentIndex < 0) {
                currentIndex = questions.length - 1;
            }

            updateQuestion();
        });

        questionTextView.setOnClickListener(view -> {
            currentIndex = (currentIndex + 1) % questions.length;
            updateQuestion();
        });
    }

    private void showToast(int messageId) {
        Toast toast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void updateQuestion() {
        setEnabledButtons(true);
        int question = questions[currentIndex].getTextResId();
        questionTextView.setText(question);
    }

    private void checkAnswer(boolean value) {
        setEnabledButtons(false);
        boolean answer = questions[currentIndex].isAnswer();
        int messageId;
        if (answer == value) {
            messageId = R.string.correct_answer;
            count++;
        } else {
            messageId = R.string.incorrect_answer;
        }
        showToast(messageId);
        if(currentIndex == questions.length -1) {
            int percent =(count * 100) / questions.length;
            String message = "Your result: " + percent;
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void setEnabledButtons(boolean value) {
        trueButton.setEnabled(value);
        falseButton.setEnabled(value);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, currentIndex);
    }
}