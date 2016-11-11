package com.example.bistos.myvet;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSignUpButton = (Button) findViewById(R.id.signUpSubmit_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                //intent.setType("text/plain");
                //intent.setType(Intent.)
                EditText editText=(EditText)findViewById(R.id.name_editText);
                String name=editText.getText().toString();
                EditText surnameText=(EditText)findViewById(R.id.surname_editText);
                String surname=surnameText.getText().toString();
                EditText email_editText=(EditText)findViewById(R.id.email_editText);
                String email=email_editText.getText().toString();
                EditText password_editText=(EditText)findViewById(R.id.password_editText);
                String password=password_editText.getText().toString();
                EditText password1_editText=(EditText)findViewById(R.id.confirmPassword_editText);
                String password1=password1_editText.getText().toString();
                String data=name+" "+surname+" "+email+" "+password+" "+password1;
                //String surname=
                String[] adresses=new String[1];
                adresses[0]="roxana.anghel11@gmail.com";
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,adresses);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Test");
                intent.putExtra(Intent.EXTRA_TEXT,data);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(Intent.createChooser(intent,"Sent Email"));

                }
            }
        });
    }






}
