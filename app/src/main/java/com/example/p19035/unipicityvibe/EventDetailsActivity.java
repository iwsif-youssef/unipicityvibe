package com.example.p19035.unipicityvibe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import models.Event;
import com.google.firebase.firestore.FirebaseFirestore;


public class EventDetailsActivity extends AppCompatActivity {

    TextView titleTv, dateTv, priceTv, descriptionTv;
    Button bookBtn;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        titleTv = findViewById(R.id.titleTv2);
        dateTv = findViewById(R.id.dateTv2);
        priceTv = findViewById(R.id.priceTv2);
        descriptionTv = findViewById(R.id.descriptionTv2);
        bookBtn = findViewById(R.id.bookBtn);

        db = FirebaseFirestore.getInstance();

        String eventId = getIntent().getStringExtra("EVENT_ID");

        if (eventId == null) {
            finish();
            return;
        }

        loadEventDetails(eventId);

        bookBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookEventActivity.class);
            intent.putExtra("EVENT_ID", eventId);
            startActivity(intent);
        });

    }

    private void loadEventDetails(String eventId) {

        db.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener(doc -> {

                    Event event = doc.toObject(Event.class);
                    if (event != null) {
                        titleTv.setText(event.getTitle());
                        dateTv.setText(event.getDateTime());
                        priceTv.setText(event.getPrice() + " €");
                        descriptionTv.setText(event.getDescription());
                    }
                });
    }
}
