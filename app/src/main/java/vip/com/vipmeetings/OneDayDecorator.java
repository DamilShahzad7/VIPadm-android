package vip.com.vipmeetings;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class OneDayDecorator implements DayViewDecorator {

    private CalendarDay date;
    Drawable highlightDrawable;

    public OneDayDecorator(Context c) {
        date = CalendarDay.today();
        highlightDrawable = ContextCompat.getDrawable(c, R.drawable.circlebackground);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.setBackgroundDrawable(highlightDrawable);
    }

    /**
     * We're changing the internals, so make sure to call
     * {@linkplai invalidateDecorators()}
     */
    public void setDate(CalendarDay date) {
        this.date = date;
    }
} 