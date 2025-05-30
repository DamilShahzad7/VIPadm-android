package vip.com.vipmeetings.utilities;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import vip.com.vipmeetings.R;

public class ModalListPopup {
    private PopupWindow popupWindow;
    private Context context;

    public ModalListPopup(Context context) {
        this.context = context;
        initPopupWindow();
    }

    private void initPopupWindow() {
        // Initialize your custom popup window
        View contentView = LayoutInflater.from(context).inflate(R.layout.dropdown_item, null); // Replace with your layout
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        // Set animation style
        popupWindow.setAnimationStyle(R.style.PopupAnimation); // Use system fade animation style
    }

    public void show(View anchorView) {
        // Get the location of the anchor view on screen
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);

        // Calculate the x and y position for the popup window
        int offsetX = 0;  // You can adjust this offset if needed
        int offsetY = 40; // Initial space between the popup and the anchor view

        // Calculate additional offset based on anchor view's height and desired space
        offsetY += anchorView.getHeight() + context.getResources().getDimensionPixelOffset(R.dimen.popup_offset_y);

        // Show the popup window with the calculated position
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0] + offsetX,
                location[1] + anchorView.getHeight() + offsetY);

        // Custom animation logic - fade in animation
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.popup_enter);
        anchorView.startAnimation(animation); // Apply animation to the content view
    }

    public void dismiss() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
