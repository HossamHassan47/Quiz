package com.wordpress.hossamhassan47.quiz.activities;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wordpress.hossamhassan47.quiz.R;
import com.wordpress.hossamhassan47.quiz.helper.JsonHelper;
import com.wordpress.hossamhassan47.quiz.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    List<RadioButton> rbOptions;
    List<CheckBox> chkOptions;
    EditText editTextAnswer;

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAudioManger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

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

                // Color Correct/Wrong answers
                highlightUserAnswer(isCorrect);

                // Play Sound
                playSound(isCorrect);

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
                    displayQuizResult();
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
        txtQuestionTitle.setText(currentQuestion.getTitle());

        // Clear previous question
        if (layoutQuestionOptions.getChildCount() > 0) {
            layoutQuestionOptions.removeAllViews();
        }

        // Display the current question
        if (currentQuestion.getType().equals("radio")) {
            rbOptions = new ArrayList<RadioButton>();

            // Create Radio Group
            RadioGroup rgOptions = new RadioGroup(this);
            rgOptions.setOrientation(RadioGroup.VERTICAL);

            // Create Radio button for Option 1
            RadioButton radioButton1 = new RadioButton(this);
            radioButton1.setText("" + currentQuestion.getOption1());
            rbOptions.add(radioButton1);

            // Create Radio button for Option 2
            if (currentQuestion.getOption2() != null && !currentQuestion.getOption2().isEmpty()) {
                RadioButton radioButton2 = new RadioButton(this);
                radioButton2.setText("" + currentQuestion.getOption2());
                rbOptions.add(radioButton2);
            }

            // Create Radio button for Option 3
            if (currentQuestion.getOption3() != null && !currentQuestion.getOption3().isEmpty()) {
                RadioButton radioButton3 = new RadioButton(this);
                radioButton3.setText("" + currentQuestion.getOption3());
                rbOptions.add(radioButton3);
            }

            // Create Radio button for Option 4
            if (currentQuestion.getOption4() != null && !currentQuestion.getOption4().isEmpty()) {
                RadioButton radioButton4 = new RadioButton(this);
                radioButton4.setText("" + currentQuestion.getOption4());
                rbOptions.add(radioButton4);
            }

            Collections.shuffle(rbOptions);

            for (int i = 0; i < rbOptions.size(); i++) {
                rgOptions.addView(rbOptions.get(i));
            }

            layoutQuestionOptions.addView(rgOptions);

        } else if (currentQuestion.getType().equals("text")) {
            // Create Edit Text
            editTextAnswer = new EditText(this);

            if (currentQuestion.getOption1() != null && !currentQuestion.getOption1().isEmpty()) {
                editTextAnswer.setText(currentQuestion.getOption1());
            }

            layoutQuestionOptions.addView(editTextAnswer);

        } else if (currentQuestion.getType().equals("checkbox")) {
            // Create Check Box array with size equals the options count
            chkOptions = new ArrayList<CheckBox>();

            // Create checkbox for Option 1
            CheckBox checkBox1 = new CheckBox(this);
            checkBox1.setText("" + currentQuestion.getOption1());
            chkOptions.add(checkBox1);

            // Create checkbox for Option 2
            if (currentQuestion.getOption2() != null && !currentQuestion.getOption2().isEmpty()) {
                CheckBox checkBox2 = new CheckBox(this);
                checkBox2.setText("" + currentQuestion.getOption2());
                chkOptions.add(checkBox2);
            }

            // Create checkbox for Option 3
            if (currentQuestion.getOption3() != null && !currentQuestion.getOption3().isEmpty()) {
                CheckBox checkBox3 = new CheckBox(this);
                checkBox3.setText("" + currentQuestion.getOption3());
                chkOptions.add(checkBox3);
            }

            // Create checkbox for Option 4
            if (currentQuestion.getOption4() != null && !currentQuestion.getOption4().isEmpty()) {
                CheckBox checkBox4 = new CheckBox(this);
                checkBox4.setText("" + currentQuestion.getOption4());
                chkOptions.add(checkBox4);
            }

            //Collections.shuffle(chkOptions);

            for (int i = 0; i < chkOptions.size(); i++) {
                layoutQuestionOptions.addView(chkOptions.get(i));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isCorrectAnswer() {
        String userAnswer = "";

        switch (currentQuestion.getType()) {
            case "text":
                userAnswer = editTextAnswer.getText().toString().trim();
                break;
            case "radio":
                for (int i = 0; i < rbOptions.size(); i++) {
                    if (rbOptions.get(i).isChecked()) {
                        userAnswer = rbOptions.get(i).getText().toString().trim();
                        break;
                    }
                }
                break;
            case "checkbox":
                for (int i = 0; i < chkOptions.size(); i++) {
                    if (chkOptions.get(i).isChecked()) {
                        userAnswer += chkOptions.get(i).getText().toString().trim() + ",";
                    }
                }

                // Trim the last character ','
                if (userAnswer != null && userAnswer.length() > 0 && userAnswer.charAt(userAnswer.length() - 1) == ',') {
                    userAnswer = userAnswer.substring(0, userAnswer.length() - 1);
                }
                break;
        }

        Log.v("Correct Answer", currentQuestion.getCorrectAnswer());
        Log.v("User Answer", userAnswer);
        Log.v("Is Correct", Objects.equals(currentQuestion.getCorrectAnswer(), userAnswer) + "");
        return Objects.equals(currentQuestion.getCorrectAnswer(), userAnswer);
    }

    private void highlightUserAnswer(boolean isCorrect) {

        switch (currentQuestion.getType()) {
            case "text":
                editTextAnswer.setTextColor(isCorrect ? getResources().getColor(R.color.correct_answer) :
                        getResources().getColor(R.color.wrong_answer));
                break;
            case "radio":
                for (int i = 0; i < rbOptions.size(); i++) {
                    // Color correct answer with Green
                    if (currentQuestion.getCorrectAnswer().equals(rbOptions.get(i).getText().toString().trim())) {
                        rbOptions.get(i).setTextColor(getResources().getColor(R.color.correct_answer));
                    }

                    if (!isCorrect && rbOptions.get(i).isChecked()) {
                        rbOptions.get(i).setTextColor(getResources().getColor(R.color.wrong_answer));
                    }
                }
                break;
            case "checkbox":
                for (int i = 0; i < chkOptions.size(); i++) {
                    // Color correct answer with Green
                    if (currentQuestion.getCorrectAnswer().contains(chkOptions.get(i).getText().toString().trim())) {
                        chkOptions.get(i).setTextColor(getResources().getColor(R.color.correct_answer));
                    }

                    // Color wrong answer with Red
                    if (!isCorrect && chkOptions.get(i).isChecked()
                            && !currentQuestion.getCorrectAnswer().contains(chkOptions.get(i).getText().toString().trim())) {
                        chkOptions.get(i).setTextColor(getResources().getColor(R.color.wrong_answer));
                    }
                }
                break;
        }
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

    private void displayQuizResult() {
        // Clear activity
        txtQuestionTitle.setVisibility(View.GONE);

        btnSubmitQuestion.setVisibility(View.GONE);
        btnNextQuestion.setVisibility(View.GONE);

        if (layoutQuestionOptions.getChildCount() > 0) {
            layoutQuestionOptions.removeAllViews();
        }

        // Check if passed or not
        boolean passed = quizMark >= 7;

        // Quiz Result
        String result;
        if (passed) {
            result = "Congratulations, You passed! \nYour Score: " + quizMark + " \nPassing Score: 7";
        } else {
            result = "Sorry, You didn't pass. \nYour Score: " + quizMark + " \nPassing Score: 7";
        }

        // Show result in Toast
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

        // Display result emotion icon
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(passed ? R.drawable.ic_emoticon_grey600_48dp : R.drawable.ic_emoticon_sad_grey600_48dp);
        layoutQuestionOptions.addView(imageView);

        // Display result text view
        TextView textView = new TextView(this);
        textView.setText(result);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setGravity(Gravity.CENTER);
        layoutQuestionOptions.addView(textView);
    }

    private void playSound(boolean isCorrect) {

        releaseMediaPlayer();

        // Request audio focus
        int result = mAudioManger.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            // We have audio focus now
            if (isCorrect) {
                mMediaPlayer = MediaPlayer.create(QuizActivity.this, R.raw.correct_answer);
            } else {
                mMediaPlayer = MediaPlayer.create(QuizActivity.this, R.raw.incorrect_answer);
            }

            mMediaPlayer.start();

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    releaseMediaPlayer();
                }
            });
        }
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // Permanent loss of audio focus
                        // Pause playback immediately
                        releaseMediaPlayer();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        // Pause playback
                        mMediaPlayer.pause();
                        mMediaPlayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Your app has been granted audio focus again
                        // Raise volume to normal, restart playback if necessary
                        mMediaPlayer.start();
                    }
                }
            };

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManger.abandonAudioFocus(afChangeListener);
        }
    }
}
