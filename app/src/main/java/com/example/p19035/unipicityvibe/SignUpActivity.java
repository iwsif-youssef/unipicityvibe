package com.example.p19035.unipicityvibe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends BaseActivity {

    EditText emailET;
    EditText passwordET;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.emailEditText1);
        passwordET = findViewById(R.id.passwordEditText1);

        findViewById(R.id.SignUpButton).setOnClickListener(v -> {
            String email = emailET.getText().toString();
            String pass = passwordET.getText().toString();

            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            startActivity(new Intent(this, MainActivity.class));
                            finish();

                        } else {

                            String message;

                            if (task.getException() instanceof
                                    com.google.firebase.auth.FirebaseAuthWeakPasswordException) {

                                message = getString(R.string.error_weak_password);

                            } else if (task.getException() instanceof
                                    com.google.firebase.auth.FirebaseAuthUserCollisionException) {

                                message = getString(R.string.error_email_exists);

                            } else if (task.getException() instanceof
                                    com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {

                                message = getString(R.string.error_invalid_email);

                            } else {
                                message = getString(R.string.error_signup_generic);
                            }

                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        }

            });
        });
    }
}