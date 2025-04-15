package com.example.text.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SideBar extends LinearLayout {

    private OnLetterChangedListener mListener;

    public interface OnLetterChangedListener {
        void onLetterChanged(String letter);  // 字母变化时回调
        void onLetterReleased();             // 手指抬起时回调
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z", "#"};

        for (String letter : letters) {
            TextView tv = new TextView(getContext());
            tv.setText(letter);
            tv.setTextSize(12);
            tv.setTextColor(Color.GRAY);
            tv.setGravity(Gravity.CENTER);
            addView(tv);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float y = event.getY();
        int index = calculateIndex(y);

        if (index < 0 || index >= getChildCount()) return true;

        String letter = ((TextView) getChildAt(index)).getText().toString();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (mListener != null) {
                    mListener.onLetterChanged(letter);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null) {
                    mListener.onLetterReleased();
                }
                break;
        }
        return true;
    }

    public void setOnLetterChangedListener(OnLetterChangedListener listener) {
        this.mListener = listener;
    }

    private int calculateIndex(float y) {
        int itemHeight = getHeight() / getChildCount();
        return (int) (y / itemHeight);
    }
}