package com.law.rightive.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.law.rightive.R;
import com.law.rightive.calendar.CalendarActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private Context context;
    private static List<Date> data;
    private static Calendar currentDate;
    static private Calendar changeMonth;
    static private Calendar selectedDate;
    static private CalendarAdapter.OnItemClickListener mListener = null;

    private static int index = -1;
    private boolean selectCurrentDate = true;
    private int currentMonth;
    private int currentYear;
    private int currentDay;

    private static int selectedDay;
    private static int selectedMonth;
    private static int selectedYear;

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
            assert dayInWeek != null;
            holder.txtDayInWeek.setText(simpleDateFormat.format(dayInWeek));
            int s = cal.get(Calendar.DAY_OF_MONTH);
            holder.txtDate.setText(String.valueOf(s));
        } catch (ParseException ex) {
            Log.v("Exception", ex.getLocalizedMessage());
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = holder.getAdapterPosition();
                selectCurrentDate = false;
                holder.onItemClick(context, index);
                notifyDataSetChanged();
            }
        });

        if (index == holder.getAdapterPosition()) {
            makeItemSelected(holder);
        } else {
            if (displayDay == selectedDay
                    && displayMonth == selectedMonth
                    && displayYear == selectedYear
                    && selectCurrentDate)
                makeItemSelected(holder);
            else
                makeItemDefault(holder);
        }
    }

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
        void onItemClick(Context context, int position);
    }

    public void setOnItemClickListener(CalendarAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener {
        public CalendarAdapter.OnItemClickListener listener;
        TextView txtDate;
        TextView txtDayInWeek;
        LinearLayout linearLayout;

        private ViewHolder(@NonNull View itemView, CalendarAdapter.OnItemClickListener listener) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtDayInWeek = itemView.findViewById(R.id.txt_day);
            txtDayInWeek.setAllCaps(false);
            linearLayout = itemView.findViewById(R.id.calendar_linear_layout);
            this.listener = listener;
        }

        @Override
        public void onItemClick(Context context, int position) {
            CalendarActivity.fetchEvents(position + 1, selectedMonth, selectedYear);
//            Toast.makeText(context, "Toast Made!!!" + index, Toast.LENGTH_SHORT).show();
        }
    }
}
