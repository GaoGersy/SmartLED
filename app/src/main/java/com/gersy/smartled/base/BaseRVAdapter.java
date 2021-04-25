package com.gersy.smartled.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.gersy.smartled.listener.OnItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 Gersy
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mData = new ArrayList<>();
    public Context mContext;
    private OnItemClickListener mListener;

    public BaseRVAdapter(List<T> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(data);
        }
    }

    public void addAll(List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void add(T data) {
        mData.add(data);
        notifyItemInserted(mData.size() - 1);
    }

    public void add(int index, T data) {
        mData.add(index, data);
        notifyItemInserted(index);
    }

    public void appendAll(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void appendPre(T t) {
        mData.add(0, t);
        notifyDataSetChanged();
    }

    public void append(T t) {
        mData.add(t);
        notifyDataSetChanged();
    }

    public void updateItem(T t) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i) == t) {
                notifyItemChanged(i);
                break;
            }
        }

    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mData;
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return getViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseViewHolder<T> viewHolder = (BaseViewHolder<T>) holder;
        if (mData.size() > 0) {
            viewHolder.setData(mData.get(position), position);
            View itemView = viewHolder.itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(itemView, position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected abstract BaseViewHolder getViewHolder(ViewGroup view, int viewType);

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void onItemClick(View view, int position) {
        if (mListener != null) {
            mListener.onItemClick(view, position);
        }
    }

}