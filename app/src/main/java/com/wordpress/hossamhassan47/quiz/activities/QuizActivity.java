package com.wordpress.hossamhassan47.quiz.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wordpress.hossamhassan47.quiz.R;

public class QuizActivity extends AppCompatActivity {

    String quizSubject;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Team A Name
        quizSubject = getIntent().getExtras().getString("quizSubject");
        userName = getIntent().getExtras().getString("quizSubject");

        TextView txtQuizSubject = findViewById(R.id.text_view_quiz_subject);
        txtQuizSubject.setText(quizSubject);

        FrameLayout frameQuizSubject = findViewById(R.id.frame_layout_quiz_subject);

        if (quizSubject.equals("CSS")) {
            frameQuizSubject.setBackgroundResource(R.color.background_CSS);
        } else if (quizSubject.equals("HTML")) {
            frameQuizSubject.setBackgroundResource(R.color.background_HTML);
        } else if (quizSubject.equals("JavaScript")){
            frameQuizSubject.setBackgroundResource(R.color.background_JavaScript);
        }

    }

}
