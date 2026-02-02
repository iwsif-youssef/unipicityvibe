package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.p19035.unipicityvibe.R;
import models.Event;

import java.util.List;

public class EventListAdapter extends ArrayAdapter<Event> {

    public EventListAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.event_item, parent, false);
        }

        Event event = getItem(position);

        TextView titleTv = convertView.findViewById(R.id.titleTv);
        TextView dateTv = convertView.findViewById(R.id.dateTv);
        TextView priceTv = convertView.findViewById(R.id.priceTv);


        titleTv.setText(event.getTitle());
        dateTv.setText(event.getDateTime());
        priceTv.setText(event.getPrice() + " €");


        return convertView;
    }
}

