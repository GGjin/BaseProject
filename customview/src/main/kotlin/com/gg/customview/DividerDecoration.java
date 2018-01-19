package com.gg.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gg.utils.Utils;


/**
 * Creator : GG
 * Time    : 2017/11/20
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

public class DividerDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private Drawable defaultDivider;
    private int mHeight;
    private int defaultHeight = 1;
    private Context mContext;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int startIndex = 0;

    public DividerDecoration(Context ctx) {
        mContext = ctx;
        defaultDivider = new ColorDrawable(Color.TRANSPARENT);
        mDivider = defaultDivider;
        mHeight = defaultHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position < startIndex)
                continue;
            if (startIndex == -1 && position == 0) {
                mDivider.setBounds(left + paddingLeft, child.getTop() - mHeight, right - paddingRight, child.getTop());
                mDivider.draw(c);
            }
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + mHeight;
            mDivider.setBounds(left + paddingLeft, top, right - paddingRight, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildPosition(view);
        if (startIndex == -1 && position == 0) {
            outRect.set(0, mHeight, 0, mHeight);
        } else {
            outRect.set(0, 0, 0, mHeight);
        }
    }

    public void setDividerDrawable(Drawable divider) {
        if (divider != null) {
            mDivider = divider;
        }
    }

    public void setDividerColor(int color) {
        mDivider = new ColorDrawable(color);
    }

    public void setDividerHeight(int height) {
        if (height >= 0) {
            mHeight = height;
        }
    }

    public void setDividerHeightDip(int dipHeight) {
        if (dipHeight >= 0) {
            mHeight = Utils.dip2px(mContext, dipHeight);
        }
    }

    public void setStartIndex(int index) {
        // -1 means drawAboveFirstItem
        startIndex = index;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setPaddingLeftDip(int paddingLeftDip){
        this.paddingLeft = Utils.dip2px(mContext, paddingLeftDip);
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public void setPaddingRightDip(int paddingRightDip) {
        this.paddingRight = Utils.dip2px(mContext, paddingRightDip);
    }
}
