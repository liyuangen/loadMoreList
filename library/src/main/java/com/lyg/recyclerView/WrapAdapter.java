package com.lyg.recyclerView;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_VIEW_TYPE_HEADER = -2;
    public static final int ITEM_VIEW_TYPE_FOOTER = -3;
    private int headerPosition = 0;
    private int footerPosition = 0;
    private final RecyclerView.Adapter mAdapter;
    ArrayList<LRecycleView.FixedViewInfo> mHeaderViewInfos;
    ArrayList<LRecycleView.FixedViewInfo> mFooterViewInfos;
    static final ArrayList<LRecycleView.FixedViewInfo> EMPTY_INFO_LIST = new ArrayList<>();
    public WrapAdapter(ArrayList<LRecycleView.FixedViewInfo> headerViewInfos,
                       ArrayList<LRecycleView.FixedViewInfo> footerViewInfos,
                       RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        if (headerViewInfos == null) {
            mHeaderViewInfos = EMPTY_INFO_LIST;
        } else {
            mHeaderViewInfos = headerViewInfos;
        }
        if (footerViewInfos == null) {
            mFooterViewInfos = EMPTY_INFO_LIST;
        } else {
            mFooterViewInfos = footerViewInfos;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            return new ViewHolder(mHeaderViewInfos.get(headerPosition++).view);
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return new ViewHolder(mFooterViewInfos.get(footerPosition++).view);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int numHeaders = getHeaderViewCount();
        if (position < numHeaders) {
            return;
        }
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, position-numHeaders);
            }
        }
    }
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            int numHeaders = getHeaderViewCount();
            if (holder.getLayoutPosition() < numHeaders) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            final int adjPosition = holder.getLayoutPosition() - numHeaders;
            int adapterCount = 0;
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return;
            }
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int numHeaders = getHeaderViewCount();
                    if (position < numHeaders) {
                        return gridManager.getSpanCount();
                    }
                    final int adjPosition = position - numHeaders;
                    int adapterCount = 0;
                    adapterCount = mAdapter.getItemCount();
                    if (adjPosition < adapterCount) {
                        return 1;
                    }
                    return gridManager.getSpanCount();
                }
            });
        }
    }
    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (mAdapter != null)
            mAdapter.registerAdapterDataObserver(observer);
    }
    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        if (mAdapter != null)
            mAdapter.unregisterAdapterDataObserver(observer);
    }
    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return mAdapter.getItemCount() + getHeaderViewCount() + getFooterViewCount();
        }
        return getHeaderViewCount() + getFooterViewCount();
    }
    public int getHeaderViewCount() {
        return mHeaderViewInfos.size();
    }
    public int getFooterViewCount() {
        return mFooterViewInfos.size();
    }
    @Override
    public int getItemViewType(int position) {
        int numHeaders = getHeaderViewCount();
        if (position < numHeaders) {
            return ITEM_VIEW_TYPE_HEADER;
        }
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        return ITEM_VIEW_TYPE_FOOTER;
    }
    @Override
    public long getItemId(int position) {
        int numHeaders = getHeaderViewCount();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemId(adjPosition);
            }
        }
        return -1;
    }
    public boolean removeHeaderView(View v) {
        for (int i = 0; i < mHeaderViewInfos.size(); i++) {
            LRecycleView.FixedViewInfo info = mHeaderViewInfos.get(i);
            if (info.view == v) {
                mHeaderViewInfos.remove(i);
                return true;
            }
        }
        return false;
    }
    public boolean removeFooterView(View v) {
        for (int i = 0; i < mFooterViewInfos.size(); i++) {
            LRecycleView.FixedViewInfo info = mFooterViewInfos.get(i);
            if (info.view == v) {
                mFooterViewInfos.remove(i);
                return true;
            }
        }
        return false;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}