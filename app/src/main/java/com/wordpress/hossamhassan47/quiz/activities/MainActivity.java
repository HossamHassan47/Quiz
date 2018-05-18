package com.wordpress.hossamhassan47.quiz.activities;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wordpress.hossamhassan47.quiz.R;
import com.wordpress.hossamhassan47.quiz.fragments.NoticeDialogListener;
import com.wordpress.hossamhassan47.quiz.fragments.StartQuizFragment;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements NoticeDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // css
        TextView css = (TextView) findViewById(R.id.text_view_css);
        css.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                StartQuizFragment fragment = new StartQuizFragment();
                Bundle bundle = new Bundle();
                bundle.putString("quizSubject", "css");
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "dialog_StartQuizFragment");

            }
        });

        // html
        TextView html = (TextView) findViewById(R.id.text_view_html);
        html.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                StartQuizFragment fragment = new StartQuizFragment();
                Bundle bundle = new Bundle();
                bundle.putString("quizSubject", "html");
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "dialog_StartQuizFragment");

            }
        });

        // javascript
        TextView javascript = (TextView) findViewById(R.id.text_view_javascript);
        javascript.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                StartQuizFragment fragment = new StartQuizFragment();
                Bundle bundle = new Bundle();
                bundle.putString("quizSubject", "javascript");
                fragment.setArguments(bundle);
                fragment.show(getSupportFragmentManager(), "dialog_StartQuizFragment");

            }
        });
    }
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
