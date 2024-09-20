package com.exedos.livingwaterplant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Contact_Page extends AppCompatActivity {

    private Button CallBtn1, CallBtn2, ChatBtn, mailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        CallBtn1 = findViewById(R.id.contact_btn1);
        CallBtn2 = findViewById(R.id.contact_btn2);
        ChatBtn = findViewById(R.id.chat_with_Us_Btn);
        mailBtn = findViewById(R.id.mail_Us_Btn);

        CallBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: (+91)9666339067"));
                startActivity(intent);

            }
        });


        CallBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: (+91)8465911545"));
                startActivity(intent);

            }
        });

        ChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:"+ "8465911545");
                Intent i=new Intent(Intent.ACTION_SENDTO,uri);
                i.setPackage("com.whatsapp");
                startActivity(i);
            }
        });

        mailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });


//        return setContentView();
    }

    private void sendEmail() {
        Log.i("sendEmail","");
        String[] TO = {"livingwaterplant@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mail to:"));
        emailIntent.setType("Text//plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(emailIntent, "send mail..."));
            finish();
            Log.i("finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(this, "There is no email client installed", Toast.LENGTH_SHORT).show();
        }


    }
}
