package com.example.bistos.myvet;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.realm.Realm;

public class SignUp extends AppCompatActivity {

    Button mSignUpButton;

    private FirebaseAuth mAuth;
    private  FirebaseAuth.AuthStateListener mAuthLisner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSignUpButton = (Button) findViewById(R.id.signUpSubmit_button);

        mAuth=FirebaseAuth.getInstance();

        mAuthLisner=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user !=null){
                    Log.d("---------------","user id signed out");
                }
                else {
                    // User is signed out
                    Log.d("--------------------", "onAuthStateChanged:signed_out");
                }
            }
        };
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

                    int a=3;
                    Log.d("a=",a+" ");
                    tryLogin(email,password);



                }
            }

            private void tryLogin(String email,String password) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("--------------", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    startActivity(new Intent(SignUp.this,DoctorListActivity.class));
                                }

                                // ...
                            }
                        });
            }
        });


    }






}
