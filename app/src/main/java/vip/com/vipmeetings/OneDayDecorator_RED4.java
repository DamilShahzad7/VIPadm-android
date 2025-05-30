package vip.com.vipmeetings;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class OneDayDecorator_RED4 implements DayViewDecorator {

    private CalendarDay date;
    Context c;

    public OneDayDecorator_RED4(Context c) {
        date = CalendarDay.today();
        this.c = c;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return date != null && day.isBefore(date);

    }


    @Override
    public void decorate(DayViewFacade view) {



        view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.textred)));

    }

    /**
     * We're changing the internals, so make sure to call
     * {@linkplai invalidateDecorators()}
     */
    public void setDate(CalendarDay date) {
        this.date = date;
    }
} 