package com.example.p19035.unipicityvibe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import adapters.EventListAdapter;
import models.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class EventsActivity extends AppCompatActivity {

    ListView listView;
    EventListAdapter adapter;
    List<Event> eventList;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.eventsListView);
        eventList = new ArrayList<>();
        adapter = new EventListAdapter(this, eventList);
        listView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadEvents();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Event event = eventList.get(position);

            Intent intent = new Intent(EventsActivity.this, EventDetailsActivity.class);
            intent.putExtra("EVENT_ID", event.getId());
            startActivity(intent);
        });
    }

    private void loadEvents() {
        db.collection("events")
                .get()
                .addOnSuccessListener(query -> {
                    eventList.clear();
                    for (DocumentSnapshot doc : query) {

                        Event event = doc.toObject(Event.class);

                        if (event != null) {
                            event.setId(doc.getId());
                            eventList.add(event);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Σφάλμα φόρτωσης",
                                Toast.LENGTH_SHORT).show());
    }
}
