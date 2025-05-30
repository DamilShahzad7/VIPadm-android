package vip.com.vipmeetings;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;


public class DateSeletedDecorator implements DayViewDecorator {

    private CalendarDay date;
    Context c;

    public DateSeletedDecorator(Context c) {
        date = CalendarDay.today();
        this.c = c;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return  day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {



        view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.md_white_1000)));
        view.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.datecolor2));
    }

    /**
     * We're changing the internals, so make sure to call
     * {@linkplai invalidateDecorators()}
     */
    public void setDate(CalendarDay date) {

        this.date = date;
    }
}