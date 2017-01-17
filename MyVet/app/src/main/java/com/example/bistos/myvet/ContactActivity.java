package com.example.bistos.myvet;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ContactActivity extends AppCompatActivity {

    Button mButonSend;
    EditText mEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mButonSend=(Button)findViewById(R.id.buttonSend);
        mButonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                String data=mEditText.getText().toString();
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
        mEditText=(EditText)findViewById(R.id.edit_contact);
    }
}
