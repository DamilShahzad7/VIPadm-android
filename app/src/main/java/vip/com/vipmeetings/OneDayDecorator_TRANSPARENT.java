package vip.com.vipmeetings;

import android.content.Context;

import androidx.core.content.ContextCompat;

import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

public class OneDayDecorator_TRANSPARENT implements DayViewDecorator {

    private CalendarDay date;
    Context c;

    public OneDayDecorator_TRANSPARENT(Context c) {
        date = CalendarDay.today();
        this.c = c;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {


//        view.addSpan(new BackgroundColorSpan(Color.TRANSPARENT));
//        view.setSelectionDrawable(c.getResources().getDrawable(R.drawable.transparant));
//        view.setBackgroundDrawable(
//                c.getResources().getDrawable(R.drawable.transparant));

        if (view.areDaysDisabled()) {

            view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.textred)));
        } else {

            view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(c, R.color.md_black_1000)));
        }
        view.setBackgroundDrawable(c.getResources().getDrawable(R.drawable.rectselect));
    }

    /**
     * We're changing the internals, so make sure to call
     * {@linkplai invalidateDecorators()}
     */
    public void setDate(CalendarDay date) {
        this.date = date;
    }
} 