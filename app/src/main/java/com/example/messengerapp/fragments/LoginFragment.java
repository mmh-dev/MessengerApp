package com.example.messengerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messengerapp.MainActivity;
import com.example.messengerapp.PhoneAuth;
import com.example.messengerapp.R;
import com.example.messengerapp.ui.main.SectionsPagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {
    MaterialEditText email_l, password_l;
    EditText phone_number;
    String code;
    Button signIn_btn, sms_request_btn;
    FirebaseAuth auth_l;
    PhoneAuthProvider phoneAuthProvider;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        email_l = view.findViewById(R.id.email_login);
        password_l = view.findViewById(R.id.password_login);
        signIn_btn = view.findViewById(R.id.signIn_btn);
        phone_number = view.findViewById(R.id.phone_number);
        sms_request_btn = view.findViewById(R.id.sms_request_btn);

        auth_l = FirebaseAuth.getInstance();
        phoneAuthProvider = PhoneAuthProvider.getInstance();

        signIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email_l.getText().toString();
                String txt_password = password_l.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getContext(), "Complete all fields!", Toast.LENGTH_SHORT).show();
                }

                else {
                    auth_l.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(getContext(), "You have been successfully signed in!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                    }
                                }
                            });
                }
            }
        });

        final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.i("Code", phoneAuthCredential.getSmsCode());
        }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
                mResendToken = forceResendingToken;
                Toast.makeText(getContext(), "Code has been sent!", Toast.LENGTH_SHORT).show();
                Log.i("Code", mVerificationId);
//                String code = phoneAuthCredential.getSmsCode();
                InputSmsFragment inputSmsFragment = new InputSmsFragment();

                Bundle bundle = new Bundle();
                bundle.putString("verificationId", mVerificationId);
//                bundle.putString("сode", code);
                inputSmsFragment.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.view_pager, inputSmsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };

        sms_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_phone_number = phone_number.getText().toString().trim();
                if (TextUtils.isEmpty(txt_phone_number) || txt_phone_number.length() < 9) {
                    Toast.makeText(getContext(), "Enter a valid phone number!", Toast.LENGTH_SHORT).show();
                } else {
                    phoneAuthProvider.verifyPhoneNumber("+996" + txt_phone_number, 60, TimeUnit.SECONDS, getActivity(), mCallbacks);
                }
            }
        });
        return view;
    }


}