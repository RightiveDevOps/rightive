package com.law.rightive.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.law.rightive.R;
import com.law.rightive.utils.EventsUtils;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    ArrayList<EventsUtils> eventsUtilsArrayList;
    Context context;
    int index = 0;
    LottieAnimationView events_progress_bar;

    public EventsAdapter(ArrayList<EventsUtils> list, Context context) {
        this.eventsUtilsArrayList = list;
        this.context = context;
        this.events_progress_bar = ((Activity) context).findViewById(R.id.events_progress_bar);
    }

    @NonNull
    @Override
    public EventsAdapter.EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_material_card_view, parent, false);
        return new EventsAdapter.EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.EventsViewHolder holder, int position) {
        EventsUtils eventsUtils = eventsUtilsArrayList.get(position);
        holder.titleCRN.setText(eventsUtils.getCrn());
        holder.clientName.setText(eventsUtils.getClientName());
        holder.description.setText(eventsUtils.getDescription());
        events_progress_bar.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return eventsUtilsArrayList.size();
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {
        TextView titleCRN, clientName, description;

        private EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleCRN = itemView.findViewById(R.id.titleCRN);
            clientName = itemView.findViewById(R.id.clientName);
            description = itemView.findViewById(R.id.description);
        }
    }
}
