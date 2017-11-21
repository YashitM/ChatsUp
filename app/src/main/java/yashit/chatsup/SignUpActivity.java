package yashit.chatsup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;

import yashit.chatsup.DataObjects.UserProfile;

public class SignUpActivity extends AppCompatActivity implements IPickResult {

    private EditText emailID;
    private EditText password;
    private EditText retypePassword;
    private EditText fullName;
    private Bitmap bitmap;

    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private StorageReference mStorage;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //To hide the toolbar
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();


        emailID = (EditText) findViewById(R.id.emailSignUpPage);
        password = (EditText) findViewById(R.id.passwordSignUpPage);
        retypePassword = (EditText) findViewById(R.id.retypePasswordSignUpPage);
        fullName = (EditText) findViewById(R.id.fullNameSignUpPage);
    }

    public void loginFunction(View view) {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    public void signUpFunction(View view) {
        final String emailString = emailID.getText().toString();
        String passwordString = password.getText().toString();
        String retypePasswordString = retypePassword.getText().toString();
        final String fullNameString = fullName.getText().toString();

        if(retypePasswordString.equals(passwordString)) {
            if(passwordString.length()<=6) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    password.setError("Password should be atleast 6 characters long.",getDrawable(R.drawable.ic_error));
                }
                else {
                    password.setError("Password should be atleast 6 characters long.");
                }
            }
            else {
                //Password is valid
                auth.createUserWithEmailAndPassword(emailString, passwordString)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user = auth.getCurrentUser();
                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                    myRef = mFirebaseDatabase.getReference();
                                    mStorage = FirebaseStorage.getInstance().getReference();
                                    userID = user.getUid();
                                    final String[] url = {"https://firebasestorage.googleapis.com/v0/b/chatsup-e7003.appspot.com/o/ProfilePhotos%2Fprofile_placeholder.png?alt=media&token=abf5f7b4-0745-4e9a-985f-d6089ce7d4f2"};
                                    if(bitmap==null) {
                                        //if the user hasn't selected any profile image
                                        UserProfile userInformation = new UserProfile(fullNameString, emailString, url[0]);
                                        myRef.child("users").child(userID).setValue(userInformation);
                                        startActivity(new Intent(SignUpActivity.this, HomePage.class));
                                        finish();
                                    }
                                    else {
                                        Uri thumbUri = getImageUri(SignUpActivity.this, bitmap);
                                        StorageReference filePath = mStorage.child("ProfilePhotos").child(userID + "ProfileImage");
                                        filePath.putFile(thumbUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(SignUpActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                                url[0] = downloadUrl.toString();
                                                UserProfile userInformation = new UserProfile(fullNameString, emailString, url[0]);
                                                myRef.child("users").child(userID).setValue(userInformation);
                                                startActivity(new Intent(SignUpActivity.this, HomePage.class));
                                                finish();
                                            }
                                        });
                                    }
                                }
                            }
                        });
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                password.setError("Passwords do not match",getDrawable(R.drawable.ic_error));
            }
            else {
                password.setError("Passwords do not match");
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void pickImage(View view) {
        PickImageDialog.build(new PickSetup()).show(this);
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            bitmap = r.getBitmap();
            ImageView lendAddItemIV = (ImageView) findViewById(R.id.profile_image);
            lendAddItemIV.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
