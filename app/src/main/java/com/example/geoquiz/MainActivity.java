package com.example.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_IS_CHEATER = "isCheater";
    private static final String KEY_ANSWERED = "answered";
    private static final String CHEATING_COUNT="cheating_count";

    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private Button cheatButton;
    private TextView questionTextView;
    private int count = 0;
    private boolean isCheater;
    private boolean answered;
    private int cheatingCount = 0;

    private final Question[] questions = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int currentIndex = 0;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        if(data == null) {
                            return;
                        }
                        cheatingCount++;
                        isCheater = CheatActivity.wasAnswerShown(data);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        questionTextView = findViewById(R.id.question_text_view);
        cheatButton = findViewById(R.id.cheat_button);

        restoreState(savedInstanceState);
        updateQuestion();

        trueButton.setOnClickListener(view ->
                checkAnswer(true)
        );

        falseButton.setOnClickListener(view ->
                checkAnswer(false)
        );

        nextButton.setOnClickListener(view -> {
            if(currentIndex == questions.length - 1){
                return;
            }
            currentIndex = (currentIndex + 1) % questions.length;
            isCheater = false;
            setEnabledButtons(true);
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

        cheatButton.setOnClickListener(view -> {
            boolean answer = questions[currentIndex].isAnswer();
            boolean canCheating = cheatingCount < 3;
            Intent intent = CheatActivity.newIntent(MainActivity.this, answer, canCheating);
            activityResultLauncher.launch(intent);
        });
    }

    private void showToast(int messageId) {
        Toast toast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void updateQuestion() {
        int question = questions[currentIndex].getTextResId();
        questionTextView.setText(question);
    }

    private void checkAnswer(boolean value) {
        setEnabledButtons(false);
        boolean answer = questions[currentIndex].isAnswer();
        int messageId;
        if(isCheater){
            messageId = R.string.judgment_toast;
        } else
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
        answered = value;
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
        outState.putBoolean(KEY_IS_CHEATER, isCheater);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putInt(CHEATING_COUNT, cheatingCount);
    }

    protected void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            isCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED, false);
            cheatingCount = savedInstanceState.getInt(CHEATING_COUNT, 0);
            setEnabledButtons(answered);
        }

    }
}