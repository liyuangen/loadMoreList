package com.lyg.listView;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lyg.library.R;

public class LoadListView extends ListView implements AbsListView.OnScrollListener {
    private FrameLayout footerView;
    private boolean isFirst = true;
    private boolean isFinish = true;
    private loadListener loadListener;

    public LoadListView(Context context) {
        this(context, null);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        footerView = (FrameLayout) LayoutInflater.from(context)
                .inflate(R.layout.list_item_footer, this, false);
        setOnScrollListener(this);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (isFirst) {
            isFirst = false;
            addFooterView(footerView);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isFinish && isDown()) {
            isFinish = false;
            if (loadListener != null)
                loadListener.loadMore();
        }
    }

    private boolean isDown() {
        return !ViewCompat.canScrollVertically(this, 1);
    }

    public void complete() {
        isFinish = true;
    }

    public void setLoadListener(loadListener listener) {
        loadListener = listener;
    }

    public void noMore() {
        footerView.setVisibility(GONE);
    }

    public interface loadListener {
        void loadMore();
    }
}