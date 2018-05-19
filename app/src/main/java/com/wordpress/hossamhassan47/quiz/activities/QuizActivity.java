package com.wordpress.hossamhassan47.quiz.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.quiz.R;
import com.wordpress.hossamhassan47.quiz.fragments.StartQuizFragment;
import com.wordpress.hossamhassan47.quiz.model.JsonHelper;
import com.wordpress.hossamhassan47.quiz.model.Question;

import java.util.ArrayList;
import java.util.Objects;

public class QuizActivity extends AppCompatActivity {

    String quizSubject;

    ArrayList<Question> lstQuestions;

    Question currentQuestion;
    int currentQuestionIndex = 0;

    int quizMark = 0;

    TextView txtQuestionTitle;
    LinearLayout layoutQuestionOptions;

    Button btnSubmitQuestion;
    Button btnNextQuestion;

    RadioButton[] rbOptions;
    CheckBox[] chkOptions;
    EditText editTextAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        quizSubject = getIntent().getExtras().getString("quizSubject");

        // Set Quiz Subject
        TextView txtQuizSubject = findViewById(R.id.text_view_quiz_subject);
        txtQuizSubject.setText(quizSubject);

        FrameLayout frameQuizSubject = findViewById(R.id.frame_layout_quiz_subject);

        if (quizSubject.equals("CSS")) {
            frameQuizSubject.setBackgroundResource(R.color.background_CSS);
        } else if (quizSubject.equals("HTML")) {
            frameQuizSubject.setBackgroundResource(R.color.background_HTML);
        } else if (quizSubject.equals("JavaScript")) {
            frameQuizSubject.setBackgroundResource(R.color.background_JavaScript);
        }

        // Question Subject & Options
        txtQuestionTitle = findViewById(R.id.text_view_question_title);
        layoutQuestionOptions = findViewById(R.id.linear_layout_question_options);

        // Submit button
        btnSubmitQuestion = findViewById(R.id.button_submit_answer);
        btnSubmitQuestion.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                // Check user answer
                boolean isCorrect = isCorrectAnswer();
                if (isCorrect) {
                    quizMark += 1;
                }

                displayMessage(isCorrect);

