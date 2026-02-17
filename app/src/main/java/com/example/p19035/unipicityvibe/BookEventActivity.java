package com.example.p19035.unipicityvibe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class BookEventActivity extends BaseActivity {

    EditText usernameEditText, emailEditText;
    Button confirmButton;

    FirebaseFirestore db;
    FirebaseAuth auth;
    String eventId;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_event);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        confirmButton = findViewById(R.id.confirmBookingButton);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            userEmail = user.getEmail();
            emailEditText.setText(userEmail);

            emailEditText.setEnabled(false);
        }

        eventId = getIntent().getStringExtra("EVENT_ID");

        if (eventId == null) {
            Toast.makeText(this, getString(R.string.event_error), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        confirmButton.setOnClickListener(v -> completeBooking());
    }

    private void completeBooking() {

        String username = usernameEditText.getText().toString().trim();

        if (username.isEmpty()) {
            Toast.makeText(this, getString(R.string.empty_username_error), Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("bookings")
                .whereEqualTo("email", userEmail)
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(query -> {

                    //Check if ticket exists
                    if (!query.isEmpty()) {

                        Toast.makeText(this,
                                getString(R.string.existing_booking),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    //Save to database
                    saveBooking(username);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                getString(R.string.booking_check_error),
                                Toast.LENGTH_LONG).show());
    }

    private void saveBooking(String username) {

        Map<String, Object> booking = new HashMap<>();
        booking.put("username", username);                      //Save insertet Username
        booking.put("email", userEmail);                        //Save user Email
        booking.put("eventId", eventId);                        //Save selected event ID
        booking.put("timestamp", System.currentTimeMillis());   //Save when the booking was done

        //Add to database
        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this,
                            getString(R.string.booking_complete),
                            Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                getString(R.string.booking_error),
                                Toast.LENGTH_LONG).show());
    }


}
