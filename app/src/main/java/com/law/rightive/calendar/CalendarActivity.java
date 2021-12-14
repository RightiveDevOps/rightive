package com.law.rightive.calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.law.rightive.R;
import com.law.rightive.adapters.CalendarAdapter;
import com.law.rightive.adapters.EventsAdapter;
import com.law.rightive.fragments.EventAddFragment;
import com.law.rightive.utils.EventsUtils;
import com.law.rightive.utils.FireBaseUtils;
import com.law.rightive.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import club.cred.synth.views.SynthImageButton;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity {

    private final Calendar lastDayInCalendar = Calendar.getInstance();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy\nMMMM", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);

    private final Calendar currentDate = Calendar.getInstance(Locale.ENGLISH);
    private final int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
    private final int currentMonth = currentDate.get(Calendar.MONTH);
    private final int currentYear = currentDate.get(Calendar.YEAR);
    RecyclerView recyclerView;
    FloatingActionButton addEventFAB, addEventForCRN, addCustomEvent;
    TextView addEventForCRNText, addCustomEventText;

    boolean isAllFabsVisible;

    TextView textView;
    int selectedDay = currentDay;
    int selectedMonth = currentMonth;
    int selectedYear = currentYear;

    Calendar changeMonth;
    List<Date> dates = new ArrayList<>();

    EventsAdapter eventsAdapter;

    private DatabaseReference databaseReference;
    private RecyclerView eventsRecyclerView;
    private ArrayList<EventsUtils> utilsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        SynthImageButton calendar_prev_button = findViewById(R.id.calendar_prev_button);
        SynthImageButton calendar_next_button = findViewById(R.id.calendar_next_button);

        recyclerView = findViewById(R.id.calendar_recycler_view);
        addEventFAB = findViewById(R.id.event_add_fab);
        addEventForCRN = findViewById(R.id.add_event_for_case);
        addCustomEvent = findViewById(R.id.add_custom_event_fab);
        addEventForCRNText = findViewById(R.id.add_event_for_case_text);
        addCustomEventText = findViewById(R.id.add_custom_event_fab_text);

        addEventForCRN.setVisibility(View.GONE);
        addCustomEvent.setVisibility(View.GONE);
        addEventForCRNText.setVisibility(View.GONE);
        addCustomEventText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        lastDayInCalendar.roll(Calendar.MONTH, 365);
        setUpCalendar(currentDate);

        calendar_prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendar(changeMonth = cal);
            }
        });

        calendar_next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendar(changeMonth = cal);
            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CalendarActivity.this, R.style.ThemeOverlay_App_DatePicker, pickerListener, selectedYear, selectedMonth, selectedDay);
                datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeMonth = currentDate;
                        setUpCalendar(currentDate);
                    }
                });
                datePickerDialog.show();
            }
        });

        //FLOATING ACTION BUTTON

        addEventFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllFabsVisible) {
                    addEventForCRN.show();
                    addCustomEvent.show();
                    addEventForCRNText.setVisibility(View.VISIBLE);
                    addCustomEventText.setVisibility(View.VISIBLE);
                    isAllFabsVisible = true;
                } else {
                    addEventForCRN.hide();
                    addCustomEvent.hide();
                    addEventForCRNText.setVisibility(View.GONE);
                    addCustomEventText.setVisibility(View.GONE);
                    isAllFabsVisible = false;
                }
            }
        });

        addEventForCRN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventAddFragment eventAddFragment = new EventAddFragment();
                eventAddFragment.show(getSupportFragmentManager(), "Event Add Bottom Sheet");
            }
        });

        //EVENTS FROM FIREBASE -> CARD LINEAR LAYOUT

        databaseReference = FireBaseUtils.getInstance().getFirebaseDatabase().getReference().child("EVENTS");
        databaseReference.keepSynced(true);

        eventsRecyclerView = findViewById(R.id.events_recycler_view);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventsAdapter = new EventsAdapter(utilsArrayList, CalendarActivity.this);
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsAdapter.notifyDataSetChanged();
//        loadRecyclerViewData();
        fetchEvents();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isAllFabsVisible) {
            addEventForCRN.show();
            addCustomEvent.show();
            addEventForCRNText.setVisibility(View.VISIBLE);
            addCustomEventText.setVisibility(View.VISIBLE);
            isAllFabsVisible = true;
        } else {
            addEventForCRN.hide();
            addCustomEvent.hide();
            addEventForCRNText.setVisibility(View.GONE);
            addCustomEventText.setVisibility(View.GONE);
            isAllFabsVisible = false;
        }

    }

    private void fetchEvents() {
        FireBaseUtils
                .getInstance()
                .getFirebaseFirestore()
                .collection(StringUtils.FIRESTORE_EVENTS)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            utilsArrayList.clear();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot dataSnapshot : list) {
                                EventsUtils eventsUtils = dataSnapshot.toObject(EventsUtils.class);
                                utilsArrayList.add(eventsUtils);
                            }
                            eventsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CalendarActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadRecyclerViewData() {
        FireBaseUtils
                .getInstance()
                .getFirebaseFirestore()
                .collection(StringUtils.FIRESTORE_EVENTS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot dataSnapshot : list) {
                                EventsUtils eventsUtils = dataSnapshot.toObject(EventsUtils.class);
                                utilsArrayList.add(eventsUtils);
                            }
                            eventsAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CalendarActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CalendarActivity.this, "Fail to get the data.\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            changeMonth = cal;
            changeMonth.set(year, month, dayOfMonth);
            setUpCalendar(changeMonth);
        }
    };

    private void setUpCalendar(Calendar cal) {
        textView = findViewById(R.id.txt_current_month);
        textView.setText(sdf.format(cal.getTime()).toLowerCase());

        Calendar monthCalendar = (Calendar) cal.clone();
        int maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        selectedDay = changeMonth != null ? changeMonth.get(Calendar.DAY_OF_MONTH) : currentDay;
        selectedMonth = changeMonth != null ? changeMonth.get(Calendar.MONTH) : currentMonth;
        selectedYear = changeMonth != null ? changeMonth.get(Calendar.YEAR) : currentYear;

        int currentPosition = 0;
        dates.clear();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        while (dates.size() < maxDaysInMonth) {
            // get position of selected day
            if (monthCalendar.get(Calendar.DAY_OF_MONTH) == selectedDay)
                currentPosition = dates.size();
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CalendarAdapter calendarAdapter = new CalendarAdapter(this, dates, currentDate, changeMonth);
        recyclerView.setAdapter(calendarAdapter);

        if (currentPosition > 2) {
            recyclerView.scrollToPosition(currentPosition - 3);
        } else if (maxDaysInMonth - currentPosition < 2) {
            recyclerView.scrollToPosition(currentPosition);
        } else {
            recyclerView.scrollToPosition(currentPosition);
        }

        calendarAdapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Calendar clickCalendar = Calendar.getInstance();
                clickCalendar.setTime(dates.get(position));
                selectedDay = clickCalendar.get(Calendar.DAY_OF_MONTH);
            }
        });
    }
}