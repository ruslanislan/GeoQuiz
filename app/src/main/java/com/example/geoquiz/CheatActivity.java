package com.example.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {
    private static final String EXTRA_ANSWER_IS_TRUE = "extra_answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "answer_shown";
    private static final String CAN_CHEATING = "can_cheating";
    private boolean answer;
    private boolean cheated;
    private boolean canCheating;

    private TextView answerTextView;
    private TextView apiLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        restore(savedInstanceState);

        answerTextView = findViewById(R.id.answer_text_view);
        apiLevelTextView = findViewById(R.id.api_level_text_view);
        Button showAnswer = findViewById(R.id.show_answer_button);

        String apiLevel = "API Level " + Build.VERSION.SDK_INT;

        apiLevelTextView.setText(apiLevel);


        answer = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        canCheating = getIntent().getBooleanExtra(CAN_CHEATING, true);


        showAnswer.setOnClickListener(view -> {
            if (!canCheating) {
                answerTextView.setText(R.string.can_not_cheating);
            } else if (answer) {
                answerTextView.setText(R.string.true_button);
            } else {
                answerTextView.setText(R.string.false_button);
            }
            setAnswerShownResult();
            cheated = true;
            int cx = showAnswer.getWidth() / 2;
            int cy = showAnswer.getHeight() / 2;
            float radius = showAnswer.getWidth();
            Animator anim = ViewAnimationUtils
                    .createCircularReveal(showAnswer, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    showAnswer.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        });
    }

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, true);
        setResult(RESULT_OK, data);
    }

    public static Intent newIntent(Context packageContext, boolean value, boolean canCheating) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, value);
        intent.putExtra(CAN_CHEATING, canCheating);
        return intent;
    }

    public static boolean wasAnswerShown(Intent intent) {
        return intent.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(EXTRA_ANSWER_IS_TRUE, cheated);
    }

    protected void restore(Bundle state) {
        if (state != null) {
            cheated = state.getBoolean(EXTRA_ANSWER_IS_TRUE, false);
            if (cheated) {
                setAnswerShownResult();
            }
        }
    }
}