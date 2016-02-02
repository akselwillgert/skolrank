package se.subsurface.skolrank;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class GoneTextView extends TextView {
    public GoneTextView(Context context) {
        super(context);
    }

    public GoneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoneTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("SetTextI18n")
    public void setTextOrGone(String text, String unit) {
        super.setText(text + unit);
        if (text == null || text.equals("") || text.equals("-1") || text.equals("-1.0")) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }
}
