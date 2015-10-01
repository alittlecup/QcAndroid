package com.qingchengfit.fitcoach.component;

import android.content.Context;
import android.util.AttributeSet;

import com.paper.paperbaselibrary.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/29 2015.
 */
public class DateSegmentLayout extends CustomSetmentLayout {
    String[] mWeek = {"MON", "TUS", "WEN", "TUR", "FRI", "SAT", "SUN"};

    public DateSegmentLayout(Context context) {
        super(context);
    }

    public DateSegmentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateSegmentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int x = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 0; i < 6; i++) {
            calendar.add(Calendar.DATE, 1);
            ((ScheduleDateLayout) getChildAt(i)).setWeekday(mWeek[(x + i) % 7], DateUtils.getOnlyDay(calendar.getTime()));
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
//            ((ScheduleDateLayout) getChildAt(i)).setSegmentListener(v ->
//                    onSegmentClick(v.getId()));
        }

    }
}
