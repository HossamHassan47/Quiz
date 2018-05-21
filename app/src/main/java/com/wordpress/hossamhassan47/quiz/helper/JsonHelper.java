package com.wordpress.hossamhassan47.quiz.helper;

import android.content.Context;

import com.wordpress.hossamhassan47.quiz.R;
import com.wordpress.hossamhassan47.quiz.model.Question;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonHelper {

    public static ArrayList<Question> ReadQuestions(Context context, String subject) {

        // Get questions data from res/raw/questions Json file based on subject
        InputStream inputStream;
        if (subject.equals("CSS")) {
            inputStream = context.getResources().openRawResource(R.raw.questions_css);
        } else if (subject.equals("HTML")) {
            inputStream = context.getResources().openRawResource(R.raw.questions_html);
        } else {
            inputStream = context.getResources().openRawResource(R.raw.questions_javascript);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Prepare the questions array list
        ArrayList<Question> questions = new ArrayList<>();

        try {
            // Parse the data into json object to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());

            // Get questions based on subject CSS, HTML, JavaScript...etc.
            JSONObject jObjectResult = jObject.getJSONObject(subject);
            JSONArray jArray = jObjectResult.getJSONArray("Questions");

            // Iterate through the questions
            for (int i = 0; i < jArray.length(); i++) {
                // Create question object
                Question question = new Question();

                // Set question details
                question.setId(jArray.getJSONObject(i).getString("Id"));
                question.setTitle(jArray.getJSONObject(i).getString("Title"));
                question.setOption1(jArray.getJSONObject(i).getString("Option1"));
                question.setOption2(jArray.getJSONObject(i).getString("Option2"));
                question.setOption3(jArray.getJSONObject(i).getString("Option3"));
                question.setOption4(jArray.getJSONObject(i).getString("Option4"));
                question.setCorrectAnswer(jArray.getJSONObject(i).getString("CorrectAnswer"));
                question.setType(jArray.getJSONObject(i).getString("Type"));
                question.setOptionsCount(jArray.getJSONObject(i).getString("OptionsCount"));

                // Add to question array list
                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Finally, return the questions array list
        return questions;
    }
}