                btnNextQuestion.setEnabled(true);
                btnSubmitQuestion.setEnabled(false);
            }
        });

        // Next button
        btnNextQuestion = findViewById(R.id.button_next_question);
        btnNextQuestion.setEnabled(false);
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentQuestionIndex >= (lstQuestions.size() - 1)) {
                    // Display quiz mark
                    String result = "Your mark is " + quizMark + " of " + lstQuestions.size();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    return;
                }

                // Display next question
                currentQuestionIndex++;
                displayQuestion(currentQuestionIndex);

                btnNextQuestion.setEnabled(false);
                btnSubmitQuestion.setEnabled(true);

                if (currentQuestionIndex == (lstQuestions.size() - 1)) {
                    // Last question
                    btnNextQuestion.setText("Finish");
                }
            }
        });

        // Read questions
        lstQuestions = JsonHelper.ReadQuestions(getApplicationContext(), quizSubject);

        // Display 1st question
        if (lstQuestions.size() > 0) {
            displayQuestion(0);
        }
        else {
            btnSubmitQuestion.setEnabled(false);
        }
    }

    private void displayQuestion(int index) {
        // Get current question
        currentQuestion = lstQuestions.get(index);

        // Set question title text view
        txtQuestionTitle.setText(currentQuestion.Title);

        // Clear previous options if any exists
        if (layoutQuestionOptions.getChildCount() > 0) {
            layoutQuestionOptions.removeAllViews();
        }

        // Add the current options based on question Type
        if (currentQuestion.Type.equals("radio")) {

            rbOptions = new RadioButton[Integer.parseInt(currentQuestion.OptionsCount)];

            RadioGroup rgOptions = new RadioGroup(this);
            rgOptions.setOrientation(RadioGroup.VERTICAL);

            // Option 1
            rbOptions[0] = new RadioButton(this);
            rbOptions[0].setText(" " + currentQuestion.Option1);
            rbOptions[0].setId(100 + 1);

            rgOptions.addView(rbOptions[0]);

            // Option 2
            if (currentQuestion.Option2 != null && !currentQuestion.Option2.isEmpty()) {
                rbOptions[1] = new RadioButton(this);
                rbOptions[1].setText(" " + currentQuestion.Option2);
                rbOptions[1].setId(100 + 2);

                rgOptions.addView(rbOptions[1]);
            }

            // Option 3
            if (currentQuestion.Option3 != null && !currentQuestion.Option3.isEmpty()) {
                rbOptions[2] = new RadioButton(this);
                rbOptions[2].setText(" " + currentQuestion.Option3);
                rbOptions[2].setId(100 + 3);

                rgOptions.addView(rbOptions[2]);
            }

            // Option 4
            if (currentQuestion.Option4 != null && !currentQuestion.Option4.isEmpty()) {
                rbOptions[3] = new RadioButton(this);
                rbOptions[3].setText(" " + currentQuestion.Option4);
                rbOptions[3].setId(100 + 4);

                rgOptions.addView(rbOptions[3]);
            }

            layoutQuestionOptions.addView(rgOptions);

        } else if (currentQuestion.Type.equals("text")) {
            editTextAnswer = new EditText(this);

            if (currentQuestion.Option1 != null && !currentQuestion.Option1.isEmpty()) {
                editTextAnswer.setText(currentQuestion.Option1);
            }

            layoutQuestionOptions.addView(editTextAnswer);

        } else if (currentQuestion.Type.equals("checkbox")) {

            chkOptions = new CheckBox[Integer.parseInt(currentQuestion.OptionsCount)];

            // Option 1
            chkOptions[0] = new CheckBox(this);
            chkOptions[0].setText(" " + currentQuestion.Option1);
            chkOptions[0].setId(100 + 1);

            layoutQuestionOptions.addView(chkOptions[0]);

            // Option 2
            if (currentQuestion.Option2 != null && !currentQuestion.Option2.isEmpty()) {
                chkOptions[1] = new CheckBox(this);
                chkOptions[1].setText(" " + currentQuestion.Option2);
                chkOptions[1].setId(100 + 2);

                layoutQuestionOptions.addView(chkOptions[1]);
            }

            // Option 3
            if (currentQuestion.Option3 != null && !currentQuestion.Option3.isEmpty()) {
                chkOptions[2] = new CheckBox(this);
                chkOptions[2].setText(" " + currentQuestion.Option3);
                chkOptions[2].setId(100 + 3);

                layoutQuestionOptions.addView(chkOptions[2]);
            }

            // Option 4
            if (currentQuestion.Option4 != null && !currentQuestion.Option4.isEmpty()) {
                chkOptions[3] = new CheckBox(this);
                chkOptions[3].setText(" " + currentQuestion.Option4);
                chkOptions[3].setId(100 + 4);

                layoutQuestionOptions.addView(chkOptions[3]);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isCorrectAnswer() {
        String userAnswer = "";

        switch (currentQuestion.Type) {
            case "text":
                userAnswer = editTextAnswer.getText().toString().trim();
                break;
            case "radio":
                for (int i = 0; i < rbOptions.length; i++) {
                    if (rbOptions[i].isChecked()) {
                        userAnswer = rbOptions[i].getText().toString().trim();
                        break;
                    }
                }

                break;
            case "checkbox":
                for (int i = 0; i < chkOptions.length; i++) {
                    if (chkOptions[i].isChecked()) {
                        userAnswer += chkOptions[i].getText().toString().trim() + ",";
                    }
                }

                if (userAnswer != null && userAnswer.length() > 0 && userAnswer.charAt(userAnswer.length() - 1) == ',') {
                    userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
                }

                break;
        }

        Log.v("user answer", userAnswer);
        Log.v("correct answer", currentQuestion.CorrectAnswer);
        Log.v("is correct", userAnswer.equals(currentQuestion.CorrectAnswer) + "");

        return Objects.equals(currentQuestion.CorrectAnswer, userAnswer);
    }

    private void displayMessage(boolean isCorrectAnswer) {
        TextView txtMessage = new TextView(this);

        if (isCorrectAnswer) {
            txtMessage.setText(getResources().getString(R.string.message_correct));
            txtMessage.setBackgroundColor(getResources().getColor(R.color.message_green));
        } else {
            txtMessage.setText(getResources().getString(R.string.message_wrong));
            txtMessage.setBackgroundColor(getResources().getColor(R.color.message_red));
        }

        txtMessage.setTextColor(Color.WHITE);
        txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        txtMessage.setPadding(8, 8, 8, 8);
        txtMessage.setGravity(Gravity.CENTER);
        layoutQuestionOptions.addView(txtMessage);
    }
}
