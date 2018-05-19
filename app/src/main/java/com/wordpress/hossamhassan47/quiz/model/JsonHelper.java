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

    public void ReadQuestions(Context context) {
//        {
//            "Categories": {
//            "Category": [
//            {
//                "cat_id": "3",
//                    "cat_name": "test"
//            },
//            {
//                "cat_id": "4",
//                    "cat_name": "test1"
//            },
//            {
//                "cat_id": "5",
//                    "cat_name": "test2"
//            },
//            {
//                "cat_id": "6",
//                    "cat_name": "test3"
//            }
//        ]
//        }
//        }

        //Get Data From Text Resource File Contains Json Data.
        InputStream inputStream = context.getResources().openRawResource(R.raw.css);
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

        Log.v("Text Data", byteArrayOutputStream.toString());

        try {

            // Parse the data into jsonobject to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());

            JSONObject jObjectResult = jObject.getJSONObject("CSS");
            JSONArray jArray = jObjectResult.getJSONArray("Questions");

            ArrayList<Question> questions = new ArrayList<>();

            for (int i = 0; i < jArray.length(); i++) {
                Question question = new Question();

                question.Id = jArray.getJSONObject(i).getString("Id");
                question.Title = jArray.getJSONObject(i).getString("Title");
                questions.add(question);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
