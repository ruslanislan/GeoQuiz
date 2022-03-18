package com.example.geoquiz;

public class Question {

    public int getTextResId() {
        return textResId;
    }

    public boolean isAnswer() {
        return answer;
    }

    private int textResId;
    private boolean answer;

    public Question(int textResId, boolean answerTrue) {
        this.textResId = textResId;
        this.answer = answerTrue;
    }
}
