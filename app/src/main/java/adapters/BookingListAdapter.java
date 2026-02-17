package adapters;

import android.content.Context;
import android.view.*;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.*;

import models.Booking;

//Insert each booked event as an item in MyBookings Activity
public class BookingListAdapter extends ArrayAdapter<Booking> {

    public BookingListAdapter(Context context, List<Booking> bookings) {
        super(context, 0, bookings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Booking booking = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView line1 = convertView.findViewById(android.R.id.text1);
        TextView line2 = convertView.findViewById(android.R.id.text2);

        line1.setText("ID: " + booking.getEventId());

        String date = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date(booking.getTimestamp()));

        line2.setText(date);

        return convertView;
    }
}
