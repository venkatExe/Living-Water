package com.exedos.livingwaterplant;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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

    // Initialize dialogView here
    private View dialogView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.profile_username);
        email = view.findViewById(R.id.profile_user_email);

        // Initialize dialogView here
        dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_reauth, null);
        final EditText emailInput = dialogView.findViewById(R.id.emailInput);
        final EditText passwordInput = dialogView.findViewById(R.id.passwordInput);

        signOutBtn = view.findViewById(R.id.sign_out_btn);
     //   viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);
        Button deleteMyAccountBtn = view.findViewById(R.id.delete_my_account_btn);

        name.setText(DbQueries.fullname);
        email.setText(DbQueries.email);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

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

                                name.setText(DbQueries.fullname);
                                email.setText(DbQueries.email);

                                // Code for profile image (commented out)
                                /*
                                if(DbQueries.profile.equals("")){
                                    addProfileIcon.setVisibility(View.VISIBLE);
                                } else {
                                    addProfileIcon.setVisibility(View.INVISIBLE);
                                    Glide.with(MainActivity.this).load(DbQueries.profile).apply(new RequestOptions().placeholder(R.drawable.ic_baseline_person_24)).into(profileImage);
                                }
                                */
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        // Uncommented code is left as is

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
        // Show a dialog to prompt the user for their email and password
        new AlertDialog.Builder(requireContext())
                .setTitle("Reauthenticate")
                .setView(dialogView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    
                    public void onClick(DialogInterface dialog, int which) {
                        String email = ((EditText) dialogView.findViewById(R.id.emailInput)).getText().toString().trim();
                        String password = ((EditText) dialogView.findViewById(R.id.passwordInput)).getText().toString().trim();

                        if (email.isEmpty() || password.isEmpty()) {
                            Toast.makeText(requireContext(), "Email and Password are required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                        // Re-authenticate the user with the entered credentials
                        currentUser.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("DeleteAccount", "User re-authenticated successfully.");
                                            // Delete user's data from Firestore before deleting the authentication account
                                            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                                                    .delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                // Now delete the user's Firebase Authentication account
                                                                currentUser.delete()
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    FirebaseAuth.getInstance().signOut();
                                                                                    Toast.makeText(requireContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                                                                                    Intent registerIntent = new Intent(requireContext(), RegisterActivity.class);
                                                                                    startActivity(registerIntent);
                                                                                    requireActivity().finish();
                                                                                } else {
                                                                                    String errorMessage = "Failed to delete Firebase Authentication account: " + task.getException().getMessage();
                                                                                    Log.e("DeleteAccount", errorMessage);
                                                                                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                            } else {
                                                                String errorMessage = "Failed to delete user data from Firestore: " + task.getException().getMessage();
                                                                Log.e("DeleteAccount", errorMessage);
                                                                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            String errorMessage = "Failed to re-authenticate: " + task.getException().getMessage();
                                            Log.e("DeleteAccount", errorMessage);
                                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    //last modification


//    private void deleteAccount() {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (currentUser == null) {
//            Toast.makeText(getActivity(), "No user is signed in", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        new AlertDialog.Builder(getContext())
//                .setTitle("Delete Account")
//                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
//                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String userId = currentUser.getUid();
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//                        // Delete user data from Firestore, including any subcollections
//                        deleteDocumentAndSubcollections(db, "USERS", userId, new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d("DeleteAccount", "Firestore document and subcollections deleted successfully");
//
//                                    // Now delete the user's Firebase Authentication account
//                                    currentUser.delete()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//                                                        FirebaseAuth.getInstance().signOut();
//                                                        Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
//
//                                                        Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
//                                                        startActivity(registerIntent);
//                                                        getActivity().finish();
//                                                    } else {
//                                                        String errorMessage = "Failed to delete Firebase Authentication account: " + task.getException().getMessage();
//                                                        Log.e("DeleteAccount", errorMessage);
//                                                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                            });
//                                } else {
//                                    String errorMessage = "Failed to delete Firestore document: " + task.getException().getMessage();
//                                    Log.e("DeleteAccount", errorMessage);
//                                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }
//
//    private void deleteDocumentAndSubcollections(FirebaseFirestore db, String collection, String documentId, OnCompleteListener<Void> onCompleteListener) {
//        DocumentReference docRef = db.collection(collection).document(documentId);
//
//        // Get all subcollections
//        docRef.getCollections().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Iterable<CollectionReference> subcollections = task.getResult();
//
//                List<Task<Void>> deletionTasks = new ArrayList<>();
//
//                for (CollectionReference subcollection : subcollections) {
//                    // Get all documents in the subcollection and delete them
//                    deletionTasks.add(subcollection.get().continueWithTask(querySnapshotTask -> {
//                        for (DocumentSnapshot document : querySnapshotTask.getResult()) {
//                            document.getReference().delete();
//                        }
//                        return null;
//                    }));
//                }
//
//                // After all subcollections are deleted, delete the main document
//                Tasks.whenAllComplete(deletionTasks).addOnCompleteListener(subtask -> {
//                    docRef.delete().addOnCompleteListener(onCompleteListener);
//                });
//            } else {
//                // Handle error in retrieving subcollections
//                String errorMessage = "Failed to retrieve subcollections: " + task.getException().getMessage();
//                Log.e("DeleteAccount", errorMessage);
//                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

// perfectly working but id still there


//    private void deleteAccount() {
//        // Get the currently signed-in user
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (currentUser == null) {
//            Toast.makeText(getActivity(), "No user is signed in", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Confirm deletion with a dialog
//        new AlertDialog.Builder(getContext())
//                .setTitle("Delete Account")
//                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
//                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String userId = currentUser.getUid();
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//                        // First, remove user data from Firestore
//                        db.collection("USERS")
//                                .document(userId)
//                                .delete()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Log.d("DeleteAccount", "Firestore document deleted successfully");
//
//                                            // Now delete the user's Firebase Authentication account
//                                            currentUser.delete()
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                                // Sign out from Firebase Authentication
//                                                                FirebaseAuth.getInstance().signOut();
//                                                                Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
//
//                                                                // Redirect to the register activity
//                                                                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
//                                                                startActivity(registerIntent);
//                                                                getActivity().finish();
//                                                            } else {
//                                                                // Log the error and show a message
//                                                                String errorMessage = "Failed to delete Firebase Authentication account: " + task.getException().getMessage();
//                                                                Log.e("DeleteAccount", errorMessage);
//                                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
//                                        } else {
//                                            // Log the error and show a message
//                                            String errorMessage = "Failed to delete Firestore document: " + task.getException().getMessage();
//                                            Log.e("DeleteAccount", errorMessage);
//                                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//
//
//
//    }

//    private void deleteAccount() {
//        if (currentUser == null) {
//            Toast.makeText(getActivity(), "No user is signed in", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//
//
//        // Confirm deletion with a dialog
//        new AlertDialog.Builder(getContext())
//                .setTitle("Delete Account")
//                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
//                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String userId = currentUser.getUid();
//                        FirebaseFirestore db = FirebaseFirestore.getInstance();
//                        DocumentReference userDocrf = db.collection("USERS").document(userId).delete();
//
//                        // Remove user data from Firestore first
//                        db.collection("USERS")
//                                .document(userId)
//                                .delete()
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Log.d("DeleteAccount", "Firestore document deleted successfully");
//
//                                            // Now delete the user's Firebase Authentication account
//                                            currentUser.delete()
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                                // Sign out from Firebase Authentication
//                                                                FirebaseAuth.getInstance().signOut();
//                                                                Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
//
//                                                                // Redirect to the register activity
//                                                                Intent registerIntent = new Intent(getContext(), RegisterActivity.class);
//                                                                startActivity(registerIntent);
//                                                                getActivity().finish();
//                                                            } else {
//                                                                // Log the error and show a message
//                                                                String errorMessage = "Failed to delete Firebase Authentication account: " + task.getException().getMessage();
//                                                                Log.e("DeleteAccount", errorMessage);
//                                                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
//                                        } else {
//                                            // Log the error and show a message
//                                            String errorMessage = "Failed to delete Firestore document: " + task.getException().getMessage();
//                                            Log.e("DeleteAccount", errorMessage);
//                                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }

//   const uidToDelete = 'the-uid-to-delete';
//
//// Query for the document where the userId field matches the UID
//const query = firebase.firestore().collection('your-collection').where('userId', '==', uidToDelete);
//
//query.get().then((querySnapshot) => {
//        querySnapshot.forEach((doc) => {
//        // Get the document reference
//    const docRef = doc.ref;
//
//        // Delete the document
//        docRef.delete().then(() => {
//                console.log('Document successfully deleted!');
//    }).catch((error) => {
//                console.error('Error removing document: ', error);
//    });
//  });
//    }).catch((error) => {
//        console.error('Error getting documents: ', error);
//    });
//
}