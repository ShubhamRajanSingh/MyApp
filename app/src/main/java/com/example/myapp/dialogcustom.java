package com.example.myapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class dialogcustom extends DialogFragment {

EditText text;
Button add;
private dialogCustomListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogtask, null);
        builder.setView(view);
        text=view.findViewById(R.id.dialog_editText);
        add=view.findViewById(R.id.dialog_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=text.getText().toString();
                listener.applyText(s,false);
                 getDialog().dismiss();

            }
        });



        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener=(dialogCustomListener)context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface dialogCustomListener{
        void applyText(String task,Boolean done);

    }
}
