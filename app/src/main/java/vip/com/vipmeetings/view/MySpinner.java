package vip.com.vipmeetings.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.AttributeSet;

public class MySpinner extends AppCompatSpinner {

    OnItemSelectedListener listener;

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void setSelection(int position) {
        super.setSelection(position);

        if (position == getSelectedItemPosition() && listener != null) {
            listener.onItemSelected(null, null, position, 0);
        }
    }


    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
}