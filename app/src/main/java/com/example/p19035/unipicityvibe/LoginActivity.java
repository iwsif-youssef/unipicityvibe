package com.example.p19035.unipicityvibe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {

    EditText emailET;
    EditText passwordET;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.emailEditText2);
        passwordET = findViewById(R.id.passwordEditText2);

        findViewById(R.id.LoginButton2).setOnClickListener(v -> {
            auth.signInWithEmailAndPassword(
                    emailET.getText().toString(),
                    passwordET.getText().toString()
            ).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    SharedPreferences prefs =
                            getSharedPreferences("user", MODE_PRIVATE);

                    prefs.edit()
                            .putBoolean("logged_in", true)
                            .putString("email", emailET.getText().toString())
                            .apply();

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this,
                            getString(R.string.login_failed),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        findViewById(R.id.signUpTextView1).setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class))
        );
    }
}