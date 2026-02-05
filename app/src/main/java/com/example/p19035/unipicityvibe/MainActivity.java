package com.example.p19035.unipicityvibe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    Button myBookingsButton;
    Button logoutButton;
    Button viewEventsButton;

    Button loginButton;
    Button registerButton;

    Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        int theme = prefs.getInt("theme", 0);

        if (theme == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();
        myBookingsButton = findViewById(R.id.mainMyBookingsButton);
        logoutButton = findViewById(R.id.mainLogoutButton);

        loginButton = findViewById(R.id.mainLoginButton);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton = findViewById(R.id.mainRegisterButton);
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        viewEventsButton = findViewById(R.id.mainViewEvents);
        viewEventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventsActivity.class);
            startActivity(intent);
        });

        Button bookingsButton = findViewById(R.id.mainMyBookingsButton);
        bookingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyBookingsActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            finish();
        });

        settingsButton = findViewById(R.id.mainSettingsButton);

        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });


    }
    @Override
    protected void onResume() {
        super.onResume();

        myBookingsButton = findViewById(R.id.mainMyBookingsButton);
        logoutButton = findViewById(R.id.mainLogoutButton);
        viewEventsButton = findViewById(R.id.mainViewEvents);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            myBookingsButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            viewEventsButton.setVisibility(View.GONE);


            loginButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);
        } else {
            myBookingsButton.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            viewEventsButton.setVisibility(View.VISIBLE);


            loginButton.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);
        }
    }

}