package com.law.rightive.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.law.rightive.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private Context context;
    private List<Date> data;
    private Calendar currentDate;
    private Calendar changeMonth;
    private CalendarAdapter.OnItemClickListener mListener = null;

    private int index = -1;
    private boolean selectCurrentDate = true;
    private int currentMonth;
    private int currentYear;
    private int currentDay;

    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;

    public CalendarAdapter(Context context, List<Date> data, Calendar currentDate, Calendar changeMonth) {
        this.context = context;
        this.data = data;
        this.currentDate = currentDate;
        this.changeMonth = changeMonth;

        currentMonth = currentDate.get(Calendar.MONTH);
        currentYear = currentDate.get(Calendar.YEAR);
        currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        selectedDay = changeMonth != null ? changeMonth.get(Calendar.DAY_OF_MONTH) : currentDay;
        selectedMonth = changeMonth != null ? changeMonth.get(Calendar.MONTH) : currentMonth;
        selectedYear = changeMonth != null ? changeMonth.get(Calendar.YEAR) : currentYear;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewHolder(inflater.inflate(R.layout.custom_calendar_day, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        cal.setTime(data.get(position));

        int displayMonth = cal.get(Calendar.MONTH);
        int displayYear = cal.get(Calendar.YEAR);
        int displayDay = cal.get(Calendar.DAY_OF_MONTH);

        try {
            Date dayInWeek = simpleDateFormat.parse(cal.getTime().toString());
            simpleDateFormat.applyPattern("EEE");
            holder.txtDayInWeek.setText(simpleDateFormat.format(dayInWeek));
            int s = cal.get(Calendar.DAY_OF_MONTH);
            holder.txtDate.setText(String.valueOf(s));
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }

//        if (displayYear >= currentYear)
//            if (displayMonth >= currentMonth || displayYear > currentYear)
//                if (displayDay >= currentDay || displayMonth > currentMonth || displayYear > currentYear) {
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = holder.getAdapterPosition();
                selectCurrentDate = false;
                holder.onItemClick(index);
                notifyDataSetChanged();
            }
        });

        if (index == holder.getAdapterPosition())
            makeItemSelected(holder);
        else {
            if (displayDay == selectedDay
                    && displayMonth == selectedMonth
                    && displayYear == selectedYear
                    && selectCurrentDate)
                makeItemSelected(holder);
            else
                makeItemDefault(holder);
        }
//                } else makeItemDefault(holder);
//            else makeItemDefault(holder);
//        else makeItemDefault(holder);

    }

//    private void makeItemDisabled(ViewHolder holder) {
//        holder.txtDate.setTextColor(ContextCompat.getColor(context, R.color.copper_text_color_30));
//        holder.txtDayInWeek.setTextColor(ContextCompat.getColor(context, R.color.copper_text_color_30));
//        holder.linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_deselect));
//        holder.linearLayout.setEnabled(false);
//    }

    private void makeItemDefault(ViewHolder holder) {
        holder.txtDate.setTextColor(Color.parseColor("#cdcdce"));
        holder.txtDayInWeek.setTextColor(Color.parseColor("#cdcdce"));
        holder.linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_deselect));
        holder.linearLayout.setEnabled(true);
    }

    private void makeItemSelected(ViewHolder holder) {
        holder.txtDate.setTextColor(Color.parseColor("#34955e"));
        holder.txtDayInWeek.setTextColor(Color.parseColor("#34955e"));
        holder.linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.calendar_select));
        holder.linearLayout.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(CalendarAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener {
        public CalendarAdapter.OnItemClickListener listener;
        TextView txtDate;
        TextView txtDayInWeek;
        LinearLayout linearLayout;
        ImageView calImageView;

        private ViewHolder(@NonNull View itemView, CalendarAdapter.OnItemClickListener listener) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtDayInWeek = itemView.findViewById(R.id.txt_day);
            txtDayInWeek.setAllCaps(false);
            linearLayout = itemView.findViewById(R.id.calendar_linear_layout);
            calImageView = itemView.findViewById(R.id.calImageView);
            this.listener = listener;
        }

        @Override
        public void onItemClick(int position) {
        }
    }
}
