package com.wordpress.hossamhassan47.quiz.model;

import android.content.Context;
import android.util.Log;

import com.wordpress.hossamhassan47.quiz.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonHelper {

    public static ArrayList<Question> ReadQuestions(Context context, String subject) {

        //Get Data From Text Resource File Contains Json Data.
        InputStream inputStream = context.getResources().openRawResource(R.raw.questions);
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

        //Log.v("Text Data", byteArrayOutputStream.toString());

        ArrayList<Question> questions = new ArrayList<>();

        try {

            // Parse the data into json object to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());

            JSONObject jObjectResult = jObject.getJSONObject(subject);

            JSONArray jArray = jObjectResult.getJSONArray("Questions");

            for (int i = 0; i < jArray.length(); i++) {
                Question question = new Question();

                question.Id = jArray.getJSONObject(i).getString("Id");
                question.Title = jArray.getJSONObject(i).getString("Title");
                question.Option1 = jArray.getJSONObject(i).getString("Option1");
                question.Option2 = jArray.getJSONObject(i).getString("Option2");
                question.Option3 = jArray.getJSONObject(i).getString("Option3");
                question.Option4 = jArray.getJSONObject(i).getString("Option4");
                question.CorrectAnswer = jArray.getJSONObject(i).getString("CorrectAnswer");
                question.Type = jArray.getJSONObject(i).getString("Type");
                question.OptionsCount = jArray.getJSONObject(i).getString("OptionsCount");

                questions.add(question);

                //Log.v("Id", question.Id);
                //Log.v("Title", question.Title);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }
}
