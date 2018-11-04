package com.ahj.mr.circularprogress;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class CircularProgress extends View {

    private int max = 100;
    private int min = 0;
    private int progress = 0;
    private int color = Color.DKGRAY;
    private float strokeWidth = 4;
    private boolean autoColored = false;
    private boolean showPercent = true;

    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint percentPaint;

    private RectF rectF;

    private Rect bounds;

    public CircularProgress(Context context) {
        super(context);
        init(context);
    }

    public CircularProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs
                , R.styleable.CircularProgress
                , defStyleAttr
                , 0);
        max = ta.getInteger(R.styleable.CircularProgress_cp_max, 100);
        min = ta.getInteger(R.styleable.CircularProgress_cp_min, 0);
        if (max < min)
            max = min;
        progress = ta.getInteger(R.styleable.CircularProgress_cp_progress, min);
        if (progress < min) {
            progress = min;
        } else if (progress > max) {
            progress = max;
        }
        color = ta.getColor(R.styleable.CircularProgress_cp_color, Color.DKGRAY);
        strokeWidth = ta.getDimension(R.styleable.CircularProgress_cp_stroke_width, 4);
        autoColored = ta.getBoolean(R.styleable.CircularProgress_cp_auto_colored, false);
        showPercent = ta.getBoolean(R.styleable.CircularProgress_cp_show_percent, true);
        ta.recycle();
        init(context);
    }

    public void init(Context context) {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(adjustAlpha(color, 0.2f));
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(strokeWidth);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);

        rectF = new RectF();

        bounds = new Rect();

        percentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        percentPaint.setColor(color);
        percentPaint.setTextAlign(Paint.Align.CENTER);
        percentPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        float density = context.getResources().getDisplayMetrics().density;
        percentPaint.setTextSize(20 * density);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        rectF.set(strokeWidth / 2
                , strokeWidth / 2
                , min - strokeWidth / 2
                , min - strokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int percent = (progress - min) * 100 / (max - min);

        if (autoColored) {
            int r = (100 - percent) * 200 / 100;
            int g = percent * 200 / 100;
            int b = 0;
            int newColor = Color.rgb(r, g, b);
            backgroundPaint.setColor(adjustAlpha(newColor, 0.2f));
            foregroundPaint.setColor(newColor);
            percentPaint.setColor(newColor);
        } else {
            backgroundPaint.setColor(adjustAlpha(color, 0.2f));
            foregroundPaint.setColor(color);
            percentPaint.setColor(color);
        }

        canvas.drawOval(rectF, backgroundPaint);

        int sweepAngle = (progress - min) * 360 / (max - min);
        canvas.drawArc(rectF, -90, sweepAngle, false, foregroundPaint);

        if (showPercent) {
            String percentLabel = percent + " %";
            float x = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
            float y = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;

            percentPaint.getTextBounds(percentLabel, 0, percentLabel.length(), bounds);
            y += bounds.height() / 2;
            canvas.drawText(percentLabel, x, y, percentPaint);
        } else {

        }
    }

    public void setMax(int max) {
        this.max = (max > min) ? max : min;
        invalidate();
    }

    public void setMin(int min) {
        this.min = (min > 0) ? min : 0;
        invalidate();
    }

    public void setProgress(int value) {
        this.progress = (value < min) ? min : (value > max) ? max : value;
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int getProgress() {
        return progress;
    }

    public void setStrokeWidth(float strokeWidth) {
        if (strokeWidth < 0 || strokeWidth == this.strokeWidth)
            return;
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        backgroundPaint.setStrokeWidth(strokeWidth);
        invalidate();
        requestLayout();
    }

    public boolean isAutoColored() {
        return autoColored;
    }

    public void setAutoColored(boolean autoColored) {
        if (this.autoColored == autoColored)
            return;
        this.autoColored = autoColored;
        invalidate();
    }

    public boolean isShowPercent() {
        return showPercent;
    }

    public void setShowPercent(boolean showPercent) {
        if (this.showPercent == showPercent)
            return;
        this.showPercent = showPercent;
        invalidate();
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
        foregroundPaint.setColor(color);
        backgroundPaint.setColor(adjustAlpha(color, 0.2f));
        invalidate();
    }

    private int adjustAlpha(int color, float factor) {
        if (factor < 0.0f || factor > 1.0f)
            return color;
        float alpha = Math.round(Color.alpha(color) * factor);
        return Color.argb(
                (int) alpha
                , Color.red(color)
                , Color.green(color)
                , Color.blue(color)
        );
    }

    public void setProgressWithAnimation(int value) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", value);
        long duration = Math.abs(value - progress) * 2000L / (max - min);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}
