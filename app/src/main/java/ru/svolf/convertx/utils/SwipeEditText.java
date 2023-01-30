package ru.svolf.convertx.utils;

import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

public class SwipeEditText extends AppCompatEditText {
    private static final String TAG = "SwipeEditText";
    private static final float SWIPE_THRESHOLD = 0.2f;
    private float startX;
    private float endX;
    private float deltaX;
    private OnSwipeListener onSwipeListener;
    private OnCompoundClick onCompoundClickListener;

    public SwipeEditText(Context context) {
        super(context);
        init();
    }

    public SwipeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SwipeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Получаем ссылку на right CompoundDrawable
        Drawable drawable = getCompoundDrawables()[2];


        this.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    Log.d(TAG, "init: ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "init: ACTION_UP or MOVE");
                    if (drawable != null && onCompoundClickListener != null) {
                        if (startX >= (getRight() - drawable.getBounds().width())) {
                            onCompoundClickListener.onDrawableClicked();
                            break;
                        }
                    } else {
                        Log.d(TAG, "init: some is null!!!");
                    }
                    endX = event.getX();
                    deltaX = endX - startX;

                    if (deltaX > SWIPE_THRESHOLD * getWidth()) {
                        if (onSwipeListener != null) {
                            onSwipeListener.onSwipeLeft();
                        }
                        animate()
                                .translationX(getWidth() * SWIPE_THRESHOLD)
                                .withEndAction(() ->
                                        animate().translationX(0)
                                                .setDuration(200).start())
                                .setDuration(250)
                                .start();

                    } else if (deltaX < -SWIPE_THRESHOLD * getWidth()) {
                        if (onSwipeListener != null) {
                            onSwipeListener.onSwipeRight();
                        }
                        animate()
                                .translationX(getWidth() * -SWIPE_THRESHOLD)
                                .withEndAction(() ->
                                        animate().translationX(0)
                                                .setDuration(200).start())
                                .setDuration(250)
                                .start();
                    }
                    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    break;
            }
            return performClick();
        });
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean isHapticFeedbackEnabled() {
        return true;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeLeftListener) {
        this.onSwipeListener = onSwipeLeftListener;
    }

    public void setOnCompoundClickListener(OnCompoundClick clickListener) {
        this.onCompoundClickListener = clickListener;
    }

    public interface OnSwipeListener {
        void onSwipeLeft();

        void onSwipeRight();
    }

    public interface OnCompoundClick {
        void onDrawableClicked();
    }
}