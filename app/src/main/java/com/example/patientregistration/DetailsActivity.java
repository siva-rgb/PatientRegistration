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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    EditText age,address,town,district,pincode,gender;
    Button submit;
    CheckBox accept;
    TextView clickcondition;
    String url = "https://bikashmysql.000webhostapp.com/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        age=findViewById(R.id.register_age);
        address=findViewById(R.id.register_address);
        town=findViewById(R.id.register_town);
        district=findViewById(R.id.register_district);
        pincode=findViewById(R.id.register_pincode);
        gender=findViewById(R.id.register_gender);

        submit=findViewById(R.id.submit);
        accept=findViewById(R.id.checkbox);
        clickcondition=findViewById(R.id.click_here);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getIntent_signUp=getIntent();
                final String name=getIntent_signUp.getStringExtra("name");
                final String phone=getIntent_signUp.getStringExtra("phone");
                final String email=getIntent_signUp.getStringExtra("email");
                final String password=getIntent_signUp.getStringExtra("password");
                        if (accept.isChecked())
                        {
                            Toast.makeText(DetailsActivity.this, "Please accept terms and condition before sign up ", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            final ProgressDialog progressDialog = new ProgressDialog(DetailsActivity.this);
                            progressDialog.setMessage("Please Wait..");


                            if (age.equals("")) {
                                Toast.makeText(DetailsActivity.this, "Enter you age", Toast.LENGTH_SHORT).show();
                            }
                            else if (address.getText().toString().equals("")) {
                                Toast.makeText(DetailsActivity.this, "Enter your address", Toast.LENGTH_SHORT).show();
                            }
                            else if (district.getText().toString().equals("")) {
                                Toast.makeText(DetailsActivity.this, "Enter your district", Toast.LENGTH_SHORT).show();
                            }
                            else if (town.getText().toString().equals("")) {
                                Toast.makeText(DetailsActivity.this, "Enter town ", Toast.LENGTH_SHORT).show();
                            }
                            else if (pincode.getText().toString().equals("")) {
                                Toast.makeText(DetailsActivity.this, "Enter your pincode", Toast.LENGTH_SHORT).show();
                            }
                            else {

                                progressDialog.show();
                               final String  str_Age = age.getText().toString().trim();
                               final String  str_Gender = gender.getText().toString().trim();
                               final String  str_Address = address.getText().toString().trim();
                               final String  str_District = district.getText().toString().trim();
                               final String  str_Town=town.getText().toString().trim();
                               final String  str_Pincode = pincode.getText().toString().trim();


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
                                            Intent intent=new Intent(DetailsActivity.this,HomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(DetailsActivity.this, response, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(DetailsActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();

                                        params.put("name", name);
                                        params.put("email", email);
                                        params.put("phone", phone);
                                        params.put("age", str_Age);
                                        params.put("gender", str_Gender);
                                        params.put("address", str_Address);
                                        params.put("district", str_District);
                                        params.put("town", str_Town);
                                        params.put("pincode", str_Pincode);
                                        params.put("password",password);
                                        return params;

                                    }
                                };

                                RequestQueue requestQueue = Volley.newRequestQueue(DetailsActivity.this);
                                requestQueue.add(request);


                            }
                        }

                    }

                });
            }


    }
