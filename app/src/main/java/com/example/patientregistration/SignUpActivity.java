package com.example.patientregistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    EditText sign_name,sign_email,sign_password,sign_phone,otp;
    Button signUp,generate_otp;
    TextView go_login;

    Button gle_signIn;
    FirebaseAuth auth;
    String codeSent;

    GoogleSignInClient signInClient;
    private final static int RC_SIGN_IN=2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_name=findViewById(R.id.register_fullname);
        sign_email=findViewById(R.id.register_email);
        sign_password=findViewById(R.id.register_password);
        sign_phone=findViewById(R.id.register_phone);
        signUp=findViewById(R.id.register);
        otp=findViewById(R.id.register_otp);
        generate_otp=findViewById(R.id.generate_otp);
        go_login=findViewById(R.id.txt_login);
        gle_signIn=findViewById(R.id.google_signIn);



        auth=FirebaseAuth.getInstance();

        generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

        go_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sign_name.getText().toString().equals(""))
                {
                    Toast.makeText(SignUpActivity.this, "Please provide your name", Toast.LENGTH_SHORT).show();
                }
                else if (sign_email.getText().toString().equals(""))
                {
                    Toast.makeText(SignUpActivity.this, "Enter you email", Toast.LENGTH_SHORT).show();
                }
                else if (sign_password.getText().toString().equals(""))
                {
                    Toast.makeText(SignUpActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifySigninCode();
                }
            }
        });

        createRequest();
        
        gle_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        
    }

    private void createRequest() {

        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient= GoogleSignIn.getClient(this,signInOptions);

    }

    private void signIn() {

        Intent signInIntent=signInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void verifySigninCode() {
        String str_otp=otp.getText().toString();
        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(codeSent,str_otp);

        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            if (user!=null)
                            {
                                Intent intent=new Intent(SignUpActivity.this,GoogleSignUpDetailActivity.class);
                                startActivity(intent);
                            }

                        } else {

                            Toast.makeText(SignUpActivity.this, "Login cannot be successful", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String str_name=sign_name.getText().toString();
                            String str_email=sign_email.getText().toString();
                            String str_password=sign_password.getText().toString();
                            String phone=sign_phone.getText().toString();
                            Intent intent=new Intent(SignUpActivity.this,DetailsActivity.class);
                            intent.putExtra("name",str_name);
                            intent.putExtra("email",str_email);
                            intent.putExtra("password",str_password);
                            intent.putExtra("phone",phone);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(SignUpActivity.this, "Sign Up failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void sendVerificationCode() {

        String phoneNumber= sign_phone.getText().toString();
        if (phoneNumber.isEmpty())
        {
            Toast.makeText(this, "Please provide phone number", Toast.LENGTH_SHORT).show();
            sign_phone.requestFocus();
            return;
        }
        if (phoneNumber.length()<10)
        {
            Toast.makeText(this, "Phone number is invalid", Toast.LENGTH_SHORT).show();
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this,mCallbacks);

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSent=s;
        }
    };
}
