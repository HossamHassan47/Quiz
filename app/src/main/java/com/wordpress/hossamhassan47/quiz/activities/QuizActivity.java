package com.wordpress.hossamhassan47.quiz.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.wordpress.hossamhassan47.quiz.helper.JsonHelper;
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

        // Set Quiz Subject
        quizSubject = getIntent().getExtras().getString("quizSubject");

        TextView txtQuizSubject = findViewById(R.id.text_view_quiz_subject);
        txtQuizSubject.setText(quizSubject);

        FrameLayout frameQuizSubject = findViewById(R.id.frame_layout_quiz_subject);

        // Set Subject Background based on Module
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
                    // Increment user mark by 1
                    quizMark += 1;
                }

                // Display Correct/Wrong answer message
                displayMessage(isCorrect);

                // Enable Next button
                btnNextQuestion.setEnabled(true);

                // Disable Submit button
                btnSubmitQuestion.setEnabled(false);
            }
        });

        // Next button
        btnNextQuestion = findViewById(R.id.button_next_question);
        btnNextQuestion.setEnabled(false);
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // No more question --> Display quiz result summary
                if (currentQuestionIndex >= (lstQuestions.size() - 1)) {
                    String result = "Your mark is " + quizMark + " of " + lstQuestions.size();

                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    return;
                }

                // Display next question
                currentQuestionIndex++;
                displayQuestion(currentQuestionIndex);

                // Disable Next button
                btnNextQuestion.setEnabled(false);

                // Enable Submit button
                btnSubmitQuestion.setEnabled(true);

                // Last question --> Display Finish button
                if (currentQuestionIndex == (lstQuestions.size() - 1)) {
                    btnNextQuestion.setText(getResources().getString(R.string.quiz_finish));
                }
            }
        });

        // Read questions based on quiz subject CSS, HTML, JavaScript...etc.
        lstQuestions = JsonHelper.ReadQuestions(getApplicationContext(), quizSubject);

        // Valid question list --> Display the 1st question
        if (lstQuestions.size() > 0) {
            displayQuestion(0);
        } else {
            btnSubmitQuestion.setEnabled(false);
        }
    }


    private void displayQuestion(int index) {
        // Get current question
        currentQuestion = lstQuestions.get(index);

        // Set question title text view
        txtQuestionTitle.setText(currentQuestion.Title);

        // Clear previous question
        if (layoutQuestionOptions.getChildCount() > 0) {
            layoutQuestionOptions.removeAllViews();
        }

        // Display the current question
        if (currentQuestion.Type.equals("radio")) {
            // Initiate radio button array with size equals the question options count
            rbOptions = new RadioButton[Integer.parseInt(currentQuestion.OptionsCount)];

            // Create Radio Group
            RadioGroup rgOptions = new RadioGroup(this);
            rgOptions.setOrientation(RadioGroup.VERTICAL);

            // Create Radio button for Option 1
            rbOptions[0] = new RadioButton(this);
            rbOptions[0].setText(" " + currentQuestion.Option1);
            rbOptions[0].setId(100 + 1);

            rgOptions.addView(rbOptions[0]);

            // Create Radio button for Option 2
            if (currentQuestion.Option2 != null && !currentQuestion.Option2.isEmpty()) {
                rbOptions[1] = new RadioButton(this);
                rbOptions[1].setText(" " + currentQuestion.Option2);
                rbOptions[1].setId(100 + 2);

                rgOptions.addView(rbOptions[1]);
            }

            // Create Radio button for Option 3
            if (currentQuestion.Option3 != null && !currentQuestion.Option3.isEmpty()) {
                rbOptions[2] = new RadioButton(this);
                rbOptions[2].setText(" " + currentQuestion.Option3);
                rbOptions[2].setId(100 + 3);

                rgOptions.addView(rbOptions[2]);
            }

            // Create Radio button for Option 4
            if (currentQuestion.Option4 != null && !currentQuestion.Option4.isEmpty()) {
                rbOptions[3] = new RadioButton(this);
                rbOptions[3].setText(" " + currentQuestion.Option4);
                rbOptions[3].setId(100 + 4);

                rgOptions.addView(rbOptions[3]);
            }

            layoutQuestionOptions.addView(rgOptions);

        } else if (currentQuestion.Type.equals("text")) {
            // Create Edit Text
            editTextAnswer = new EditText(this);

            if (currentQuestion.Option1 != null && !currentQuestion.Option1.isEmpty()) {
                editTextAnswer.setText(currentQuestion.Option1);
            }

            layoutQuestionOptions.addView(editTextAnswer);

        } else if (currentQuestion.Type.equals("checkbox")) {
            // Create Check Box array with size equals the options count
            chkOptions = new CheckBox[Integer.parseInt(currentQuestion.OptionsCount)];

            // Create checkbox for Option 1
            chkOptions[0] = new CheckBox(this);
            chkOptions[0].setText(" " + currentQuestion.Option1);
            chkOptions[0].setId(100 + 1);

            layoutQuestionOptions.addView(chkOptions[0]);

            // Create checkbox for Option 2
            if (currentQuestion.Option2 != null && !currentQuestion.Option2.isEmpty()) {
                chkOptions[1] = new CheckBox(this);
                chkOptions[1].setText(" " + currentQuestion.Option2);
                chkOptions[1].setId(100 + 2);

                layoutQuestionOptions.addView(chkOptions[1]);
            }

            // Create checkbox for Option 3
            if (currentQuestion.Option3 != null && !currentQuestion.Option3.isEmpty()) {
                chkOptions[2] = new CheckBox(this);
                chkOptions[2].setText(" " + currentQuestion.Option3);
                chkOptions[2].setId(100 + 3);

                layoutQuestionOptions.addView(chkOptions[2]);
            }

            // Create checkbox for Option 4
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

                // Trim the last character ','
                if (userAnswer != null && userAnswer.length() > 0 && userAnswer.charAt(userAnswer.length() - 1) == ',') {
                    userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
                }
                break;
        }

        //Log.v("user answer", userAnswer);
        //Log.v("correct answer", currentQuestion.CorrectAnswer);
        //Log.v("is correct", userAnswer.equals(currentQuestion.CorrectAnswer) + "");

        return Objects.equals(currentQuestion.CorrectAnswer, userAnswer);
    }

    private void displayMessage(boolean isCorrectAnswer) {
        // Create message text view
        TextView txtMessage = new TextView(this);

        // Set message text and background color based for Correct/Wrong answers
        if (isCorrectAnswer) {
            txtMessage.setText(getResources().getString(R.string.message_correct));
            txtMessage.setBackgroundColor(getResources().getColor(R.color.message_green));
        } else {
            txtMessage.setText(getResources().getString(R.string.message_wrong));
            txtMessage.setBackgroundColor(getResources().getColor(R.color.message_red));
        }

        // Set message color, text size, padding, gravity
        txtMessage.setTextColor(Color.WHITE);
        txtMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        txtMessage.setPadding(8, 8, 8, 8);
        txtMessage.setGravity(Gravity.CENTER);

        // Finally, display the message
        layoutQuestionOptions.addView(txtMessage);
    }
}
