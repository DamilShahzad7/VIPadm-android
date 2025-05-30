package vip.com.vipmeetings.bluno;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by srinath on 5/8/2016.
 */
public class CustomFont1 extends AppCompatTextView {

    public CustomFont1(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomFont1(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public CustomFont1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = Typeface.createFromAsset(context.getAssets(),"fonts/Noteworthy-Bold.otf");
        setTypeface(customFont);
    }
}
