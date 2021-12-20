package com.law.rightive.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.law.rightive.R;
import com.law.rightive.calendar.CalendarActivity;
import com.law.rightive.utils.EventsUtils;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    ArrayList<EventsUtils> eventsUtilsArrayList;
    Context context;
    static private EventsAdapter.OnItemClickListener mListener = null;

    public EventsAdapter(ArrayList<EventsUtils> list, Context context) {
        this.eventsUtilsArrayList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsAdapter.EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_material_card_view, parent, false);
        return new EventsAdapter.EventsViewHolder(view, mListener);
    }


    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.EventsViewHolder holder, int position) {
        if (eventsUtilsArrayList.size() > 0) {
            EventsUtils eventsUtils = eventsUtilsArrayList.get(position);
            holder.titleCRN.setText(eventsUtils.getCrn());
            holder.clientName.setText(eventsUtils.getClientName());
            holder.description.setText(eventsUtils.getDescription());
            CalendarActivity.events_progress_bar.setVisibility(View.GONE);
            CalendarActivity.events_noDataFound.setVisibility(View.GONE);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.onItemClick(context, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventsUtilsArrayList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Context context, int position);
    }

    public void setOnItemClickListener(EventsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener {
        public EventsAdapter.OnItemClickListener listener;
        TextView titleCRN, clientName, description;
        LinearLayout linearLayout;

        private EventsViewHolder(@NonNull View itemView, EventsAdapter.OnItemClickListener listener) {
            super(itemView);
            titleCRN = itemView.findViewById(R.id.titleCRN);
            clientName = itemView.findViewById(R.id.clientName);
            description = itemView.findViewById(R.id.description);
            linearLayout = itemView.findViewById(R.id.event_liner_layout);
            this.listener = listener;
        }

        @Override
        public void onItemClick(Context context, int position) {
//            if (imageForegroundLayout.getVisibility() == View.VISIBLE) {
//                imageForegroundLayout.setVisibility(View.INVISIBLE);
//            } else {
//                imageForegroundLayout.setVisibility(View.VISIBLE);
//            }
        }
    }
}
