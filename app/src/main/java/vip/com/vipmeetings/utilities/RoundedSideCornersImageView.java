package vip.com.vipmeetings.utilities;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

import com.facebook.drawee.view.SimpleDraweeView;

public class RoundedSideCornersImageView extends SimpleDraweeView {

    private float cornerRadius = 10.0f; // default corner radius

    public RoundedSideCornersImageView(Context context) {
        super(context);
    }

    public RoundedSideCornersImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedSideCornersImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        int width = this.getWidth();
        int height = this.getHeight();
        clipPath.addRoundRect(0, 0, width, height, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }
}