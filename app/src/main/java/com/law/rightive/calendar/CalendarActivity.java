package com.law.rightive.calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
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

    public static LottieAnimationView events_noDataFound;
    public static LottieAnimationView events_progress_bar;

    boolean isAllFabsVisible;

    TextView textView;
    int selectedDay = currentDay;
    int selectedMonth = currentMonth;
    int selectedYear = currentYear;

    public static FragmentManager fragmentManager;

    Calendar changeMonth;
    List<Date> dates = new ArrayList<>();

    private static EventsAdapter eventsAdapter;

    private DatabaseReference databaseReference;
    private RecyclerView eventsRecyclerView;
    private static ArrayList<EventsUtils> utilsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        SynthImageButton calendar_prev_button = findViewById(R.id.event_dialog_close_button);
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

        events_noDataFound = findViewById(R.id.events_noDataFound);
        events_progress_bar = findViewById(R.id.events_progress_bar);

        fragmentManager = getSupportFragmentManager();

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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            private final ColorDrawable background = new ColorDrawable(getResources().getColor(R.color.rally_green_500));

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;
                float newDx = dX;
                if (newDx >= (float) itemView.getWidth() / 3) {
                    newDx = (float) itemView.getWidth() / 3;
                }
                else if(newDx < (float) -itemView.getWidth() / 3)
                {
                    newDx = (float) -itemView.getWidth() / 3;
                }

/*              if (dX > 0) {
                    background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int) dX), itemView.getBottom());
                } else if (dX < 0) {
                    background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                } else {
                    background.setBounds(100, 0, 100, 0);
                }
                background.draw(c);
 */
                Bitmap icon;

                super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive);
            }

            @Override
            public int convertToAbsoluteDirection(int flags, int layoutDirection) {
                return super.convertToAbsoluteDirection(flags, layoutDirection);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);

        //EVENTS FROM FIREBASE -> CARD LINEAR LAYOUT
        databaseReference = FireBaseUtils.getInstance().getFirebaseDatabase().getReference().child("EVENTS");
        databaseReference.keepSynced(true);
        eventsRecyclerView = findViewById(R.id.events_recycler_view);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        events_noDataFound.setVisibility(View.GONE);
        eventsAdapter = new EventsAdapter(utilsArrayList, CalendarActivity.this);
        eventsRecyclerView.setAdapter(eventsAdapter);
        eventsAdapter.notifyDataSetChanged();
        itemTouchHelper.attachToRecyclerView(eventsRecyclerView);
        fetchEvents(selectedDay, selectedMonth, selectedYear);
    }

    public static void fetchEvents(int day, int month, int year) {
        month = month + 1;
        events_progress_bar.setVisibility(View.VISIBLE);
        events_progress_bar.playAnimation();
        FireBaseUtils
                .getInstance()
                .getFirebaseFirestore()
                .collection(StringUtils.FIRESTORE_EVENTS)
                .document(StringUtils.FIRESTORE_EVENTS_CAL_DOC)
                .collection(String.valueOf(year))     //YEAR
                .document(String.valueOf(month))       //MONTH
                .collection(String.valueOf(day))       //DATE
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                        assert queryDocumentSnapshots != null;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            events_noDataFound.setVisibility(View.GONE);
                            utilsArrayList.clear();
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot dataSnapshot : list) {
                                EventsUtils eventsUtils = dataSnapshot.toObject(EventsUtils.class);
                                utilsArrayList.add(eventsUtils);
                            }
                        } else {
                            utilsArrayList.clear();
                            events_progress_bar.setVisibility(View.GONE);
                            events_noDataFound.setVisibility(View.VISIBLE);
                            events_noDataFound.playAnimation();
                        }
                        eventsAdapter.notifyDataSetChanged();
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
        fetchEvents(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
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
            public void onItemClick(Context context, int position) {
                Calendar clickCalendar = Calendar.getInstance();
                clickCalendar.setTime(dates.get(position));
                selectedDay = clickCalendar.get(Calendar.DAY_OF_MONTH);
            }
        });
    }
}