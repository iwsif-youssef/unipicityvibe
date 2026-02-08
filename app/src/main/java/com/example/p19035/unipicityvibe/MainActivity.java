package com.example.p19035.unipicityvibe;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Event;

public class MainActivity extends AppCompatActivity {

    //Authentication
    FirebaseAuth auth;

    //Buttons
    Button myBookingsButton;
    Button logoutButton;
    Button viewEventsButton;
    Button loginButton;
    Button registerButton;
    Button settingsButton;

    //Geolocation AND Notifications
    FusedLocationProviderClient fusedLocationClient;
    private static final float RADIUS_METERS = 200f;
    private static final int LOCATION_PERMISSION_REQUEST = 1001;
    private static final int NOTIFICATION_PERMISSION_REQUEST = 2001;
    List<Event> eventList = new ArrayList<>();
    FirebaseFirestore db;
    private static final String CHANNEL_ID = "events_channel";
    Set<String> notifiedEventIds = new HashSet<>();


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

        //Geolocation AND Notifications
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        db = FirebaseFirestore.getInstance();
        loadEvents();
        createNotificationChannel();

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

    //Geolocation Methods
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            getUserLocation();

        }
        if (requestCode == 1001 &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }

    private void loadEvents() {

        db.collection("events")
                .get()
                .addOnSuccessListener(query -> {

                    eventList.clear();

                    for (DocumentSnapshot doc : query) {
                        Event event = doc.toObject(Event.class);
                        event.setId(doc.getId());
                        eventList.add(event);
                    }

                    getUserLocation();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Σφάλμα φόρτωσης εκδηλώσεων",
                                Toast.LENGTH_SHORT).show());
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
    }

    private void getUserLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }

        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {

                        if (location != null) {

                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            checkNearbyEvents(lat, lon);
                        }
                    });

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }



    private float distanceInMeters(double userLat, double userLon, double eventLat, double eventLon) {

        Location userLocation = new Location("user");
        userLocation.setLatitude(userLat);
        userLocation.setLongitude(userLon);

        Location eventLocation = new Location("event");
        eventLocation.setLatitude(eventLat);
        eventLocation.setLongitude(eventLon);

        return userLocation.distanceTo(eventLocation);
    }

    private void checkNearbyEvents(double userLat, double userLon) {

        for (Event event : eventList) {

            float distance = distanceInMeters(
                    userLat,
                    userLon,
                    event.getLatitude(),
                    event.getLongitude()
            );

            if (distance <= RADIUS_METERS) {
                showEventNotification(event, distance);
            }
        }
    }
/*
    private void requestNotificationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_REQUEST
                );
            }
        }
    }
*/
    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Nearby Events",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for nearby events");

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

    private void showEventNotification(Event event, float distance) {

        if (notifiedEventIds.contains(event.getId())) {
            return;
        }

        notifiedEventIds.add(event.getId());

        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtra("EVENT_ID", event.getId());

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                event.getId().hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
                return;
            }
        }

        Notification notification =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Κοντινή Εκδήλωση")
                        .setContentText(
                                event.getTitle() + " (" + (int) distance + "m)"
                        )
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .build();

        NotificationManagerCompat.from(this)
                .notify(event.getId().hashCode(), notification);
    }


}