package com.example.messengerapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.messengerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;


public class PhoneAuthFragment extends Fragment {

    MaterialEditText phone_number;
    Button sms_request_btn;
    PhoneAuthProvider auth_phone;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_auth, container, false);
        phone_number = view.findViewById(R.id.phone_number);
        sms_request_btn = view.findViewById(R.id.sms_request_btn);

        auth_phone = PhoneAuthProvider.getInstance();

//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
//
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                String code = phoneAuthCredential.getSmsCode();
//                if (code != null) {
//                    editTextCode.setText(code);
//                    //verifying the code
//                    verifyVerificationCode(code);
//                }
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e) {
//
//            }
//        };
//
//        sms_request_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String txt_phone = phone_number.getText().toString();
//                if (TextUtils.isEmpty(txt_phone)){
//                    Toast.makeText(getContext(), "Enter your phone number!", Toast.LENGTH_SHORT).show();
//                }
//
//                else {
//                    auth_phone.verifyPhoneNumber(txt_phone, 60, TimeUnit.SECONDS, this, mCallbacks);
//                }
//            }
//        });
        return view;
    }
}