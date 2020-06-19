package com.example.patientregistration;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView change_language;
    Button btn_login;
    Button btn_signUp;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null)
        {
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "No current user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocal();
        setContentView(R.layout.activity_main);

        btn_login=findViewById(R.id.login);
        change_language=findViewById(R.id.txt_languageChange);
        btn_signUp=findViewById(R.id.sign_up);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        change_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });
    }

    private void showLanguageDialog() {

       // final String[] laguage_item ={"english","ଓଡିଆ","তারা ছিল না"};

        final String [] laguage_item={"english","odiya","bengali"};
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose language");
        builder.setSingleChoiceItems(laguage_item, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which==0)
                {
                    setLocal("en");
                    recreate();
                }
                else if (which==1)
                {
                    setLocal("od");
                    recreate();
                }

                else if (which==2)
                {
                    setLocal("ben");
                    recreate();
                }
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void setLocal(String lan) {
        Locale locale=new Locale(lan);
        Locale.setDefault(locale);
        Configuration configuration= new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getSharedPreferences("Setting",MODE_PRIVATE).edit();
        editor.putString("My_lang",lan);
        editor.apply();
    }

    private void loadLocal()
    {
//        SharedPreferences preferences= getPreferences(MODE_PRIVATE);
//        String language=preferences.getString("My_lang","");
//        setLocal(language);

        String data=getApplicationContext().getSharedPreferences("Setting",MODE_PRIVATE).getString("My_lang","none");
        setLocal(data);
    }
}
