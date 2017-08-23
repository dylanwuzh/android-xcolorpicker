package com.xcolorpicker.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 选择颜色值的调色板。
 *
 * @author wuzhen
 * @version 2017-08-23
 */
public class XColorPicker extends View {

    private static final float DEFAULT_POINTER_WIDTH_DP = 1f;
    private static final float DEFAULT_POINTER_LENGTH_DP = 12f;

    private final float[] colorHsv = {0f, 0f, 1f};

    private int mPointerLength;

    private int innerPadding;
    private int lastSelectedColor;
    private float circleRadius;
    private float innerCircleRadius;
    private int[] pixels;

    private Rect rect;
    private Bitmap bitmap;

    private Paint mPointerPaint = new Paint();
    private Paint mCirclePaint = new Paint();
    private PointF mSelectedPoint = new PointF();

    private OnColorSelectListener mOnColorSelectListener;

    public XColorPicker(Context context) {
        super(context);
        init(context, null, 0);
    }

    public XColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public XColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.XColorPicker, defStyleAttr, 0);
        mPointerLength = a.getDimensionPixelOffset(R.styleable.XColorPicker_xcp_pointerLength,
                dp2px(DEFAULT_POINTER_LENGTH_DP));
        int pointerWidth = a.getDimensionPixelOffset(R.styleable.XColorPicker_xcp_pointerWidth,
                dp2px(DEFAULT_POINTER_WIDTH_DP));
        int pointerColor = a.getColor(R.styleable.XColorPicker_xcp_pointerColor, Color.BLACK);
        a.recycle();

        mPointerPaint.setColor(pointerColor);
        mPointerPaint.setStrokeWidth(pointerWidth);

        innerPadding = mPointerLength / 2;

        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setAntiAlias(true);

        setColor(Color.HSVToColor(colorHsv));
    }

    /**
     * 设置当前选中的颜色。
     *
     * @param color 颜色值
     */
    public void setColor(int color) {
        if (mOnColorSelectListener != null) {
            mOnColorSelectListener.onColorSelected(color, lastSelectedColor);
        }
        lastSelectedColor = color;

        Color.colorToHSV(color, colorHsv);
        invalidate();
    }

    /**
     * 设置颜色选中的监听事件。
     *
     * @param listener 监听事件
     */
    public void setOnColorSelectListener(OnColorSelectListener listener) {
        this.mOnColorSelectListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, rect, null);

            // TODO 防止边缘锯齿的更好方法
            Drawable drawable = getBackground();
            if (drawable instanceof ColorDrawable) {
                innerCircleRadius = circleRadius - 2;
                int color = ((ColorDrawable) drawable).getColor();
                mCirclePaint.setColor(color);
                canvas.drawCircle(getWidth() / 2.f, getHeight() / 2.f, getWidth() / 2.f - innerPadding - 1, mCirclePaint); //画出圆环
            }

            float hueInPiInterval = colorHsv[0] / 180f * (float) Math.PI;

            mSelectedPoint.x = (float) (rect.left + (-Math.cos(hueInPiInterval) * colorHsv[1] *
                    innerCircleRadius + circleRadius));
            mSelectedPoint.y = (float) (rect.top + (-Math.sin(hueInPiInterval) * colorHsv[1] *
                    innerCircleRadius + circleRadius));

            canvas.drawLine(mSelectedPoint.x - mPointerLength / 2.f, mSelectedPoint.y,
                    mSelectedPoint.x + mPointerLength / 2.f, mSelectedPoint.y, mPointerPaint);
            canvas.drawLine(mSelectedPoint.x, mSelectedPoint.y - mPointerLength / 2.f,
                    mSelectedPoint.x, mSelectedPoint.y + mPointerLength / 2.f, mPointerPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rect = new Rect(innerPadding, innerPadding, w - innerPadding, h - innerPadding);
        bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        circleRadius = Math.min(rect.width(), rect.height()) / 2;
        innerCircleRadius = circleRadius;

        pixels = new int[rect.width() * rect.height()];

        createBitmap();
    }

    private void createBitmap() {
        int width = rect.width();
        int height = rect.height();
        int[] tempPixels = new int[width * height];

        float[] hsv = new float[]{0f, 0f, 1f};

        int x = (int) -circleRadius, y = (int) -circleRadius;
        for (int i = 0; i < tempPixels.length; i++) {
            if (i % width == 0) {
                x = (int) -circleRadius;
                y++;
            } else {
                x++;
            }

            double centerDist = Math.sqrt(x * x + y * y);
            if (centerDist <= circleRadius) {
                hsv[0] = (float) (Math.atan2(y, x) / Math.PI * 180f) + 180;
                hsv[1] = (float) (centerDist / circleRadius);
                tempPixels[i] = Color.HSVToColor(255, hsv);
            } else {
                tempPixels[i] = 0x00000000;
            }
        }

        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                pixels[x * height + y] = tempPixels[x * height + y];
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        width = height = Math.min(maxWidth, maxHeight);

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            int newColor = getColorForPoint((int) event.getX(), (int) event.getY(), colorHsv);
            if (mOnColorSelectListener != null) {
                mOnColorSelectListener.onColorSelected(newColor, lastSelectedColor);
            }
            lastSelectedColor = newColor;
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    private int getColorForPoint(int x, int y, float[] hsv) {
        x -= circleRadius;
        y -= circleRadius;
        double centerDist = Math.sqrt(x * x + y * y);
        hsv[0] = (float) (Math.atan2(y, x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (centerDist / circleRadius)));
        return Color.HSVToColor(255, hsv);
    }

    private int dp2px(float dpValue) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (density * dpValue + 0.5f);
    }
}
