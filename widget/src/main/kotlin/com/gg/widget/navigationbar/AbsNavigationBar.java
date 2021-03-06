package com.gg.widget.navigationbar;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Creator : GG
 * Time    : 2017/10/18
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

public abstract class AbsNavigationBar<P extends AbsNavigationBar.Builder.AbsNavigationParams> implements INavigationBar {

    private P mParams;
    private View mParentView;

    public AbsNavigationBar(P params) {
        this.mParams = params;
        createAndBindView();
    }

    public P getParams() {
        return mParams;
    }

    private void createAndBindView() {

        if (mParams.mParent == null) {
            ViewGroup activityRoot = (ViewGroup) ((Activity) (mParams.mContext)).getWindow().getDecorView();
            mParams.mParent = (ViewGroup) activityRoot.getChildAt(0);
        }


        //需要单独处理Activity  Activity的实现方式和AppCompatActivity的不一样
        if (mParams.mParent == null) {
            return;
        }

        mParentView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(), mParams.mParent, false);

        mParams.mParent.addView(mParentView, 0);

        applyViews();
    }


    public void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (TextUtils.isEmpty(text)) {
            tv.setVisibility(View.INVISIBLE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
    }

    public void setIcon(int viewId, int iconId) {
        ImageView iv = findViewById(viewId);
        if (iconId != 0) {
            iv.setVisibility(View.VISIBLE);
            iv.setImageResource(iconId);
        } else {
            iv.setVisibility(View.GONE);
        }
    }


    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        findViewById(viewId).setOnClickListener(listener);
    }


    public <T extends View> T findViewById(int viewId) {
        return (T) mParentView.findViewById(viewId);
    }


    public abstract static class Builder {

        public AbsNavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            P = new AbsNavigationParams(context, parent);
        }

        public Builder(Context context) {
            P = new AbsNavigationParams(context, null);
        }

        public abstract AbsNavigationBar create();


        static class AbsNavigationParams {


            Context mContext;

            ViewGroup mParent;

            AbsNavigationParams(Context context, ViewGroup parent) {
                mContext = context;
                mParent = parent;
            }


        }


    }
}