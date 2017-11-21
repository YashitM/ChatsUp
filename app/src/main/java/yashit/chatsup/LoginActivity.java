package yashit.chatsup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.LENGTH_LONG;

public class LoginActivity extends AppCompatActivity {

    //EditText Definition
    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.emailLoginPage);
        passwordEditText = (EditText) findViewById(R.id.passwordLoginPage);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null) {
            //If user is already Logged In.
            startActivity(new Intent(LoginActivity.this, HomePage.class));
            finish();
        }
    }

    public void loginFunction(View view) {
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setTitle("Loading");
        pd.setMessage("Please Wait...");
        pd.show();
        String emailID = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        mAuth.signInWithEmailAndPassword(emailID, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                        } else {
                            pd.dismiss();
                            Intent intent = new Intent(LoginActivity.this, HomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    public void registerFunction(View view) {
        startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        finish();
    }
}
