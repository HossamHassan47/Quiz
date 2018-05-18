package com.wordpress.hossamhassan47.quiz.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wordpress.hossamhassan47.quiz.activities.MainActivity;
import com.wordpress.hossamhassan47.quiz.R;
import com.wordpress.hossamhassan47.quiz.activities.QuizActivity;

public class StartQuizFragment extends DialogFragment {

    NoticeDialogListener mListener;
    String strQuizSubject;
    EditText txtUserName;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_start_quiz, null);

        strQuizSubject = getArguments().getString("quizSubject");

        // User Name
        txtUserName = (EditText)view.findViewById(R.id.edit_text_user_name);


        builder.setTitle(getResources().getString(R.string.dialog_title))
                .setView(view)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String userName = txtUserName.getText().toString();

                        if (userName == null || userName.isEmpty()) {
                            Toast.makeText(getActivity(), "Please enter your name.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(getContext(), QuizActivity.class);

                        intent.putExtra("userName", userName);
                        intent.putExtra("quizSubject", strQuizSubject);

                        startActivity(intent);

                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(StartQuizFragment.this);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StartQuizFragment.this.getDialog().cancel();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
