package com.irimurielle.messagingapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.irimurielle.messagingapp.R;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.createUserButton)
    Button mCreateUserButton;
    @BindView(R.id.nameEditText)
    EditText mNameEditText;
    @BindView(R.id.emailEditText)
    EditText mEmailEditText;
    @BindView(R.id.passwordEditText)
    EditText mPasswordEditText;
    @BindView(R.id.confirmPasswordEditText)
    EditText mConfirmPasswordEditText;
    @BindView(R.id.loginTextView)
    TextView mLoginTextView;

    private FirebaseAuth auth;
    private ProgressDialog dialog;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        mLoginTextView.setOnClickListener(this);
        mCreateUserButton.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        createAuthProgressDialog();
    }

    public void createAuthProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Authenticating with Firebase...");
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        if (view == mLoginTextView) {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        if (view == mCreateUserButton) {
            createNewUser();
        }
    }

    public void createNewUser() {
        final String name = mNameEditText.getText().toString().trim();
        final String email = mEmailEditText.getText().toString().trim();
        final String password = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(name);
        boolean validPassword = isValidPassword(password, confirmPassword);

        if (!validEmail || !validName || !validPassword) return;
        dialog.show();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getInstance().getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("id", userid);
                    map.put("name", name);
                    map.put("email", email);
                    map.put("imageUrl", "default");
                    map.put("status", "offline");
                    map.put("search", name.toLowerCase());

                    reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail = (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPasswordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

}