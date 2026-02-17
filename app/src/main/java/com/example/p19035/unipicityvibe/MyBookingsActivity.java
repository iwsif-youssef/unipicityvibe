package com.example.p19035.unipicityvibe;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.*;

import adapters.BookingListAdapter;
import models.Booking;

public class MyBookingsActivity extends BaseActivity {

    ListView listView;
    List<Booking> bookingList;
    BookingListAdapter adapter;

    FirebaseFirestore db;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_bookings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.bookingsListView);
        bookingList = new ArrayList<>();
        adapter = new BookingListAdapter(this, bookingList);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadMyBookings();
    }

    private void loadMyBookings() {

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, getString(R.string.not_logged_in), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String email = auth.getCurrentUser().getEmail();

        //Get booked events from database based on the users email
        db.collection("bookings")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(query -> {
                    bookingList.clear();
                    for (DocumentSnapshot doc : query) {
                        Booking booking = doc.toObject(Booking.class);
                        bookingList.add(booking);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                getString(R.string.booking_load_error),
                                Toast.LENGTH_LONG).show());
    }
}
