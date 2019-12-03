package com.vios.patient.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

public class SquareLayoutH extends LinearLayout {
    public SquareLayoutH(Context context) {
        super(context);
    }

    public SquareLayoutH(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayoutH(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareLayoutH(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

       /* ViewGroup.LayoutParams params = getLayoutParams();
        params.width = getMeasuredHeight();*/

      /*  if (widthMeasureSpec < heightMeasureSpec)
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        else*/
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
