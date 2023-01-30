package ru.svolf.convertx.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.Toolbar;

public class BlurToolbar extends Toolbar {
    private Paint mBlurPaint;
    private Bitmap mBlurBitmap;

    public BlurToolbar(Context context) {
        super(context);
        init();
    }

    public BlurToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlurToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBlurPaint = new Paint();
        mBlurPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBlurBitmap == null) {
            mBlurBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas blurCanvas = new Canvas(mBlurBitmap);
            blurCanvas.drawColor(getSolidColor());
            blurCanvas.drawBitmap(mBlurBitmap, 0, 0, mBlurPaint);
        }
        canvas.drawBitmap(mBlurBitmap, 0, 0, null);
    }
}