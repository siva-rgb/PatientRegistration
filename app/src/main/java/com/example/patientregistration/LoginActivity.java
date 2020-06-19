package com.example.patientregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login;
    TextView go_signup;
    String str_email,str_password;
    String url = "https://bikashmysql.000webhostapp.com/loginPatient.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.login_email);
        password=findViewById(R.id.login_password);
        login=findViewById(R.id.btn_login);
        go_signup=findViewById(R.id.txt_signUp);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginPatient();
            }
        });

        go_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

    }

    private void loginPatient() {

        if(email.getText().toString().equals("")){
            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
        }
        else if(password.getText().toString().equals("")){
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
        }
        else{


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait..");

            progressDialog.show();

            str_email = email.getText().toString().trim();
            str_password = password.getText().toString().trim();


            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    if(response.equalsIgnoreCase("Logged In Successfully")){

                        email.setText("");
                        password.setText("");
                        Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    }

                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("email",str_email);
                    params.put("password",str_password);
                    return params;

                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(request);

        }
    }

}
