package com.exedos.livingwaterplant;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FrameLayout frameLayout;

    public static boolean onResetPasswordFragment = false;
    public static boolean setSignUpFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frameLayout = findViewById(R.id.register_frame_layout);

        if (setSignUpFragment) {
            setSignUpFragment = false;
            setDefaultFragment(new SignUpFragment());

        } else {
            setDefaultFragment(new SignInFragment());
        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){

            SignInFragment.disableCloseBtn = false;
            SignUpFragment.disableCloseBtn = false;

            if (onResetPasswordFragment){
                onResetPasswordFragment = false;
                setFragment(new SignInFragment());

                return false;

            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setDefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser1 = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser1 == null){
            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent2 = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent2);
            finish();
        }

    }



}