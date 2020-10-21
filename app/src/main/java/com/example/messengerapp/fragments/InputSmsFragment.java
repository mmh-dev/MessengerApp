package com.example.messengerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messengerapp.MainActivity;
import com.example.messengerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;


public class InputSmsFragment extends Fragment {

    EditText code_field;
    Button sms_signIn_btn;
    String code;
    String mVerificationId;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_input_sms, container, false);
        code_field = view.findViewById(R.id.sms_code);
        sms_signIn_btn = view.findViewById(R.id.sms_signIn_btn);
        auth = FirebaseAuth.getInstance();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
//            HashMap<String, Object> map = (HashMap<String, Object>) bundle.getSerializable("map");
            code = bundle.getString("code");
            mVerificationId = bundle.getString("verificationId");
            code_field.setText(code);
        }

        sms_signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getContext(), "You have been successfully signed in!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                }
                            }
                        });
            }
        });

        return view;
    }
}