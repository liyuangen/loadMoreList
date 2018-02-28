package com.lyg.recyclerView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class LRecycleView extends RecyclerView {
    private Adapter mAdapter;
    private View emptyView;
    /**
     * A class that represents a fixed view in a list, for example a header at the top
     * or a footer at the bottom.
     */
    public class FixedViewInfo {
        /** The view to add to the list */
        public View view;
        public Object data;
        /** <code>true</code> if the fixed view should be selectable in the list */
        public boolean isSelectable;
    }
    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<>();
    public LRecycleView(Context context) {
        this(context, null);
    }
    public LRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void addHeaderView(View v, Object data, boolean isSelectable) {
        final FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        mHeaderViewInfos.add(info);
    }
    public void addHeaderView(View v){
        addHeaderView(v,null,true);
    }
    public int getHeaderViewCount() {
        return mHeaderViewInfos.size();
    }
    public boolean removeHeaderView(View v){
        if (mHeaderViewInfos.size() > 0) {
            boolean result = false;
            if (mAdapter != null && ((WrapAdapter) mAdapter).removeHeaderView(v)) {
                result = true;
            }
            removeFixedViewInfo(v, mHeaderViewInfos);
            return result;
        }
        return false;
    }
    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; ++i) {
            FixedViewInfo info = where.get(i);
            if (info.view == v) {
                where.remove(i);
                break;
            }
        }
    }
    public void addFooterView(View v, Object data, boolean isSelectable) {
        final FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        mFooterViewInfos.add(info);
    }
    public void addFooterView(View v){
        addFooterView(v,null,true);
    }
    public boolean removeFooterView(View v){
        if (mFooterViewInfos.size() > 0) {
            boolean result = false;
            if (mAdapter != null && ((WrapAdapter) mAdapter).removeFooterView(v)) {
                result = true;
            }
            removeFixedViewInfo(v, mFooterViewInfos);
            return result;
        }
        return false;
    }
    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(observer);
        }
        if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
            mAdapter = new WrapAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
        } else {
            mAdapter = adapter;
        }
        super.setAdapter(mAdapter);
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(observer);
        }
        checkIfEmpty();
    }
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };
    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }
}