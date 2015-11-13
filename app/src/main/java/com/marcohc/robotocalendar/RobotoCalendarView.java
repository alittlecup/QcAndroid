/*
 * Copyright (C) 2015 Marco Hernaiz Cao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marcohc.robotocalendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * The roboto calendar view
 *
 * @author Marco Hernaiz Cao
 */
public class RobotoCalendarView extends LinearLayout {

    // ************************************************************************************************************************************************************************
    // * Attributes
    // ************************************************************************************************************************************************************************

    public static final int RED_COLOR = R.color.red;
    public static final int GREY_COLOR = R.color.grey;
    public static final int GREEN_COLOR = R.color.green;
    public static final int BLUE_COLOR = R.color.blue;
    public static final int WHITE_COLOR = R.color.white;
    private static final String DAY_OF_WEEK = "dayOfWeek";
    private static final String DAY_OF_MONTH_TEXT = "dayOfMonthText";
    private static final String DAY_OF_MONTH_BACKGROUND = "dayOfMonthBackground";
    private static final String DAY_OF_MONTH_CONTAINER = "dayOfMonthContainer";
    private static final String FIRST_UNDERLINE = "firstUnderlineView";
    private static final String SECOND_UNDERLINE = "secondUnderlineView";
    // View
    private Context context;
    private TextView dateTitle;
    private ImageView leftButton;
    private ImageView rightButton;
    private View view;
    // Class
    private RobotoCalendarListener robotoCalendarListener;
    private Calendar currentCalendar;
    private Locale locale;
    // Style
    private int monthTitleColor;
    private int dayOfWeekColor;
    private int dayOfMonthColor;
    private Date lastCurrentDay;
    private Date lastSelectedDay;

    // ************************************************************************************************************************************************************************
    // * Initialization methods
    // ************************************************************************************************************************************************************************
    private OnClickListener onDayOfMonthClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            // Extract day selected
            ViewGroup dayOfMonthContainer = (ViewGroup) view;
            String tagId = (String) dayOfMonthContainer.getTag();
            tagId = tagId.substring(DAY_OF_MONTH_CONTAINER.length(), tagId.length());
            TextView dayOfMonthText = (TextView) view.findViewWithTag(DAY_OF_MONTH_TEXT + tagId);

