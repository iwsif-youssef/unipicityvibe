package com.example.p19035.unipicityvibe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    EditText emailEditText;
    Spinner themeSpinner, fontSpinner;
    Button saveButton;
    Switch geoSwitch;

    SharedPreferences settingsPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.settingsEmailEditText);
        themeSpinner = findViewById(R.id.settingsThemeSpinner);
        fontSpinner = findViewById(R.id.settingsFontSpinner);
        saveButton = findViewById(R.id.settingsSaveButton);
        geoSwitch = findViewById(R.id.geolocationSwitch);

        SharedPreferences userPrefs =
                getSharedPreferences("user", MODE_PRIVATE);

        boolean loggedIn = userPrefs.getBoolean("logged_in", false);


        settingsPrefs = getSharedPreferences("settings", MODE_PRIVATE);

        setupSpinners();
        loadSettings();
        loadUserEmail();


        saveButton.setOnClickListener(v ->{
            saveSettings();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
        });


        boolean enabled = settingsPrefs.getBoolean("notifications_enabled", true);

        if (!loggedIn) {
            geoSwitch.setChecked(false);
            geoSwitch.setEnabled(false);
        } else {
            geoSwitch.setChecked(enabled);
            geoSwitch.setEnabled(true);

            geoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                settingsPrefs.edit()
                        .putBoolean("notifications_enabled", isChecked)
                        .apply();
            });
        }


    }

    private void setupSpinners() {

        ArrayAdapter<String> themeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Light", "Dark"}
        );
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(themeAdapter);

        ArrayAdapter<String> fontAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Small", "Medium", "Large"}
        );
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSpinner.setAdapter(fontAdapter);
    }

    private void loadSettings() {
        themeSpinner.setSelection(settingsPrefs.getInt("theme", 0));
        fontSpinner.setSelection(settingsPrefs.getInt("font", 1));
    }

    private void loadUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            emailEditText.setText(user.getEmail());
        }
    }

    private void saveSettings() {

        settingsPrefs.edit()
                .putInt("theme", themeSpinner.getSelectedItemPosition())
                .putInt("font", fontSpinner.getSelectedItemPosition())
                .apply();

        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
    }


}