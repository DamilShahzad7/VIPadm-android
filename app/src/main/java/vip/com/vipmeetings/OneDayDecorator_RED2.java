package vip.com.vipmeetings;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

import vip.com.vipmeetings.utilities.Constants;

public class OneDayDecorator_RED2 implements DayViewDecorator {

    private CalendarDay date;

    public boolean isCurrentDateDecorate = true;


    public boolean isCurrentDateDecorate() {
        return isCurrentDateDecorate;
    }

    public void setCurrentDateDecorate(boolean currentDateDecorate) {
        isCurrentDateDecorate = currentDateDecorate;
    }

    Context c;

    public OneDayDecorator_RED2(Context c) {
        date = CalendarDay.today();
        this.c = c;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {


        if (date.getDate().toString().
                equalsIgnoreCase(Constants.formatDate(new Date()))) {

            if (!isCurrentDateDecorate) {

                view.setSelectionDrawable(c.getResources().getDrawable(R.drawable.transparant));
                view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.md_black_1000)));
            } else {


                view.setSelectionDrawable(c.getResources().getDrawable(R.drawable.datecolor));
                view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.md_white_1000)));
            }



        } else {
            view.setSelectionDrawable(c.getResources().getDrawable(R.drawable.datecolor2));
            view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.md_white_1000)));

        }
    }

    /**
     * We're changing the internals, so make sure to call
     * {@linkplai invalidateDecorators()}
     */
    public void setDate(CalendarDay date) {

        this.date = date;
    }
} 