            // Fire event
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentCalendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dayOfMonthText.getText().toString()));

            if (robotoCalendarListener == null) {
                throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
            } else {
                robotoCalendarListener.onDateSelected(calendar.getTime());
            }
        }
    };

    public RobotoCalendarView(Context context) {
        super(context);
        this.context = context;
        onCreateView();
    }

    public RobotoCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (isInEditMode()) {
            return;
        }
        getAttributes(context, attrs);
        onCreateView();
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RobotoCalendarView, 0, 0);
        monthTitleColor = typedArray.getColor(R.styleable.RobotoCalendarView_monthTitleColor, getResources().getColor(R.color.month_title));
        dayOfWeekColor = typedArray.getColor(R.styleable.RobotoCalendarView_dayOfWeekColor, getResources().getColor(R.color.day_of_week_color));
        dayOfMonthColor = typedArray.getColor(R.styleable.RobotoCalendarView_dayOfMonthColor, getResources().getColor(R.color.day_of_month));
        typedArray.recycle();
    }

    public View onCreateView() {

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflate.inflate(R.layout.roboto_calendar_picker_layout, this, true);

        findViewsById(view);

        initializeEventListeners();

        initializeComponentBehavior();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        return view;
    }

    private void findViewsById(View view) {
        leftButton = (ImageView) view.findViewById(R.id.leftButton);
        rightButton = (ImageView) view.findViewById(R.id.rightButton);
        dateTitle = (TextView) view.findViewById(R.id.dateTitle);
    }

    private void initializeEventListeners() {

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (robotoCalendarListener == null) {
                    throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
                }
                robotoCalendarListener.onLeftButtonClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (robotoCalendarListener == null) {
                    throw new IllegalStateException("You must assing a valid RobotoCalendarListener first!");
                }
                robotoCalendarListener.onRightButtonClick();
            }
        });
    }

    // ************************************************************************************************************************************************************************
    // * Initialization UI methods
    // ************************************************************************************************************************************************************************

    private void initializeComponentBehavior() {
        // Initialize calendar for current month
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        initializeCalendar(currentCalendar);
    }

    @SuppressLint("DefaultLocale")
    private void initializeTitleLayout() {

        // Apply styles
        int color = monthTitleColor;
        dateTitle.setTextColor(color);

//        String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendar.get(Calendar.MONTH)].toString();
//        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());
        String dateText = Integer.toString(currentCalendar.get(Calendar.MONTH) + 1);
//        Calendar calendar = Calendar.getInstance();
//        if (currentCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
//            dateTitle.setText(dateText);
//        } else {
        dateTitle.setText(currentCalendar.get(Calendar.YEAR) + getResources().getString(R.string.pickerview_year) + dateText + getResources().getString(R.string.pickerview_month));
//        }
    }

    @SuppressLint("DefaultLocale")
    private void initializeWeekDaysLayout() {

        // Apply styles
        int color = dayOfWeekColor;

        TextView dayOfWeek;
        String dayOfTheWeekString;
//        String[] weekDaysArray = new DateFormatSymbols(locale).getShortWeekdays();
        String[] weekDaysArray = new String[]{"六", "日", "一", "二", "三", "四", "五"};
        for (int i = 1; i < weekDaysArray.length; i++) {
            dayOfWeek = (TextView) view.findViewWithTag(DAY_OF_WEEK + getWeekIndex(i, currentCalendar));
            dayOfTheWeekString = weekDaysArray[i];
            dayOfTheWeekString = checkSpecificLocales(dayOfTheWeekString, i);
            dayOfWeek.setText(dayOfTheWeekString);

            // Apply styles
            dayOfWeek.setTextColor(color);
        }
    }

    @SuppressLint("DefaultLocale")
    private String checkSpecificLocales(String dayOfTheWeekString, int i) {
        // Set Wednesday as "X" in Spanish locale
        if (i == 4 && locale.getCountry().equals("ES")) {
            dayOfTheWeekString = "X";
        } else {
            dayOfTheWeekString = dayOfTheWeekString.substring(0, 1).toUpperCase();
        }
        return dayOfTheWeekString;
    }

    private void initializeDaysOfMonthLayout() {

        // Apply styles
        int color = dayOfMonthColor;
        TextView dayOfMonthText;
        View firstUnderline;
        View secondUnderline;
        ViewGroup dayOfMonthContainer;
        ViewGroup dayOfMonthBackground;

        for (int i = 1; i < 43; i++) {

            dayOfMonthContainer = (ViewGroup) view.findViewWithTag(DAY_OF_MONTH_CONTAINER + i);
            dayOfMonthBackground = (ViewGroup) view.findViewWithTag(DAY_OF_MONTH_BACKGROUND + i);
            dayOfMonthText = (TextView) view.findViewWithTag(DAY_OF_MONTH_TEXT + i);
            firstUnderline = (View) view.findViewWithTag(FIRST_UNDERLINE + i);
            secondUnderline = (View) view.findViewWithTag(SECOND_UNDERLINE + i);

            dayOfMonthText.setVisibility(View.INVISIBLE);
            firstUnderline.setVisibility(View.INVISIBLE);
            secondUnderline.setVisibility(View.INVISIBLE);

            // Apply styles
            dayOfMonthText.setTextColor(color);
            dayOfMonthText.setBackgroundResource(android.R.color.transparent);
            dayOfMonthContainer.setBackgroundResource(android.R.color.transparent);
            dayOfMonthContainer.setOnClickListener(null);
            dayOfMonthBackground.setBackgroundResource(android.R.color.transparent);
        }
    }

    private void setDaysInCalendar() {
        Calendar auxCalendar = Calendar.getInstance(locale);
        auxCalendar.setTime(currentCalendar.getTime());
        auxCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = auxCalendar.get(Calendar.DAY_OF_WEEK);
        TextView dayOfMonthText;
        ViewGroup dayOfMonthContainer;

        // Calculate dayOfMonthIndex
        int dayOfMonthIndex = getWeekIndex(firstDayOfMonth, auxCalendar);

        for (int i = 1; i <= auxCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++, dayOfMonthIndex++) {
            dayOfMonthContainer = (ViewGroup) view.findViewWithTag(DAY_OF_MONTH_CONTAINER + dayOfMonthIndex);
            dayOfMonthText = (TextView) view.findViewWithTag(DAY_OF_MONTH_TEXT + dayOfMonthIndex);
            if (dayOfMonthText == null) {
                break;
            }
            dayOfMonthContainer.setOnClickListener(onDayOfMonthClickListener);
            dayOfMonthText.setVisibility(View.VISIBLE);
            dayOfMonthText.setText(String.valueOf(i));
        }

        // If the last week row has no visible days, hide it or show it in case
        ViewGroup weekRow = (ViewGroup) view.findViewWithTag("weekRow6");
        dayOfMonthText = (TextView) view.findViewWithTag("dayOfMonthText36");
        if (dayOfMonthText.getVisibility() == INVISIBLE) {
            weekRow.setVisibility(GONE);
        } else {
            weekRow.setVisibility(VISIBLE);
        }
    }

    // ************************************************************************************************************************************************************************
    // * Getter methods
    // ************************************************************************************************************************************************************************

    private void clearDayOfTheMonthStyle(Date currentDate) {

        if (currentDate != null) {
            Calendar calendar = getCurrentCalendar();
            calendar.setTime(currentDate);
            ViewGroup dayOfMonthBackground = getDayOfMonthBackground(calendar);
            dayOfMonthBackground.setBackgroundResource(android.R.color.transparent);
        }
    }

    private ViewGroup getDayOfMonthBackground(Calendar currentCalendar) {
        return (ViewGroup) getView(DAY_OF_MONTH_BACKGROUND, currentCalendar);
    }

    private TextView getDayOfMonthText(Calendar currentCalendar) {
        return (TextView) getView(DAY_OF_MONTH_TEXT, currentCalendar);
    }

    private View getFirstUnderline(Calendar currentCalendar) {
        return getView(FIRST_UNDERLINE, currentCalendar);
    }

    private View getSecondUnderline(Calendar currentCalendar) {
        return getView(SECOND_UNDERLINE, currentCalendar);
    }

    private int getDayIndexByDate(Calendar currentCalendar) {
        int monthOffset = getMonthOffset(currentCalendar);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        int index = currentDay + monthOffset;
        return index;
    }

    private int getMonthOffset(Calendar currentCalendar) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentCalendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayWeekPosition = calendar.getFirstDayOfWeek();
        int dayPosition = calendar.get(Calendar.DAY_OF_WEEK);

        if (firstDayWeekPosition == 1) {
            return dayPosition - 1;
        } else {

            if (dayPosition == 1) {
                return 6;
            } else {
                return dayPosition - 2;
            }
        }
    }

    private int getWeekIndex(int weekIndex, Calendar currentCalendar) {
        int firstDayWeekPosition = currentCalendar.getFirstDayOfWeek();

        if (firstDayWeekPosition == 1) {
            return weekIndex;
        } else {

            if (weekIndex == 1) {
                return 7;
            } else {
                return weekIndex - 1;
            }
        }
    }

    private View getView(String key, Calendar currentCalendar) {
        int index = getDayIndexByDate(currentCalendar);
        View childView = (View) view.findViewWithTag(key + index);
        return childView;
    }

    // ************************************************************************************************************************************************************************
    // * Public calendar methods
    // ************************************************************************************************************************************************************************

    private Calendar getCurrentCalendar() {
        Calendar currentCalendar = Calendar.getInstance(context.getResources().getConfiguration().locale);
        return currentCalendar;
    }

    @SuppressLint("DefaultLocale")
    public void initializeCalendar(Calendar currentCalendar) {

        this.currentCalendar = currentCalendar;
        locale = context.getResources().getConfiguration().locale;

        // Set date title
        initializeTitleLayout();

        // Set weeks days titles
        initializeWeekDaysLayout();

        // Initialize days of the month
        initializeDaysOfMonthLayout();

        // Set days in calendar
        setDaysInCalendar();
    }

    public void markDayAsCurrentDay(Date currentDate) {
        if (currentDate != null) {
            lastCurrentDay = currentDate;
            Calendar currentCalendar = getCurrentCalendar();
            currentCalendar.setTime(currentDate);
            TextView dayOfMonth = getDayOfMonthText(currentCalendar);
            dayOfMonth.setTextColor(context.getResources().getColor(R.color.current_day_of_month));
        }
    }

    public void markDayAsSelectedDay(Date currentDate) {

        // Initialize attributes
        Calendar currentCalendar = getCurrentCalendar();
        currentCalendar.setTime(currentDate);

        // Clear previous marks
        clearDayOfTheMonthStyle(lastSelectedDay);

        markDayAsCurrentDay(lastCurrentDay);
        markDayAsCurrentDay(currentDate);
        // Store current values as last values
        storeLastValues(currentDate);

        // Mark current day as selected
        ViewGroup dayOfMonthBackground = getDayOfMonthBackground(currentCalendar);
        dayOfMonthBackground.setBackgroundResource(R.drawable.circle_prime);
    }

    private void storeLastValues(Date currentDate) {
        lastSelectedDay = currentDate;
    }

    public void markFirstUnderlineWithStyle(int style, Date currentDate) {
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        currentCalendar.setTime(currentDate);
        View underline = getFirstUnderline(currentCalendar);

        // Draw day with style
        underline.setVisibility(View.VISIBLE);
        underline.setBackgroundResource(style);
    }

    // ************************************************************************************************************************************************************************
    // * Public interface
    // ************************************************************************************************************************************************************************

    public void markSecondUnderlineWithStyle(int style, Date currentDate) {
        Locale locale = context.getResources().getConfiguration().locale;
        Calendar currentCalendar = Calendar.getInstance(locale);
        currentCalendar.setTime(currentDate);
        View underline = getSecondUnderline(currentCalendar);

        // Draw day with style
        underline.setVisibility(View.VISIBLE);
        if (style == RED_COLOR)
            underline.setBackgroundResource(R.drawable.red_dot);
        else underline.setBackgroundResource(R.drawable.grey_dot);
    }

    public void setRobotoCalendarListener(RobotoCalendarListener robotoCalendarListener) {
        this.robotoCalendarListener = robotoCalendarListener;
    }

    // ************************************************************************************************************************************************************************
    // * Event handler methods
    // ************************************************************************************************************************************************************************

    public interface RobotoCalendarListener {

        void onDateSelected(Date date);

        void onRightButtonClick();

        void onLeftButtonClick();
    }
}
