package vn.com.hieptt149.workoutmanager.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;

public class ExpandableChildGridView extends GridView {
    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public ExpandableChildGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableChildGridView(Context context) {
        super(context);
    }

    public ExpandableChildGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != oldCount) {
            int height = getChildAt(0).getHeight() + 1;
            oldCount = getCount();
            params = getLayoutParams();
            params.height = getCount() * height;
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }
}
