package com.wuwang.mplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by LinkedME06 on 11/01/2017.
 */

public class CloseView extends View {

    private Context mContext;
    private Paint mCirclePaint;
    private Paint mLinePaint;

    public CloseView(Context context) {
        this(context, null);
    }

    public CloseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.argb(80, 0, 0, 0));
        mCirclePaint.setStyle(Paint.Style.FILL);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.argb(255, 255, 255, 255));
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(AndroidUtils.convertDpToPixels(mContext, 2));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth() / 2;
        int height = getMeasuredHeight() / 2;
        canvas.drawCircle(width, height, width, mCirclePaint);
        int leftStartX = (int) (width - Math.sqrt(2) / 4 * width);
        int leftStartY = (int) (height - Math.sqrt(2) / 4 * height);
        int leftEndX = (int) (width + Math.sqrt(2) / 4 * width);
        int leftEndY = (int) (height + Math.sqrt(2) / 4 * height);

        int rightStartX = (int) (width + Math.sqrt(2) / 4 * width);
        int rightStartY = (int) (height - Math.sqrt(2) / 4 * height);
        int rightEndX = (int) (width - Math.sqrt(2) / 4 * width);
        int rightEndY = (int) (height + Math.sqrt(2) / 4 * height);
        canvas.drawCircle(leftStartX, leftStartY, AndroidUtils.convertDpToPixels(mContext, 1), mLinePaint);
        canvas.drawCircle(leftEndX, leftEndY, AndroidUtils.convertDpToPixels(mContext, 1), mLinePaint);
        canvas.drawLine(leftStartX, leftStartY, leftEndX, leftEndY, mLinePaint);
        canvas.drawLine(rightStartX, rightStartY, rightEndX, rightEndY, mLinePaint);
        canvas.drawCircle(rightStartX, rightStartY, AndroidUtils.convertDpToPixels(mContext, 1), mLinePaint);
        canvas.drawCircle(rightEndX, rightEndY, AndroidUtils.convertDpToPixels(mContext, 1), mLinePaint);
    }


}
