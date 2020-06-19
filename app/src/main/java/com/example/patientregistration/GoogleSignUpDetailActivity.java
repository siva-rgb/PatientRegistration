package com.example.patientregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.HashMap;
import java.util.Map;

public class GoogleSignUpDetailActivity extends AppCompatActivity {

    TextView retrieve_name,retrieve_email;

    EditText gel_phoneNumber,gle_Age,gle_gender,gle_Address,gle_Town,gle_District,gle_pinCode;
    CheckBox acceptTerm;
    Button gle_submit;

    String str_name,str_email,str_Phone,str_Age,str_Gender,str_Address,str_District,str_Town,str_Pincode;
    String url = "https://bikashmysql.000webhostapp.com/registerPatient.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_up_detail);

        retrieve_name=findViewById(R.id.retrieve_name);
        retrieve_email=findViewById(R.id.retrieve_email);
        gel_phoneNumber=findViewById(R.id.gle_register_phone);
        gle_Age=findViewById(R.id.gle_register_age);
        gle_gender=findViewById(R.id.gle_register_gender);
        gle_Address=findViewById(R.id.gle_register_address);
        gle_District=findViewById(R.id.gle_register_district);
        gle_Town=findViewById(R.id.gle_register_town);
        gle_pinCode=findViewById(R.id.gle_register_pincode);
        acceptTerm=findViewById(R.id.gle_checkbox);

        gle_submit=findViewById(R.id.gle_submit);

        GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount!=null)
        {
            retrieve_name.setText(signInAccount.getDisplayName());
            retrieve_email.setText(signInAccount.getEmail());
        }


        gle_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!acceptTerm.isChecked())
                {
                    Toast.makeText(GoogleSignUpDetailActivity.this, "Please accept terms", Toast.LENGTH_SHORT).show();
                }
                else
                {


                    final ProgressDialog progressDialog = new ProgressDialog(GoogleSignUpDetailActivity.this);
                    progressDialog.setMessage("Please Wait..");

                    if (gel_phoneNumber.getText().toString().equals("")) {
                        Toast.makeText(GoogleSignUpDetailActivity.this, "If not Present, Type NONE", Toast.LENGTH_SHORT).show();
                    }
                    else if (gle_Age.getText().toString().equals("")) {
                        Toast.makeText(GoogleSignUpDetailActivity.this, "If not Present, Type NONE", Toast.LENGTH_SHORT).show();
                    }
                    else if (gle_gender.getText().toString().equals("")) {
                        Toast.makeText(GoogleSignUpDetailActivity.this, "If not Present, Type NONE", Toast.LENGTH_SHORT).show();
                    }
                    else if (gle_Address.getText().toString().equals("")) {
                        Toast.makeText(GoogleSignUpDetailActivity.this, "Email is Mandatory", Toast.LENGTH_SHORT).show();
                    }
                    else if (gle_District.getText().toString().equals("")) {
                        Toast.makeText(GoogleSignUpDetailActivity.this, "Mobile No. is Mandatory", Toast.LENGTH_SHORT).show();
                    }
                    else if (gle_Town.getText().toString().equals("")) {
                        Toast.makeText(GoogleSignUpDetailActivity.this, "Account No. is Mandatory", Toast.LENGTH_SHORT).show();
                    }
                    else if (gle_pinCode.getText().toString().equals("")) {
                        Toast.makeText(GoogleSignUpDetailActivity.this, "Working Hours are Mandatory", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        progressDialog.show();
                        str_name = retrieve_name.getText().toString().trim();
                        str_email = retrieve_email.getText().toString().trim();
                        str_Phone = gel_phoneNumber.getText().toString().trim();
                        str_Age = gle_Age.getText().toString().trim();
                        str_Gender = gle_gender.getText().toString().trim();
                        str_Address = gle_Address.getText().toString().trim();
                        str_District = gle_District.getText().toString().trim();
                        str_Town=gle_Town.getText().toString().trim();
                        str_Pincode = gle_pinCode.getText().toString().trim();


                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressDialog.dismiss();
//                                ed_username.setText("");
//                                ed_speciality.setText("");
//                                ed_qualification.setText("");
//                                ed_designation.setText("");
//                                ed_institution.setText("");
//                                ed_email.setText("");
//                                ed_mobile.setText("");
//                                ed_account.setText("");
//                                ed_hours.setText("");
//                                ed_password.setText("");
//                                ed_confirmp.setText("");
                                if (response.equalsIgnoreCase("Registered Successfully"))
                                {
                                    Intent intent=new Intent(GoogleSignUpDetailActivity.this,HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(GoogleSignUpDetailActivity.this, response, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Toast.makeText(GoogleSignUpDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        ) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("name", str_name);
                                params.put("email", str_email);
                                params.put("phone", str_Phone);
                                params.put("age", str_Age);
                                params.put("gender", str_Gender);
                                params.put("address", str_Address);
                                params.put("district", str_District);
                                params.put("town", str_Town);
                                params.put("pincode", str_Pincode);
                                return params;

                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(GoogleSignUpDetailActivity.this);
                        requestQueue.add(request);


                    }
                }


            }
        });

    }
}
