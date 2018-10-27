package com.demo.mdb.spring2017finalassessment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class RegisterActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    ImageView imageView;
    Uri selectedImageUri;
    FirebaseAuth mAuth;
    EditText name;

    StorageReference mStorageRef;

    boolean imageChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        final Button createNewUser = (Button) findViewById(R.id.createnewuser);
        name = (EditText) findViewById(R.id.name);
        final EditText email = (EditText) findViewById(R.id.email2);
        final EditText password = (EditText) findViewById(R.id.password2);
        final EditText confirmPassword = (EditText) findViewById(R.id.confirmpassword);
        imageView = (ImageView) findViewById(R.id.imageView);

        imageChosen = false;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        createNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!imageChosen) {
                    Toast.makeText(RegisterActivity.this, "No image chosen", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                    createUser(email.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /* TODO Part 2
        * Implement registration. If the imageView is clicked, set it to an image from the gallery
        * and store the image as a Uri instance variable (also change the imageView's image to this
        * Uri. If the create new user button is pressed, call createUser using the email and password
        * from the edittexts. Remember that it's email2 and password2 now!
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            imageChosen = true;
        }
    }

    private void createUser(final String email, final String password) {
        /* TODO Part 2.1
         * This part's long, so listen up!
         * Create a user, and if it fails, display a Toast.
         *
         * If it works, we're going to add their image to the database. To do this, we will need a
         * unique user id to identify the user (push isn't the best answer here. Do some Googling!)
         *
         * Now, if THAT works (storing the image), set the name and photo uri of the user (hint: you
         * want to update a firebase user's profile.)
         *
         * Finally, if updating the user profile works, go to the TabbedActivity
         */
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("BOB", "createUserWithEmail:success");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference();
                            final String key = ref.child("images").push().getKey();
                            uploadImage(selectedImageUri, key);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("BOB", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    private void uploadImage(Uri filePath, String key) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = mStorageRef.child("images/"+ key);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name.getText().toString())
                                            .setPhotoUri(uri)
                                            .build();

                                    FirebaseUser user = mAuth.getCurrentUser();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        startActivity(new Intent(RegisterActivity.this, TabbedActivity.class));
                                                    }
                                                }
                                            });

                                }});

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

}
