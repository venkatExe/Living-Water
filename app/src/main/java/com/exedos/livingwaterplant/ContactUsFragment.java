package com.exedos.livingwaterplant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class ContactUsFragment extends Fragment {


    public ContactUsFragment() {
        // Required empty public constructor
    }
    private Button CallBtn1, CallBtn2, ChatBtn, mailBtn;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        CallBtn1 = view.findViewById(R.id.contact_btn1);
        CallBtn2 = view.findViewById(R.id.contact_btn2);
        ChatBtn = view.findViewById(R.id.chat_with_Us_Btn);
        mailBtn = view.findViewById(R.id.mail_Us_Btn);


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
                Uri uri = Uri.parse("smsto:"+ "9666339067");
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


        return  view;
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
            getActivity().finish();
            Log.i("finished sending email", "");
        }
        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getContext(), "There is no email client installed", Toast.LENGTH_SHORT).show();
        }


    }
}