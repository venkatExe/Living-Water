package com.exedos.livingwater;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    private Button viewAllAddressBtn, signOutBtn;

    public static final int MANAGE_ADDRESS = 1;

    private CircleImageView profileImage;
    private TextView name, email;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.profile_username);
        email = view.findViewById(R.id.profile_user_email);


        signOutBtn = view.findViewById(R.id.sign_out_btn);
        Button deleteMyAccountBtn = view.findViewById(R.id.delete_my_account_btn);

        name.setText(DbQueries.fullname);
        email.setText(DbQueries.email);
//        if(!DbQueries.profile.equals("")){
//            Glide.with(getContext()).load(DbQueries.profile).apply(new RequestOptions().placeholder(R.drawable.baseline_person_24)).into(profileImage);
//        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(getActivity(), "No User Found", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DbQueries.fullname = task.getResult().getString("fullname");
                                DbQueries.email = task.getResult().getString("email");
                                //DbQueries.profile = task.getResult().getString("profile");

                                name.setText(DbQueries.fullname);
                                email.setText(DbQueries.email);

                                //// code for profile image
                        /* if(DBqueries.profile.equals("")){
                             addProfileIcon.setVisibility(View.VISIBLE);
                         }else {
                             addProfileIcon.setVisibility(View.INVISIBLE);
                             Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_person_24)).into(profileImage);
                         }
                        */
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


        //viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);
     /*   viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressItent = new Intent(getContext(), MyAddressActivity.class);
                myAddressItent.putExtra("MODE",MANAGE_ADDRESS);
                startActivity(myAddressItent);

            }
        });
*/

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
                startActivity(registerIntent);
                getActivity().finish();
            }
        });

        // Handle delete account button click
        deleteMyAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });


        return view;
    }

    private void deleteAccount() {
        if (currentUser == null) {
            Toast.makeText(getActivity(), "No user is signed in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Confirm deletion with a dialog
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userId = currentUser.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Remove user data from Firestore first
                        db.collection("USERS")
                                .document(userId)
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("DeleteAccount", "Firestore document deleted successfully");

                                            // Now delete the user's Firebase Authentication account
                                            currentUser.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // Sign out from Firebase Authentication
                                                                FirebaseAuth.getInstance().signOut();
                                                                Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                                                // Redirect to the register activity
                                                                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
                                                                startActivity(registerIntent);
                                                                getActivity().finish();
                                                            } else {
                                                                // Log the error and show a message
                                                                String errorMessage = "Failed to delete Firebase Authentication account: " + task.getException().getMessage();
                                                                Log.e("DeleteAccount", errorMessage);
                                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            // Log the error and show a message
                                            String errorMessage = "Failed to delete Firestore document: " + task.getException().getMessage();
                                            Log.e("DeleteAccount", errorMessage);
                                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}