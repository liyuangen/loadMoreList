package com.lyg.recyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lyg.library.R;

public class LoadRecycleView extends LRecycleView {
    private LinearLayout footerView;
    public LoadRecycleView(Context context) {
        super(context);
    }
    public LoadRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(mOnScrollListener);
    }
    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        footerView = (LinearLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.list_item_footer, this,false);
        addFooterView(footerView);
    }
    private loadListener loadListener;
    private boolean isFinish = true;
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isFinish && isDown()) {
                isFinish = false;
                if (loadListener != null) loadListener.loadMore();
            }
        }
    };
    public void noMore() {
        footerView.setVisibility(GONE);
        ViewGroup.LayoutParams params = footerView.getLayoutParams();
        params.height = 0;
        footerView.setLayoutParams(params);
    }
    public void setLoadListener(loadListener listener) {
        loadListener = listener;
    }
    private boolean isDown() {
        return !ViewCompat.canScrollVertically(this, 1);
    }
    public void complete() {
        isFinish = true;
    }
    @Override
    protected void onDetachedFromWindow() {
        removeOnScrollListener(mOnScrollListener);
        super.onDetachedFromWindow();
    }
    public interface loadListener {
        void loadMore();
    }
}