package com.myscript.atk.math.sample.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by hd_chen on 2016/8/11.
 */
public class MyScrollView extends ScrollView {

    boolean isDrawing = true;
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawing(boolean isDrawing){
        this.isDrawing = isDrawing;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return !isDrawing && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isDrawing && super.onInterceptTouchEvent(ev);
    }
}
