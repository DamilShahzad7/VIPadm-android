package vip.com.vipmeetings.utilities;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class CustomMaterialAutoCompleteTextView extends MaterialAutoCompleteTextView {

    private AccessibilityManager accessibilityManager;
    private ModalListPopup modalListPopup;

    public CustomMaterialAutoCompleteTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomMaterialAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomMaterialAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        modalListPopup = new ModalListPopup(context);
    }

    @Override
    public void showDropDown() {
        if (accessibilityManager != null && accessibilityManager.isTouchExplorationEnabled()) {
            modalListPopup.show(getAnchorView());
        } else {
            super.showDropDown();
        }
    }

    private View getAnchorView() {

        return this; // Anchor the popup to the AutoCompleteTextView
    }
}